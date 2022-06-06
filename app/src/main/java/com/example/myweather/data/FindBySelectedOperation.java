package com.example.myweather.data;

import android.os.AsyncTask;

public class FindBySelectedOperation extends AsyncTask<Boolean, Object, Location> {
    LocationRepository listener;

    public FindBySelectedOperation(LocationRepository listener) {
        this.listener = listener;
    }

    @Override
    protected Location doInBackground(Boolean... city) {
        try{
            Location loc = MyApp.getAppDatabase().locationDao().findBySelected(city[0]);
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
        listener.findBySelected(location);
    }
}
