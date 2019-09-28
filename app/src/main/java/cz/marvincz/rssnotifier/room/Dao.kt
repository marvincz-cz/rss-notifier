package cz.marvincz.rssnotifier.room

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem


@androidx.room.Dao
abstract class Dao {
    @Query("SELECT * FROM RssChannel ORDER BY sortOrder")
    abstract fun getChannelsLive(): LiveData<List<RssChannel>>

    @Query("SELECT * FROM RssChannel ORDER BY sortOrder")
    abstract suspend fun getChannels(): List<RssChannel>

    @Query("SELECT * FROM RssItem WHERE channelUrl = :channelUrl")
    abstract fun getItemsLive(channelUrl: String): LiveData<List<RssItem>>

    @Update
    abstract suspend fun updateItem(item: RssItem)

    @Query("SELECT * FROM RssChannel WHERE accessUrl = :channelUrl")
    abstract suspend fun getChannel(channelUrl: String): RssChannel?

    @Query("SELECT * FROM RssItem WHERE channelUrl = :channelUrl")
    abstract suspend fun getItems(channelUrl: String): List<RssItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertOrUpdate(items: Collection<RssItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertOrUpdate(channel: RssChannel)

    @Transaction
    open suspend fun insertOrUpdate(channel: RssChannel, items: Collection<RssItem>) {
        insertOrUpdate(channel)
        deleteChannelItems(channel.accessUrl)
        insertOrUpdate(items)
    }

    @Query("DELETE FROM RssItem WHERE channelUrl = :channelUrl")
    protected abstract fun deleteChannelItems(channelUrl: String)

    @Delete
    abstract fun deleteChannel(channel: RssChannel)

    @Query("SELECT MAX(sortOrder) FROM RssChannel")
    protected abstract suspend fun lastChannelOrder(): Int?

    /**
     * Order value for a new channel, so it is placed at the end
     */
    @Transaction
    open suspend fun newChannelOrder(): Int {
        return 1 + (lastChannelOrder() ?: 0)
    }

    @Update
    abstract suspend fun update(channels: Collection<RssChannel>)
}
