package cz.marvincz.rssnotifier.repository;

import android.net.Uri;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
                    .whenCompleteAsync((rssChannels, throwable) -> {
                        isLoading = false;
                        if (throwable != null) {
                            callback.onError();
                        } else {
                            channels = rssChannels;
                            callback.onData(rssChannels);
                        }
                    }, new MainThreadExecutor());
        }
    }

    private Supplier<RssChannel> prepareDownload(RssChannel entity) {
        return () -> {
            try {
                return Client.call().rss(entity.link).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void markReadFromDb(ChannelWithItems channel, List<ChannelWithItems> channelsWithItems) {
        Set<Integer> readItems = channelsWithItems.stream()
                .filter(ch -> ch.link.equals(channel.link))
                .flatMap(ch -> ch.items.stream())
                .map(item -> item.id)
                .collect(Collectors.toSet());

        for (RssItem item : channel.items) {
            if (readItems.contains(item.id)) {
                item.seen = true;
            }
        }
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
