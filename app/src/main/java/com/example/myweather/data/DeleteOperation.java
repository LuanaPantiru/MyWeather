package com.example.myweather.data;

import android.os.AsyncTask;

public class DeleteOperation extends AsyncTask<String, Object, String> {

    LocationRepository listener;

    public DeleteOperation(LocationRepository listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... cityName) {
        try{
            MyApp.getAppDatabase().locationDao().delete(cityName[0]);
        }catch (Exception e){
            return "Error delete";
        }
        return "Success delete";
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String s) {
        listener.delete(s);
    }
}
