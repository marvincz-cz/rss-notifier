package cz.marvincz.rssnotifier.repository

import android.util.Log
import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import cz.marvincz.rssnotifier.model.ChannelWithItems
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.retrofit.Client
import cz.marvincz.rssnotifier.room.Database
import cz.marvincz.rssnotifier.util.MainThreadExecutor
import cz.marvincz.rssnotifier.util.PreferenceUtil
import org.threeten.bp.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.BiConsumer
import java.util.function.Supplier

class Repository(private val database: Database) {

    private val executor: ExecutorService = Executors.newCachedThreadPool()

    @UiThread
    fun download(force: Boolean = false, onDone: (() -> Unit)? = null) {
        val sinceLastDownload = PreferenceUtil.sinceLastDownload()

        if (force || sinceLastDownload > OLD_DATA_DURATION) {
            doDownload(onDone)
        } else {
            onDone?.invoke()
        }
    }

    @UiThread
    private fun doDownload(onDone: (() -> Unit)? = null) {
        PreferenceUtil.updateLastDownload()
        CompletableFuture.supplyAsync(Supplier { database.dao().getSlow() }, executor)
                .thenApply { channelsWithItems ->
                    channelsWithItems
                            .map { this.prepareDownload(it.channelUrl) }
                            .map { download -> CompletableFuture.supplyAsync<ChannelWithItems>(download, executor) }
                            .map { it.join() }
                            .flatMap { ch -> ch.items }
                }
                .thenAccept { database.dao().insertOrUpdate(it) }
                .whenCompleteAsync( BiConsumer { _, _ -> onDone?.invoke() }, MainThreadExecutor())
    }

    @UiThread
    fun addChannel(url: String, onDone: (() -> Unit)? = null) {
        CompletableFuture.supplyAsync(Supplier { database.dao().channelExists(url) }, executor)
                .thenAccept { exists -> if (exists) throw IllegalArgumentException() }
                .thenApply { prepareDownload(url).get() }
                .thenAccept { database.dao().insertChannel(it) }
                .whenCompleteAsync( BiConsumer { _, _ -> onDone?.invoke() }, MainThreadExecutor())
    }

    @AnyThread
    private fun prepareDownload(url: String) = Supplier<ChannelWithItems> {
        try {
            Client.call().rss(url).execute().body()!!.fixedUrl(url)
        } catch (e: Exception) {
            Log.e("Repository", "Error downloading data", e)
            throw e
        }
    }

    @UiThread
    fun updateItem(item: RssItem) {
        CompletableFuture.runAsync(Runnable { database.dao().updateItem(item) }, executor)
    }

}
private val OLD_DATA_DURATION = Duration.ofMinutes(30)
