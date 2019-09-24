package cz.marvincz.rssnotifier.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RssChannel(
    @PrimaryKey
    var accessUrl: String = "",
    var link: String? = null,
    var title: String? = null,
    var description: String? = null
)