package cz.marvincz.rssnotifier.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml(name = "item")
@Entity(foreignKeys = [ForeignKey(entity = RssChannel::class, onDelete = ForeignKey.CASCADE, parentColumns = ["accessUrl"], childColumns = ["channelUrl"])], indices = [Index("channelUrl")])
data class RssItem(
        @PrimaryKey
        @PropertyElement val link: String = "",
        @PropertyElement val channelUrl: String? = null,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val title: String? = null,
        @PropertyElement(converter = HtmlEscapeStringConverter::class) val description: String? = null,
        @PropertyElement val seen: Boolean = false
)
