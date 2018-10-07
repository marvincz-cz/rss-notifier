package cz.marvincz.rssnotifier.room;

import java.util.Set;

import androidx.room.Relation;

public class ChannelWithItems extends ChannelEntity {
    ChannelWithItems(int id, String title, String description, String channelUrl) {
        super(id, title, description, channelUrl);
    }

    @Relation(parentColumn = "id", entityColumn = "channelId")
    public Set<ReadItem> readItems;
}
