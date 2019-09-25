package cz.marvincz.rssnotifier.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
    abstract suspend fun insertOrUpdate(items: Collection<RssItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrUpdate(channel: RssChannel)
}
