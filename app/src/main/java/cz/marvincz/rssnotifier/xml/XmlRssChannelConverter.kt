package cz.marvincz.rssnotifier.xml

import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.xmlpullparserconverter.FieldValueSetter
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition
import org.apache.commons.lang3.reflect.TypeLiteral

class XmlRssChannelConverter : XmlComplexConverter<RssChannel>({ RssChannel() }) {

    override fun getType() = object : TypeLiteral<RssChannel>() {}

    override fun getAttributes() = null

    override fun getTags() = arrayListOf(
                XmlFieldDefinition<RssChannel, String>("title", FieldValueSetter { ch, t -> ch.title = t }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<RssChannel, String>("description", FieldValueSetter { ch, d -> ch.description = d }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<RssChannel, String>("link", FieldValueSetter { ch, l -> ch.link = l }, object : TypeLiteral<String>() {})
    )
}
