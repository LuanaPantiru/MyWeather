package com.example.myweather.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class GetAllOperation extends AsyncTask<Object, Object, List<Location>> {

    LocationRepository listener;

    public GetAllOperation(LocationRepository listener){
        this.listener = listener;
    }
    @Override
    protected List<Location> doInBackground(Object[] objects) {
        List<Location> locations = new ArrayList<>();
        try{
            locations = MyApp.getAppDatabase().locationDao().getAll();
        }catch (Exception e){
            return locations;
        }
        return locations;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Location> result) {
        listener.getAll(result);
    }
}
