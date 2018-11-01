package cz.marvincz.rssnotifier.room;

import android.net.Uri;

import java.util.Collection;
import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;

@androidx.room.Dao
public abstract class Dao {
    @Query("SELECT * FROM RssChannel")
    @Transaction
    public abstract List<ChannelWithItems> getChannels();

    @Query("SELECT * FROM RssChannel WHERE RssChannel.link = :link")
    @Transaction
    abstract ChannelWithItems getChannelWithItems(Uri link);

    @Query("SELECT 1 FROM RssChannel WHERE RssChannel.link = :link")
    abstract Integer getChannelExists(Uri link);

    @Insert
    public abstract void insertChannel(RssChannel channel);

    @Delete
    @Transaction
    public abstract void deleteChannel(RssChannel channel);

    @Insert
    public abstract void insertItems(RssItem... items);

    @Insert
    public abstract void insertItems(Collection<RssItem> items);

    @Update
    public abstract void updateItems(RssItem... items);

    @Update
    public abstract void updateItems(Collection<RssItem> items);

    @Delete
    public abstract void deleteItems(RssItem... items);

    @Delete
    public abstract void deleteItems(Collection<RssItem> items);

    @Transaction
    public ChannelWithItems saveChannel(RssChannel channel) {
        insertChannel(channel);
        return getChannelWithItems(channel.link);
    }

    @Transaction
    public boolean channelExists(Uri link) {
        return getChannelExists(link) != null;
    }

    @Transaction
    public void deleteAndInsertItems(List<RssItem> toDelete, List<RssItem> toAdd) {
        deleteItems(toDelete);
        insertItems(toAdd);
    }
}
