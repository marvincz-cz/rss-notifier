package cz.marvincz.rssnotifier.room;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {ChannelEntity.class, ReadItem.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "data.db";

    public abstract Dao dao();

    /**
     * Do not call directly! Inject through Dagger
     *
     * @param context
     * @return Database instance
     */
    public static Database get(Context context) {
        // Dagger handles Singleton instantiation
        return create(context, true);
    }

    static Database create(Context context, boolean inMemory) {
        final Builder<Database> builder;
        if (inMemory) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), Database.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME);
        }

        return builder.addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                initData(context, db);
            }
        }).build();
    }

    private static void initData(Context context, SupportSQLiteDatabase db) {
        insert(db, "OOTS", "OOTS description", "http://www.giantitp.com/comics/oots.rss");
        insert(db, "Erfworld", "Erfworld description", "https://www.erfworld.com/rss");
        insert(db, "DnD", "DnD description", "http://www.darthsanddroids.net/rss2.xml");
    }

    private static void insert(SupportSQLiteDatabase db, String title, String description, String url) {
        ContentValues content = new ContentValues(3);
        content.put("title", title);
        content.put("description", description);
        content.put("url", url);
        db.insert("ChannelEntity", SQLiteDatabase.CONFLICT_REPLACE, content);
    }
}
