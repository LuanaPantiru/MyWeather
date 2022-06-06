package com.example.myweather.model;

public class Caracteristics {
    private String temp;
    private String feels_like;
    private String temp_min;
    private String temp_max;
    private String pressure;
    private String humidity;

    public Caracteristics(String temp, String feels_like, String temp_min, String temp_max, String pressure, String humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getTemp() {
        return temp;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

}
