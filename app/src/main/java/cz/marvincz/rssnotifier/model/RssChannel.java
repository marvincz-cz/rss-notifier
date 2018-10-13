package cz.marvincz.rssnotifier.model;

import android.net.Uri;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.marvincz.rssnotifier.room.ChannelWithItems;
import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;

@XmlRootElement(name = "rss/channel")
public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    @XmlElement(name = "item")
    public List<RssItem> items;

    public void markRead(List<ChannelWithItems> channelsWithItems) {
        String url = link.toString();
        Set<Integer> readItems = channelsWithItems.stream()
                .filter(ch -> ch.url.equals(url))
                .flatMap(ch -> ch.readItems.stream())
                .map(item -> item.id)
                .collect(Collectors.toSet());

        for (RssItem item : items) {
            if (readItems.contains(item.getId())) {
                item.seen = true;
            }
        }
    }
}
