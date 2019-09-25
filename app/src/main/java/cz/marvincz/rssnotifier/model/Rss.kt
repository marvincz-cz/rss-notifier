package cz.marvincz.rssnotifier.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
data class Rss(@Element var channel: ChannelWithItems)