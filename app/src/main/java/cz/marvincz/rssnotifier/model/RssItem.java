package cz.marvincz.rssnotifier.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import paperparcel.PaperParcel;

@PaperParcel
public class RssItem implements Parcelable {
    public static final Creator<RssItem> CREATOR = PaperParcelRssItem.CREATOR;
    public String title;
    public String description;
    public Uri link;
    public transient boolean seen;

    /**
     * Based on {@link String#toString}
     * @return id based on link hash
     */
    public int getId() {
        String s = link.toString();
        int hash = 0;
        final int len = s.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                hash = 31 * hash + s.charAt(i);
            }
        }
        return hash;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        PaperParcelRssItem.writeToParcel(this, dest, flags); // (4)
    }
}
