package com.example.myweather.model;

public class DayOfWeek {
    private String dayName;
    private String minTemp;
    private String maxTemp;

    public DayOfWeek(String dayName, String minTemp, String maxTemp) {
        this.dayName = dayName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getDayName() {
        return dayName;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }
}
