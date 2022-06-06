package com.example.myweather.data;

import android.os.AsyncTask;

public class AddOperation extends AsyncTask<Location, Object, String> {
    LocationRepository listener;

    public AddOperation(LocationRepository listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        try{
            MyApp.getAppDatabase().locationDao().insertAll(locations);
        }catch (Exception e){
            return "Error insert";
        }
        return "Success insert";
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        listener.add(s);
    }
}
