package com.huce.hma.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.huce.hma.data.local.dao.SongDao;
import com.huce.hma.data.local.model.Song;

@Database(entities = {Song.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB INSTANCE;
    public abstract SongDao songDao();
    public static RoomDB getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), RoomDB.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}