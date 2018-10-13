package cz.marvincz.rssnotifier.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import cz.marvincz.rssnotifier.model.RssChannel;

@Entity(indices = @Index(value = "url", unique = true))
public class ChannelEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String url;

    @Ignore
    public ChannelEntity(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    @Ignore
    public ChannelEntity(RssChannel rssChannel) {
        this(rssChannel.title, rssChannel.description, rssChannel.link.toString());
    }

    ChannelEntity() {
    }
}
