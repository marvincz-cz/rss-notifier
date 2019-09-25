package cz.marvincz.rssnotifier.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.ZonedDateTime

@Entity
data class RssChannel(
    @PrimaryKey
    val accessUrl: String = "",
    val link: String? = null,
    val title: String? = null,
    val description: String? = null,
    val lastDownloaded: ZonedDateTime = ZonedDateTime.now()
)