package cz.marvincz.rssnotifier.xml

import cz.marvincz.rssnotifier.model.ChannelWithItems
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.xmlpullparserconverter.FieldValueSetter
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition
import org.apache.commons.lang3.reflect.TypeLiteral

class XmlChannelWithItemsConverter : XmlComplexConverter<ChannelWithItems>({ initChannel() }) {

    override fun getType() = object : TypeLiteral<ChannelWithItems>() {}

    override fun getAttributes() = null

    override fun getTags() = arrayListOf(
                XmlFieldDefinition<ChannelWithItems, String>("title", FieldValueSetter { ch, t -> ch.channel.first().title = t }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<ChannelWithItems, String>("description", FieldValueSetter { ch, d -> ch.channel.first().description = d }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<ChannelWithItems, String>("link", FieldValueSetter { ch, l -> ch.channel.first().link = l }, object : TypeLiteral<String>() {}),
                XmlFieldDefinition<ChannelWithItems, List<RssItem>>("item", FieldValueSetter { ch, i -> ch.items = i.orEmpty() }, object : TypeLiteral<List<RssItem>>() {})
    )
}

private fun initChannel(): ChannelWithItems {
    val channelWithItems = ChannelWithItems()
    channelWithItems.channel = setOf(RssChannel())
    return channelWithItems
}
