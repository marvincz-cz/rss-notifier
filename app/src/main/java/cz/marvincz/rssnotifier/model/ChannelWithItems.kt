package cz.marvincz.rssnotifier.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml(name = "channel")
data class ChannelWithItems(
        @Element val items: List<RssItem> = emptyList(),
        @PropertyElement val link: String? = null,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val title: String? = null,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val description: String? = null
)