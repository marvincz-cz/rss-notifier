package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        parentColumns = "accessUrl",
        childColumns = "channelUrl"),
        indices = @Index("channelUrl"))
@PaperParcel
public class RssItem implements Parcelable {
    public static final Creator<RssItem> CREATOR = PaperParcelRssItem.CREATOR;

    @PrimaryKey
    @NonNull
    public Uri link;
    public Uri channelUrl;
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

        return new EqualsBuilder()
                .append(link, rssItem.link)
                .append(channelUrl, rssItem.channelUrl)
                .append(title, rssItem.title)
                .append(description, rssItem.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(link)
                .append(channelUrl)
                .append(title)
                .append(description)
                .toHashCode();
    }
}
