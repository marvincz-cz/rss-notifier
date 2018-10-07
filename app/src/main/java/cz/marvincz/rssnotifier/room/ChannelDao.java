package cz.marvincz.rssnotifier.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface ChannelDao {
    @Query("SELECT * FROM ChannelEntity")
    @Transaction
    List<ChannelWithItems> getChannels();

    @Insert
    void saveChannel(ChannelEntity channel);

    @Delete
    @Transaction
    void deleteChannel(ChannelEntity channel);

    @Insert
    void saveReadItem(ReadItem... readItems);

    @Delete
    void deleteReadItem(ReadItem... readItems);
}
