package com.example.myweather.model;

import java.util.List;

public class MomentOfDay {
    private String dt;
    private Caracteristics main;
    private List<Weather> weather;
    private Wind wind;
    private String visibility;
    private String dt_txt;

    public MomentOfDay(String dt, Caracteristics main, List<Weather> weather, Wind wind, String visibility, String dt_txt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.wind = wind;
        this.visibility = visibility;
        this.dt_txt = dt_txt;
    }

    public String getDt() {
        return dt;
    }

    public Caracteristics getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}
