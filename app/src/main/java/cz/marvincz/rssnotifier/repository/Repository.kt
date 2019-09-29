package cz.marvincz.rssnotifier.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.retrofit.Client
import cz.marvincz.rssnotifier.room.Database
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class Repository(private val database: Database) {

    fun getChannels(): LiveData<List<RssChannel>> = database.dao().getChannelsLive()

    fun getChannelsOneTime() = liveData { emit(database.dao().getChannels()) }

    fun getItems(channelUrl: String, showSeen: Boolean): LiveData<List<RssItem>> = database.dao().getItemsLive(channelUrl, showSeen)

    suspend fun updateItem(item: RssItem) = database.dao().updateItem(item)

    /**
     * Queries the RSS feed at [channelUrl]. If new items are found on the server, they are saved to the database and also returned.
     * If the [channelUrl] does not correspond to any currently subscribed [RssChannel], it is also saved to the database.
     *
     * @param channelUrl URL to be queried, also corresponding to [RssChannel.accessUrl]
     * @param forced If *false*, respects default cool-off period and returns cached data if sufficiently fresh. If *true*, server is queried irregardless of cache freshness.
     * @return New [items][RssItem] mapped to their respective [channels][RssChannel]
     */
    suspend fun download(channelUrl: String, forced: Boolean): List<RssItem> =
            withContext(Dispatchers.Default) {
                val channel = database.dao().getChannel(channelUrl)

                if (!forced && channel.isFresh())
                    return@withContext emptyList<RssItem>() // DB data is fresh enough

                val oldItems = database.dao().getItems(channelUrl)
                        .associateBy { it.id }

                val rss = runCatching { Client.call().rss(channelUrl) }
                        .getOrNull() ?: throw CancellationException()

                val newChannel = channel?.copy(
                        link = rss.channel.link,
                        title = rss.channel.title ?: channel.title ?: channelUrl,
                        description = rss.channel.description,
                        lastDownloaded = ZonedDateTime.now()
                ) ?: RssChannel(
                        accessUrl = channelUrl,
                        link = rss.channel.link,
                        title = rss.channel.title ?: channelUrl,
                        description = rss.channel.description,
                        sortOrder = database.dao().newChannelOrder(),
                        lastDownloaded = ZonedDateTime.now()
                )

                val newItems = rss.channel.items.map {
                    val id = it.getId()
                    RssItem(
                            id = id,
                            link = it.link,
                            channelUrl = channelUrl,
                            title = it.title,
                            description = it.description,
                            seen = oldItems[id]?.seen ?: false
                    )
                }

                database.dao().insertOrUpdate(newChannel, newItems)

                return@withContext newItems.filterNot { it.id in oldItems }
            }

    /**
     * Refreshes all subscribed [channels][RssChannel]. If new [items][RssItem] are found on the server, they are saved to the database and also returned.
     *
     * @param forced If *false*, respects default cool-off period and returns cached data if sufficiently fresh. If *true*, server is queried irregardless of cache freshness.
     * @return New [items][RssItem] mapped to their respective [channels][RssChannel]
     */
    suspend fun refreshAll(forced: Boolean = false) =
            withContext(Dispatchers.Default) {
                database.dao().getChannels()
                        .map { it to async { download(it.accessUrl, forced) } }
                        .map { (channel, job) -> channel to job.await() }
                        .toMap()
            }

    suspend fun deleteChannel(channel: RssChannel) {
        database.dao().deleteChannel(channel)
    }

    suspend fun saveChannelOrder(channels: List<RssChannel>) {
        database.dao().update(channels.mapIndexed { i, channel ->
            channel.copy(sortOrder = i + 1)
        })
    }

    suspend fun markAllRead(channelUrl: String) {
        database.dao().markAllRead(channelUrl)
    }

    suspend fun markRead(itemId: String) {
        database.dao().markRead(itemId)
    }
}

private val OLD_DATA_DURATION = Duration.ofMinutes(30)

private fun RssChannel?.isFresh() =
        this != null &&
                Duration.between(lastDownloaded, ZonedDateTime.now()) <= OLD_DATA_DURATION
