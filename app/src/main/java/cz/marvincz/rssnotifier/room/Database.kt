package cz.marvincz.rssnotifier.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem

@androidx.room.Database(entities = [RssChannel::class, RssItem::class], version = 1)
@TypeConverters(Converter::class)
abstract class Database : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        private const val DB_NAME = "data.db"

        /**
         * Do not call directly! Inject through Dagger
         *
         * @param context
         * @return Database instance
         */
        fun get(context: Context, inMemory: Boolean = false): Database = if (inMemory) {
            Room.inMemoryDatabaseBuilder(context.applicationContext, Database::class.java)
        } else {
            Room.databaseBuilder(context.applicationContext, Database::class.java, DB_NAME)
        }.build()

    }
}
