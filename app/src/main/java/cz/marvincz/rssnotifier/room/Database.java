package cz.marvincz.rssnotifier.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {ChannelEntity.class, ReadItem.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "data.db";

    public abstract ChannelDao dao();

    /**
     * Do not call directly! Inject through Dagger
     *
     * @param context
     * @return Database instance
     */
    public static Database get(Context context) {
        // Dagger handles Singleton instantiation
        return create(context, false);
    }

    static Database create(Context context, boolean inMemory) {
        final Builder<Database> builder;
        if (inMemory) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), Database.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME);
        }

        return builder.build();
    }
}
