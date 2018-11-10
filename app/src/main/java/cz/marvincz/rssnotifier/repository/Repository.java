package cz.marvincz.rssnotifier.repository;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.rssnotifier.retrofit.Client;
import cz.marvincz.rssnotifier.room.Database;
import cz.marvincz.rssnotifier.util.MainThreadExecutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    @Inject
    Database database;

    private final ExecutorService executor;

    private List<ChannelWithItems> channels;

    private boolean isLoading = false;
    private DataCallback<List<ChannelWithItems>> callback;

    public Repository() {
        RssApplication.getAppComponent().inject(this);
        executor = Executors.newCachedThreadPool();
    }

    @UiThread
    public void getChannels(DataCallback<List<ChannelWithItems>> cb) {
        callback = cb;
        if (isLoading) {
            callback.onLoading();
        } else if (channels != null) {
            callback.onData(channels);
        } else {
            isLoading = true;
            callback.onLoading();
            CompletableFuture.supplyAsync(database.dao()::getChannels, executor)
                    .whenCompleteAsync(this::returnResults, new MainThreadExecutor());
        }
    }

    @UiThread
    private void returnResults(List<ChannelWithItems> rssChannels, Throwable throwable) {
        isLoading = false;
        if (throwable != null) {
            Log.e("ERR", "Repository getChannels error", throwable);
            callback.onError();
        } else {
            channels = rssChannels;
            callback.onData(rssChannels);
        }
    }

    @UiThread
    public void reload(DataCallback<List<ChannelWithItems>> cb) {
        callback = cb;
        if (isLoading) {
            callback.onLoading();
        } else {
            isLoading = true;
            channels = null;
            callback.onLoading();
            CompletableFuture.supplyAsync(database.dao()::getChannels, executor)
                    .thenAccept(channelsWithItems -> channelsWithItems.stream()
                            .map(this::prepareDownload)
                            .map(supplier -> CompletableFuture.supplyAsync(supplier, executor))
                            .map(CompletableFuture::join)
                            .forEach(rssChannel -> updateInDb(rssChannel, channelsWithItems)))
                    .thenApply(v -> database.dao().getChannels())
                    .whenCompleteAsync(this::returnResults, new MainThreadExecutor());
        }
    }

    @AnyThread
    private Supplier<ChannelWithItems> prepareDownload(ChannelWithItems entity) {
        return () -> {
            try {
                return Optional.ofNullable(Client.call().rss(entity.accessUrl).execute())
                        .map(Response::body)
                        .map(ch -> ch.fixUrl(entity.accessUrl))
                        .orElse(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @WorkerThread
    private void updateInDb(ChannelWithItems rssChannel, List<ChannelWithItems> dbChannels) {
        List<RssItem> dbItems = dbChannels.stream()
                .filter(ch -> ch.accessUrl.equals(rssChannel.accessUrl))
                .flatMap(ch -> ch.items.stream())
                .collect(Collectors.toList());

        List<RssItem> toDelete = dbItems.stream()
                .filter(i -> !rssChannel.items.contains(i))
                .collect(Collectors.toList());

        List<RssItem> toAdd = new ArrayList<>();
        rssChannel.items.forEach(item -> {
            int i = dbItems.indexOf(item);
            if (i >= 0) {
                item.seen = dbItems.get(i).seen;
            } else {
                toAdd.add(item);
            }
        });

        database.dao().deleteAndInsertItems(toDelete, toAdd);
    }

    @UiThread
    public void addChannel(Uri url, DataCallback<ChannelWithItems> cb) {
        cb.onLoading();
        executor.submit(() -> {
            if (!database.dao().channelExists(url)) {
                callAddChannel(url, cb);
            } else {
                onMainThread(cb::onError);
            }
        });
    }

    @WorkerThread
    private void callAddChannel(Uri uri, DataCallback<ChannelWithItems> cb) {
        Client.call().rss(uri).enqueue(new Callback<ChannelWithItems>() {
            @Override
            public void onResponse(Call<ChannelWithItems> call, Response<ChannelWithItems> response) {
                if (response.body() != null) {
                    executor.submit(() -> {
                        ChannelWithItems channel = database.dao().saveChannel(new RssChannel());
                        onMainThread(() -> cb.onData(channel));
                    });
                } else {
                    onMainThread(cb::onError);
                }
            }

            @Override
            public void onFailure(Call<ChannelWithItems> call, Throwable t) {
                callback.onError();
            }
        });
    }

    private static void onMainThread(@NonNull Runnable runnable) {
        new MainThreadExecutor().execute(runnable);
    }
}
