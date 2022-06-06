package com.example.myweather.model;

import java.util.List;

public class WeatherApiModel {
    private String cnt;
    private List<MomentOfDay> list;
    private City city;

    public WeatherApiModel(String cnt, List<MomentOfDay> list, City city) {
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }

    public String getCnt() {
        return cnt;
    }

    public List<MomentOfDay> getList() {
        return list;
    }

    public City getCity() {
        return city;
    }
}
