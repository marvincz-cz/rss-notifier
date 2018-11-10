package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import paperparcel.PaperParcel;

@Entity
@PaperParcel
public class RssChannel implements Parcelable {
    public static final Creator<RssChannel> CREATOR = PaperParcelRssChannel.CREATOR;

    @PrimaryKey
    @NonNull
    public Uri accessUrl;
    public Uri link;
    public String title;
    public String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PaperParcelRssChannel.writeToParcel(this, dest, flags); // (4)
    }
}
