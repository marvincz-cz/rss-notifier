package cz.marvincz.rssnotifier.room;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = ChannelEntity.class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = "id",
        childColumns = "channelId"),
        indices = @Index("channelId"))
public class ReadItem {
    @PrimaryKey
    public int id;
    int channelId;

    public ReadItem(int id, int channelId) {
        this.id = id;
        this.channelId = channelId;
    }
}
