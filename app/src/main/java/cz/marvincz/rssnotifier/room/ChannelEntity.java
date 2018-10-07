package cz.marvincz.rssnotifier.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ChannelEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String channelUrl;

    @Ignore
    public ChannelEntity(String title, String description, String channelUrl) {
        this.title = title;
        this.description = description;
        this.channelUrl = channelUrl;
    }

    ChannelEntity(int id, String title, String description, String channelUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.channelUrl = channelUrl;
    }
}
