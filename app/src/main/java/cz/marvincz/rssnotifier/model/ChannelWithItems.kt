package cz.marvincz.rssnotifier.model

import androidx.room.Relation
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement

@XmlRootElement(name = "rss/channel")
data class ChannelWithItems(var channelUrl: String = "") {
    @Relation(parentColumn = "channelUrl", entityColumn = "channelUrl")
    lateinit var items: List<RssItem>

    @Relation(parentColumn = "channelUrl", entityColumn = "accessUrl")
    lateinit var channel: Set<RssChannel>

    fun fixedUrl(url: String): ChannelWithItems {
        channelUrl = url
        channel.first().accessUrl = url
        items.forEach { it.channelUrl = url }
        return this
    }
}