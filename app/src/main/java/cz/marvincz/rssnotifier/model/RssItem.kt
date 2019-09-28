package cz.marvincz.rssnotifier.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = RssChannel::class, onDelete = ForeignKey.CASCADE, parentColumns = ["accessUrl"], childColumns = ["channelUrl"])], indices = [Index("channelUrl")])
data class RssItem(
        @PrimaryKey val id: String,
        val link: String?,
        val channelUrl: String?,
        val title: String?,
        val description: String?,
        val seen: Boolean = false
)
