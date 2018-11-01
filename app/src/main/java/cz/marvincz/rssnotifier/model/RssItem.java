package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import paperparcel.PaperParcel;

@Entity(foreignKeys = @ForeignKey(
        entity = RssChannel.class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = "link",
        childColumns = "channelLink"),
        indices = @Index("channelLink"))
@PaperParcel
public class RssItem implements Parcelable {
    public static final Creator<RssItem> CREATOR = PaperParcelRssItem.CREATOR;

    @PrimaryKey
    @NonNull
    public Uri link;
    public Uri channelLink;
    public String title;
    public String description;
    public boolean seen;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PaperParcelRssItem.writeToParcel(this, dest, flags); // (4)
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssItem rssItem = (RssItem) o;
        return channelLink == rssItem.channelLink &&
                Objects.equals(link, rssItem.link) &&
                Objects.equals(title, rssItem.title) &&
                Objects.equals(description, rssItem.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(link, channelLink, title, description);
    }
}
