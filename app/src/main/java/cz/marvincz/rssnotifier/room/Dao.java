package cz.marvincz.rssnotifier.room;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@androidx.room.Dao
public abstract class Dao {
    @Query("SELECT * FROM ChannelEntity")
    @Transaction
    public abstract List<ChannelWithItems> getChannels();

    @Query("SELECT * FROM ChannelEntity WHERE channelEntity.id = :id")
    @Transaction
    abstract ChannelWithItems getChannel(long id);

    @Query("SELECT * FROM ChannelEntity WHERE channelEntity.url = :url")
    abstract ChannelEntity getChannel(String url);

    @Insert
    abstract long insertChannel(ChannelEntity channel);

    @Delete
    @Transaction
    public abstract void deleteChannel(ChannelEntity channel);

    @Insert
    public abstract void saveReadItem(ReadItem... readItems);

    @Delete
    public abstract void deleteReadItem(ReadItem... readItems);

    @Transaction
    public ChannelWithItems saveChannel(ChannelEntity channel) {
        long id = insertChannel(channel);
        return getChannel(id);
    }

    @Transaction
    public boolean channelExists(String url) {
        return getChannel(url) != null;
    }
}
