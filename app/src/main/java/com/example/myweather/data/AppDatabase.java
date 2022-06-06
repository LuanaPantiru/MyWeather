package com.example.myweather.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Location.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
