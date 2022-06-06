package com.example.myweather.data;

import android.os.AsyncTask;

public class FindByCityOperation extends AsyncTask<String, Object, Location> {

    LocationRepository listener;

    public FindByCityOperation(LocationRepository listener) {
        this.listener = listener;
    }

    @Override
    protected Location doInBackground(String... city) {
        try{
            Location loc = MyApp.getAppDatabase().locationDao().findByCity(city[0]);
            return loc;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Location location) {
        listener.findByCity(location);
    }
}
