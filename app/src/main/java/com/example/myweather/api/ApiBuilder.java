package com.example.myweather.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiBuilder {

    private static ApiService apiService;
    private final static String BASE_URL = "https://api.openweathermap.org";
    //public final static String APP_ID = "0943b0f280b4f20e1d9205ae003b3227";
    public final static String APP_ID = "d958fa2856e3c17c0eedcec1edc1561a";
    public final static String UNITS = "metric";

    public static ApiService getInstance(){
        if(apiService == null){

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
