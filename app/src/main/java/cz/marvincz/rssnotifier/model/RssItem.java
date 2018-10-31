package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import paperparcel.PaperParcel;

@Entity(foreignKeys = @ForeignKey(
        entity = RssChannel.class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = "id",
        childColumns = "channelId"),
        indices = @Index("channelId"))
@PaperParcel
public class RssItem implements Parcelable {
    public static final Creator<RssItem> CREATOR = PaperParcelRssItem.CREATOR;

    @PrimaryKey
    public int id;
    public int channelId;
    public String title;
    public String description;
    public Uri link;
    public boolean seen;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PaperParcelRssItem.writeToParcel(this, dest, flags); // (4)
    }
}
