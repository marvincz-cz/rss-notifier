package cz.marvincz.rssnotifier.model.xml

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
data class XmlRss(
        @Element var channel: XmlChannel
)