package cz.marvincz.rssnotifier.model

import android.os.Parcelable
import androidx.room.Relation
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement
import kotlinx.android.parcel.Parcelize

@Parcelize
@XmlRootElement(name = "rss/channel")
data class ChannelWithItems(var channelUrl: String = ""): Parcelable {
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