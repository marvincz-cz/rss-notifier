package cz.marvincz.rssnotifier.room

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import org.threeten.bp.ZonedDateTime

@androidx.room.Database(entities = [RssChannel::class, RssItem::class], version = 1)
@TypeConverters(Converter::class)
abstract class Database : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        private val DB_NAME = "data.db"

        /**
         * Do not call directly! Inject through Dagger
         *
         * @param context
         * @return Database instance
         */
        fun get(context: Context): Database {
            // Dagger handles Singleton instantiation
            return create(context, true)
        }

        private fun create(context: Context, inMemory: Boolean): Database {
            val builder = if (inMemory) {
                Room.inMemoryDatabaseBuilder(context.applicationContext, Database::class.java)
            } else {
                Room.databaseBuilder(context.applicationContext, Database::class.java, DB_NAME)
            }

            return builder.addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    initData(db)
                }
            }).build()
        }

        private fun initData(db: SupportSQLiteDatabase) {
            val date = Converter().dateToLong(ZonedDateTime.now().minusDays(1))
            insert(db, "OOTS", "OOTS description", "http://www.giantitp.com/comics/oots.rss", "http://www.giantitp.com/Comics.html", date)
            insert(db, "Erfworld", "Erfworld description", "https://www.erfworld.com/rss", "todo", date)
            insert(db, "DnD", "DnD description", "http://www.darthsanddroids.net/rss2.xml", "todo", date)
        }

        private fun insert(db: SupportSQLiteDatabase, title: String, description: String, accessUrl: String, link: String, date: Long) {
            val content = ContentValues(4)
            content.put("title", title)
            content.put("description", description)
            content.put("link", link)
            content.put("accessUrl", accessUrl)
            content.put("lastDownloaded", date)
            db.insert("RssChannel", SQLiteDatabase.CONFLICT_REPLACE, content)
        }
    }
}
