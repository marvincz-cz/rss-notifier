package cz.marvincz.rssnotifier.room;

import android.net.Uri;

import java.util.Collection;
import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Query("SELECT * FROM RssChannel WHERE RssChannel.accessUrl = :url")
    @Transaction
    abstract ChannelWithItems getChannelWithItems(Uri url);

    @Query("SELECT 1 FROM RssChannel WHERE RssChannel.accessUrl = :url")
    abstract Integer getChannelExists(Uri url);

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertOrUpdate(Collection<RssItem> channels);

    @Transaction
    public ChannelWithItems saveChannel(RssChannel channel) {
        insertChannel(channel);
        return getChannelWithItems(channel.link);
    }

    @Transaction
    public boolean channelExists(Uri url) {
        return getChannelExists(url) != null;
    }

    @Transaction
    public List<ChannelWithItems> insertUpdateAndReturn(Collection<RssItem> items) {
        insertOrUpdate(items);
        return getChannels();
    }
}
