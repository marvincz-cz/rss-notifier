package cz.marvincz.rssnotifier.room

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem

@androidx.room.Dao
abstract class Dao {
    @Query("SELECT * FROM RssChannel")
    abstract fun getChannelsLive(): LiveData<List<RssChannel>>

    @Query("SELECT * FROM RssChannel")
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
        insertOrUpdate(items)
    }
}
