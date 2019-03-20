package cz.marvincz.rssnotifier.repository

import android.util.Log
import androidx.annotation.AnyThread
import cz.marvincz.rssnotifier.RssApplication
import cz.marvincz.rssnotifier.model.ChannelWithItems
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
import javax.inject.Inject

class Repository {
    @Inject
    lateinit var database: Database

    private val executor: ExecutorService

    init {
        RssApplication.appComponent.inject(this)
        executor = Executors.newCachedThreadPool()
    }

    fun download(force: Boolean = false, onDone: (() -> Unit)? = null) {
        val sinceLastDownload = PreferenceUtil.sinceLastDownload()

        if (force || sinceLastDownload > OLD_DATA_DURATION) {
            doDownload(onDone)
        } else {
            onDone?.invoke()
        }
    }

    private fun doDownload(onDone: (() -> Unit)? = null) {
        PreferenceUtil.updateLastDownload()
        CompletableFuture.supplyAsync(Supplier { database.dao().getSlow() }, executor)
                .thenApply { channelsWithItems ->
                    channelsWithItems
                            .map { this.prepareDownload(it) }
                            .map { download -> CompletableFuture.supplyAsync<ChannelWithItems>(download, executor) }
                            .map { it.join() }
                            .flatMap { ch -> ch.items }
                }
                .thenAccept { database.dao().insertOrUpdate(it) }
                .whenCompleteAsync( BiConsumer { _, _ -> onDone?.invoke() }, MainThreadExecutor())
    }

    @AnyThread
    private fun prepareDownload(entity: ChannelWithItems) = Supplier<ChannelWithItems> {
        try {
            val url = entity.channelUrl
            Client.call().rss(url).execute().body()!!.fixedUrl(url)
        } catch (e: Exception) {
            Log.e("Repository", "Error downloading data", e)
            throw e
        }
    }

}
private val OLD_DATA_DURATION = Duration.ofMinutes(30)
