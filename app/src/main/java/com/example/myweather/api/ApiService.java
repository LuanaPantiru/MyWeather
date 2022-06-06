package com.example.myweather.api;

import com.example.myweather.model.CurrentDay;
import com.example.myweather.model.WeatherApiModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //https://api.openweathermap.org/data/2.5/forecast?q=Bucharest&units=metric&appid=d958fa2856e3c17c0eedcec1edc1561a

    @GET("data/2.5/weather")
    Call<CurrentDay> getCurrentDay(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("data/2.5/weather")
    Call<CurrentDay> getByCity(
            @Query("q") String city,
            @Query("units") String units,
            @Query("appid") String appid
    );

        @GET("data/2.5/forecast")
    Call<WeatherApiModel> getWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("data/2.5/forecast")
    Call<WeatherApiModel> getWeatherByCity(
            @Query("q") String city,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
