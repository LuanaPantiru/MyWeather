package com.example.myweather.data;

import android.os.AsyncTask;

public class UpdateOperation extends AsyncTask<Location, Object, String> {

    LocationRepository listener;

    public UpdateOperation(LocationRepository listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        try{
            MyApp.getAppDatabase().locationDao().updateLocation(locations);
        }catch (Exception e){
            return "Error update";
        }
        return "Success update";
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        listener.update(s);
    }
}
