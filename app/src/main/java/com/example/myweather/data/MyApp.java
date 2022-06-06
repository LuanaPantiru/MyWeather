package com.example.myweather.data;

import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {
    private static MyApp mInstance;
    private static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "weather-database"
        ).build();
    }

    public static MyApp getInstance(){
        return mInstance;
    }
    public static AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
