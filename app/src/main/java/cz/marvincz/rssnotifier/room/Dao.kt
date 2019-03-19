package cz.marvincz.rssnotifier.room

import android.net.Uri
import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cz.marvincz.rssnotifier.model.ChannelWithItems
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem

@androidx.room.Dao
abstract class Dao {
    @Query("SELECT * FROM RssChannel")
    @AnyThread
    @Transaction
    abstract fun getChannels(): LiveData<List<RssChannel>>

    @Query("SELECT accessUrl AS channelUrl FROM RssChannel")
    @WorkerThread
    @Transaction
    abstract fun getSlow(): List<ChannelWithItems>

    @Query("SELECT * FROM RssItem WHERE channelUrl = :channelUrl")
    abstract fun getItems(channelUrl: String): LiveData<List<RssItem>>

    @Query("SELECT 1 FROM RssChannel WHERE RssChannel.accessUrl = :url")
    abstract fun getChannelExists(url: String): Int?

    @Insert
    abstract fun insertChannel(channel: RssChannel)

    @Delete
    abstract fun deleteChannel(channel: RssChannel)

    @Insert
    abstract fun insertItems(vararg items: RssItem)

    @Insert
    abstract fun insertItems(items: Collection<RssItem>)

    @Update
    abstract fun updateItems(vararg items: RssItem)

    @Update
    abstract fun updateItems(items: Collection<RssItem>)

    @Delete
    abstract fun deleteItems(items: Collection<RssItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrUpdate(items: Collection<RssItem>)

    @Transaction
    open fun channelExists(url: String) = getChannelExists(url) != null

    @Transaction
    open fun insertChannel(channel: ChannelWithItems) {
        insertChannel(channel.channel.first())
        insertItems(channel.items)
    }
}
