package cz.marvincz.rssnotifier.room;

import java.util.Set;

import androidx.room.Relation;

public class ChannelWithItems extends ChannelEntity {
    ChannelWithItems() {
        super();
    }

    @Relation(parentColumn = "id", entityColumn = "channelId")
    public Set<ReadItem> readItems;
}
