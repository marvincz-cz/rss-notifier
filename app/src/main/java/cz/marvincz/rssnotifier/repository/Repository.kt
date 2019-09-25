package cz.marvincz.rssnotifier.repository

import androidx.lifecycle.LiveData
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.retrofit.Client
import cz.marvincz.rssnotifier.room.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class Repository(private val database: Database) {

    fun getChannels(): LiveData<List<RssChannel>> = database.dao().getChannelsLive()

    fun getItems(channelUrl: String): LiveData<List<RssItem>> = database.dao().getItemsLive(channelUrl)

    suspend fun updateItem(item: RssItem) = database.dao().updateItem(item)

    suspend fun download(channelUrl: String) {
        withContext(Dispatchers.Default) {
            val channel = database.dao().getChannel(channelUrl)
            val oldItems = database.dao().getItems(channelUrl)
                    .associateBy { it.link }

            val rss = Client.call().rss(channelUrl)

            database.dao().insertOrUpdate(channel.copy(
                    link = rss.channel.link,
                    title = rss.channel.title,
                    description = rss.channel.description,
                    lastDownloaded = ZonedDateTime.now()
            ))

            database.dao().insertOrUpdate(rss.channel.items.map {
                it.copy(
                        channelUrl = channelUrl,
                        seen = oldItems[it.link]?.seen ?: false
                )
            })
        }
    }

}

private val OLD_DATA_DURATION = Duration.ofMinutes(30)
