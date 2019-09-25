package cz.marvincz.rssnotifier.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "channel")
data class ChannelWithItems(
        @Element val items: List<RssItem> = emptyList(),
        @PropertyElement val link: String? = null,
        @PropertyElement val title: String? = null,
        @PropertyElement val description: String? = null
)