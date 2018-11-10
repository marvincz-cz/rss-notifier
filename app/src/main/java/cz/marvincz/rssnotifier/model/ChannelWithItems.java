package cz.marvincz.rssnotifier.model;

import android.net.Uri;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import androidx.room.Relation;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;
import paperparcel.PaperParcel;

@XmlRootElement(name = "rss/channel")
public class ChannelWithItems extends RssChannel {
    @Relation(parentColumn = "accessUrl", entityColumn = "channelUrl")
    @XmlElement(name = "item")
    public List<RssItem> items;

    public ChannelWithItems fixUrl(Uri url) {
        accessUrl = Objects.requireNonNull(url);
        Optional.ofNullable(items)
                .ifPresent(list -> list.forEach(i -> i.channelUrl = url));
        return this;
    }
}
