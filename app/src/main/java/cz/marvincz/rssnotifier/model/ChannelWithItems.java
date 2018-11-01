package cz.marvincz.rssnotifier.model;

import java.util.List;
import java.util.Set;

import androidx.room.Relation;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;
import paperparcel.PaperParcel;

public class ChannelWithItems extends RssChannel {
    @Relation(parentColumn = "link", entityColumn = "channelLink")
    @XmlElement(name = "item")
    public List<RssItem> items;
}
