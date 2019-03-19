package cz.marvincz.rssnotifier.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class RssChannel(
    @PrimaryKey
    var accessUrl: String = "",
    var link: String? = null,
    var title: String? = null,
    var description: String? = null
): Parcelable