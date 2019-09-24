package cz.marvincz.rssnotifier.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = RssChannel::class, onDelete = ForeignKey.CASCADE, parentColumns = ["accessUrl"], childColumns = ["channelUrl"])], indices = [Index("channelUrl")])
data class RssItem(
    @PrimaryKey
    var link: String = "",
    var channelUrl: String? = null,
    var title: String? = null,
    var description: String? = null,
    var seen: Boolean = false
)
