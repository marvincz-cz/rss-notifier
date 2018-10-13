package cz.marvincz.rssnotifier.repository;

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
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.rssnotifier.retrofit.Client;
import cz.marvincz.rssnotifier.room.ChannelEntity;
import cz.marvincz.rssnotifier.room.ChannelWithItems;
import cz.marvincz.rssnotifier.room.Database;
import cz.marvincz.rssnotifier.util.MainThreadExecutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    @Inject
    Database database;

    private final ExecutorService executor;

    private List<RssChannel> channels;

    private boolean isLoading = false;
    private DataCallback<List<RssChannel>> callback;

    public Repository() {
        RssApplication.getAppComponent().inject(this);
        executor = Executors.newCachedThreadPool();
    }

    @UiThread
    public void getChannels(DataCallback<List<RssChannel>> cb) {
        callback = cb;
        if (isLoading) {
            callback.onLoading();
        } else if (channels != null) {
            callback.onData(channels);
        } else {
            isLoading = true;
            callback.onLoading();
            CompletableFuture.supplyAsync(database.dao()::getChannels, executor)
                    .thenApply(channelsWithItems -> channelsWithItems.stream()
                            .map(this::prepareDownload)
                            .map(supplier -> CompletableFuture.supplyAsync(supplier, executor))
                            .map(CompletableFuture::join)
                            .peek(rssChannel -> markReadFromDb(rssChannel, channelsWithItems))
                            .collect(Collectors.toList()))
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

    private Supplier<RssChannel> prepareDownload(ChannelEntity entity) {
        return () -> {
            try {
                return Client.call().rss(entity.url).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void markReadFromDb(RssChannel channel, List<ChannelWithItems> channelsWithItems) {
        String url = channel.link.toString();
        Set<Integer> readItems = channelsWithItems.stream()
                .filter(ch -> ch.url.equals(url))
                .flatMap(ch -> ch.readItems.stream())
                .map(item -> item.id)
                .collect(Collectors.toSet());

        for (RssItem item : channel.items) {
            if (readItems.contains(item.getId())) {
                item.seen = true;
            }
        }
    }

    @UiThread
    public void addChannel(String url, DataCallback<ChannelWithItems> cb) {
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
    private void callAddChannel(String uri, DataCallback<ChannelWithItems> cb) {
        Client.call().rss(uri).enqueue(new Callback<RssChannel>() {
            @Override
            public void onResponse(Call<RssChannel> call, Response<RssChannel> response) {
                if (response.body() != null) {
                    executor.submit(() -> {
                        ChannelWithItems channel = database.dao().saveChannel(new ChannelEntity(response.body()));
                        onMainThread(() -> cb.onData(channel));
                    });
                } else {
                    onMainThread(cb::onError);
                }
            }

            @Override
            public void onFailure(Call<RssChannel> call, Throwable t) {
                callback.onError();
            }
        });
    }

    private static void onMainThread(@NonNull Runnable runnable) {
        new MainThreadExecutor().execute(runnable);
    }
}
