package cz.marvincz.rssnotifier.room;

import android.net.Uri;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;

@androidx.room.Dao
public abstract class Dao {
    @Query("SELECT * FROM RssChannel")
    @Transaction
    public abstract List<ChannelWithItems> getChannels();

    @Query("SELECT * FROM RssChannel WHERE RssChannel.id = :id")
    @Transaction
    abstract ChannelWithItems getChannel(long id);

    @Query("SELECT * FROM RssChannel WHERE RssChannel.link = :link")
    abstract RssChannel getChannel(Uri link);

    @Insert
    abstract long insertChannel(RssChannel channel);

    @Delete
    @Transaction
    public abstract void deleteChannel(RssChannel channel);

    @Insert
    public abstract void saveRssItem(RssItem... RssItems);

    @Delete
    public abstract void deleteRssItem(RssItem... RssItems);

    @Transaction
    public ChannelWithItems saveChannel(RssChannel channel) {
        long id = insertChannel(channel);
        return getChannel(id);
    }

    @Transaction
    public boolean channelExists(Uri link) {
        return getChannel(link) != null;
    }
}
