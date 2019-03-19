package cz.marvincz.rssnotifier.xml

import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.xmlpullparserconverter.FieldValueSetter
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition
import org.apache.commons.lang3.reflect.TypeLiteral
import org.apache.commons.lang3.reflect.Typed
import java.util.*

class XmlRssItemConverter : XmlComplexConverter<RssItem>({ t -> RssItem() }) {

    override fun getAttributes() = null

    override fun getTags() = arrayListOf(
                XmlFieldDefinition<RssItem, String>("title", FieldValueSetter { i, t -> i.title = t }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<RssItem, String>("description", FieldValueSetter { i, d -> i.description = d }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<RssItem, String>("link", FieldValueSetter { i, l -> i.link = l!! }, object : TypeLiteral<String>() {})
    )

    override fun getType() = object : TypeLiteral<RssItem>() {}
}
