package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    public ZonedDateTime lastBuildDate;
    public List<RssItem> items;
}
