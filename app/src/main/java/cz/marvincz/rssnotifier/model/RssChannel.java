package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;
import paperparcel.PaperParcel;

@XmlRootElement(name = "rss/channel")
@PaperParcel
public class RssChannel implements Parcelable {
    public static final Creator<RssChannel> CREATOR = PaperParcelRssChannel.CREATOR;
    public String title;
    public String description;
    public Uri link;
    @XmlElement(name = "item")
    public List<RssItem> items;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        PaperParcelRssChannel.writeToParcel(this, dest, flags); // (4)
    }
}
