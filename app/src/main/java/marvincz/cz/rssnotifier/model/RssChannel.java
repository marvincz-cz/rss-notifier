package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import java.util.List;

public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    public List<RssItem> item;
}
