package cz.marvincz.rssnotifier.model;

import android.net.Uri;

import java.util.List;

import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;

@XmlRootElement(name = "rss/channel")
public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    @XmlElement(name = "item")
    public List<RssItem> items;
}
