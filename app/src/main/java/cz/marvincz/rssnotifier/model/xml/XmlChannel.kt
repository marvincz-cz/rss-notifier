package cz.marvincz.rssnotifier.model.xml

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml(name = "channel")
data class XmlChannel(
        @Element val items: List<XmlItem>,
        @PropertyElement val link: String?,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val title: String?,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val description: String?
)