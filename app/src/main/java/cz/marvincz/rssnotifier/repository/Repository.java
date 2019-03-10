package cz.marvincz.rssnotifier.repository;

import android.util.Log;

import org.threeten.bp.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

import androidx.annotation.AnyThread;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.rssnotifier.retrofit.Client;
import cz.marvincz.rssnotifier.room.Database;
import cz.marvincz.rssnotifier.util.MainThreadExecutor;
import cz.marvincz.rssnotifier.util.PreferenceUtil;
import retrofit2.Response;

public class Repository {
    @Inject
    Database database;

    private static Duration OLD_DATA_DURATION = Duration.ofMinutes(30);

    private final ExecutorService executor;

    private List<ChannelWithItems> channels;

    private boolean isLoading = false;
    private DataCallback<List<ChannelWithItems>> callback;

    public Repository() {
        RssApplication.getAppComponent().inject(this);
        executor = Executors.newCachedThreadPool();
    }

    @UiThread
    public void loadData(DataCallback<List<ChannelWithItems>> cb) {
        Duration sinceLastDownload = PreferenceUtil.sinceLastDownload();

        if (sinceLastDownload.compareTo(OLD_DATA_DURATION) > 0) {
            download(cb);
        } else {
            reload(cb);
        }
    }

    @UiThread
    private void reload(DataCallback<List<ChannelWithItems>> cb) {
        callback = cb;
        if (isLoading) {
            callback.onLoading();
        } else if (channels != null) {
            callback.onData(channels, true);
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
            results(rssChannels, true);
        }
    }

    @UiThread
    private List<ChannelWithItems> results(List<ChannelWithItems> rssChannels, boolean finalResult) {
        channels = rssChannels;
        callback.onData(rssChannels, finalResult);
        return rssChannels;
    }

    @UiThread
    public void download(DataCallback<List<ChannelWithItems>> cb) {
        callback = cb;
        if (isLoading) {
            callback.onLoading();
        } else {
            isLoading = true;
            channels = null;
            callback.onLoading();
            PreferenceUtil.updateLastDownload();
            CompletableFuture.supplyAsync(database.dao()::getChannels, executor)
                    .thenApplyAsync(rssChannels -> results(rssChannels, false), new MainThreadExecutor())
                    .thenApplyAsync(channelsWithItems -> channelsWithItems.stream()
                            .map(this::prepareDownload)
                            .map(download -> CompletableFuture.supplyAsync(download, executor))
                            .map(CompletableFuture::join)
                            .flatMap(ch -> ch.items
                                    .stream())
                            .collect(Collectors.toList()), executor)
                    .thenApply(database.dao()::insertUpdateAndReturn)
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

//    @UiThread
//    public void addChannel(Uri url, DataCallback<ChannelWithItems> cb) {
//        cb.onLoading();
//        executor.submit(() -> {
//            if (!database.dao().channelExists(url)) {
//                callAddChannel(url, cb);
//            } else {
//                onMainThread(cb::onError);
//            }
//        });
//    }
//
//    @WorkerThread
//    private void callAddChannel(Uri uri, DataCallback<ChannelWithItems> cb) {
//        Client.call().rss(uri).enqueue(new Callback<ChannelWithItems>() {
//            @Override
//            public void onResponse(Call<ChannelWithItems> call, Response<ChannelWithItems> response) {
//                if (response.body() != null) {
//                    executor.submit(() -> {
//                        ChannelWithItems channel = database.dao().saveChannel(new RssChannel());
//                        onMainThread(() -> cb.onData(channel));
//                    });
//                } else {
//                    onMainThread(cb::onError);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChannelWithItems> call, Throwable t) {
//                callback.onError();
//            }
//        });
//    }
//
//    private static void onMainThread(@NonNull Runnable runnable) {
//        new MainThreadExecutor().execute(runnable);
//    }
}
