package cz.marvincz.rssnotifier.model.xml

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml(name = "item")
data class XmlItem(
        @PropertyElement val link: String?,
        @PropertyElement val guid: String?,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val title: String?,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val description: String?
) {
    fun getId(): String
        = guid ?: link ?: title ?: hashCode().toString()
}
