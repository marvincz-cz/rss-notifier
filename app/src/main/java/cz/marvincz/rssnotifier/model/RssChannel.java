package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;
import paperparcel.PaperParcel;

@Entity
@XmlRootElement(name = "rss/channel")
@PaperParcel
public class RssChannel implements Parcelable {
    public static final Creator<RssChannel> CREATOR = PaperParcelRssChannel.CREATOR;

    @PrimaryKey
    @NonNull
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
