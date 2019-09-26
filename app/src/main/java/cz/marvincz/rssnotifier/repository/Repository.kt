package cz.marvincz.rssnotifier.repository

import androidx.lifecycle.LiveData
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.retrofit.Client
import cz.marvincz.rssnotifier.room.Database
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class Repository(private val database: Database) {

    fun getChannels(): LiveData<List<RssChannel>> = database.dao().getChannelsLive()

    fun getItems(channelUrl: String): LiveData<List<RssItem>> = database.dao().getItemsLive(channelUrl)

    suspend fun updateItem(item: RssItem) = database.dao().updateItem(item)

    suspend fun download(channelUrl: String, forced: Boolean) {
        withContext(Dispatchers.Default) {
            val channel = database.dao().getChannel(channelUrl)

            if (!forced && channel.isFresh())
                return@withContext // DB data is fresh enough

            val oldItems = database.dao().getItems(channelUrl)
                    .associateBy { it.link }

            val rss = runCatching { Client.call().rss(channelUrl) }
                    .getOrNull() ?: throw CancellationException()

            val newChannel = channel?.copy(
                    link = rss.channel.link,
                    title = rss.channel.title,
                    description = rss.channel.description,
                    lastDownloaded = ZonedDateTime.now()
            ) ?: RssChannel(
                    accessUrl = channelUrl,
                    link = rss.channel.link,
                    title = rss.channel.title,
                    description = rss.channel.description,
                    lastDownloaded = ZonedDateTime.now()
            )

            val newItems = rss.channel.items.map {
                it.copy(
                        channelUrl = channelUrl,
                        seen = oldItems[it.link]?.seen ?: false
                )
            }

            database.dao().insertOrUpdate(newChannel, newItems)
        }
    }

    suspend fun refreshAll() {
        withContext(Dispatchers.Default) {
            database.dao().getChannels()
                    .forEach {
                        launch {
                            download(it.accessUrl, false)
                        }
                    }
        }
    }
}

private val OLD_DATA_DURATION = Duration.ofMinutes(30)

private fun RssChannel?.isFresh() =
        this != null &&
                Duration.between(lastDownloaded, ZonedDateTime.now()) <= OLD_DATA_DURATION
