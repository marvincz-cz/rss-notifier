package cz.marvincz.rssnotifier.model;

import android.net.Uri;

public class RssItem {
    public String title;
    public String description;
    public Uri link;
    public transient boolean seen;

    public int getId() {
        return link.hashCode();
    }
}
