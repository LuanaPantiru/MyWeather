package com.example.myweather.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;
import com.example.myweather.adapter.DayOfWeekAdapter;
import com.example.myweather.api.ApiBuilder;
import com.example.myweather.data.FindBySelectedOperation;
import com.example.myweather.data.Location;
import com.example.myweather.data.LocationRepository;
import com.example.myweather.model.DayOfWeek;
import com.example.myweather.model.MomentOfDay;
import com.example.myweather.model.WeatherApiModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HomePage extends Fragment implements LocationRepository {
    public Button buttonSeeMore;
    public TextView city;
    public TextView time;
    public TextView temp;
    public ImageView img;
    private List<MomentOfDay> weather = new ArrayList<>();
    private String cityLocation;
    public static List<DayOfWeek> weekList = new ArrayList<>();
    public DayOfWeekAdapter adapter;
    private final Bundle bundleSeeMore = new Bundle();
    Bundle bundle;


    public HomePage() {
        super(R.layout.home_page);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater,container,savedInstanceState);
        bundle = getArguments();

        time = v.findViewById(R.id.Time);
        temp = v.findViewById(R.id.Temperature);
        img = v.findViewById(R.id.imgIcon);
        city = v.findViewById(R.id.City);

        new FindBySelectedOperation(this).execute(true);



        buttonSeeMore = v.findViewById(R.id.See_more);
        buttonSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                Fragment frg = new SeeMore();
                frg.setArguments(bundleSeeMore);
                fragmentTransaction.replace(R.id.home_page,frg);
                fragmentTransaction.commit();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.recycle_view_resume_week);

        adapter = new DayOfWeekAdapter(weekList);
        rv.setAdapter(adapter);
    }

    protected MomentOfDay getMomentOfDay(String currentHour){
        int h = roundHour(currentHour);
        MomentOfDay md1 = weather.get(0);
        int md1Time = Integer.parseInt(md1.getDt_txt().substring(md1.getDt_txt().length()-8).substring(0,2));
        MomentOfDay md2 = weather.get(1);
        int md2Time = Integer.parseInt(md2.getDt_txt().substring(md2.getDt_txt().length()-8).substring(0,2));
        if(h-md1Time < md2Time - h){
            return md1;
        }else{
            return md2;
        }
    }

    protected int roundHour(String hour){
        int min = Integer.parseInt(hour.substring(4));
        if(min<30){
            return Integer.parseInt(hour.substring(0,2));
        }else{
            return Integer.parseInt(hour.substring(0,2))+1;
        }
    }

    protected void weekResume(){
        List<String> dayOfWeek = new ArrayList<>();
        dayOfWeek.add("Sunday");
        dayOfWeek.add("Monday");
        dayOfWeek.add("Tuesday");
        dayOfWeek.add("Wednesday");
        dayOfWeek.add("Thursday");
        dayOfWeek.add("Friday");
        dayOfWeek.add("Saturday");
        int nrDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
        String lastday = weather.get(0).getDt_txt().substring(0,10);
        Double minTemp = null;
        Double maxTemp = null;
        for(MomentOfDay md : weather){
            if(!md.getDt_txt().contains(lastday)){
                String min = Math.round(minTemp)+"°C";
                String max = Math.round(maxTemp)+"°C";
                if(weekList.size() == 0){
                    weekList.add(new DayOfWeek("Today", min , max));
                }else{
                    weekList.add(new DayOfWeek(dayOfWeek.get(nrDay), min , max));
                }
                nrDay = nrDay == 6 ? 0 : nrDay+1;
                minTemp = null;
                maxTemp = null;
                lastday = md.getDt_txt().substring(0,10);
            }else{
                if(minTemp == null || minTemp > Double.parseDouble(md.getMain().getTemp_min())){
                    minTemp = Double.valueOf(md.getMain().getTemp_min());
                }
                if(maxTemp == null || maxTemp < Double.parseDouble(md.getMain().getTemp_max())){
                    maxTemp = Double.valueOf(md.getMain().getTemp_max());
                }
            }

        }
        adapter.submit(weekList);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void setImg(String description, int time){
        switch (description){
            case "Clouds":
                img.setImageDrawable(getResources().getDrawable(R.drawable.clouds,getActivity().getApplicationContext().getTheme()));
                break;
            case "Rain":
                img.setImageDrawable(getResources().getDrawable(R.drawable.rain,getActivity().getApplicationContext().getTheme()));
                break;
            case "Snow":
                img.setImageDrawable(getResources().getDrawable(R.drawable.snow,getActivity().getApplicationContext().getTheme()));
                break;
            case "Clear":
                if(time>7 && time<21){
                    img.setImageDrawable(getResources().getDrawable(R.drawable.sun,getActivity().getApplicationContext().getTheme()));
                }else{
                    img.setImageDrawable(getResources().getDrawable(R.drawable.moon,getActivity().getApplicationContext().getTheme()));
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    protected void configureInfo(){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentHour = dateFormat.format(date);
        time.setText(currentHour);

        MomentOfDay md = getMomentOfDay(currentHour);
        temp.setText(Math.round(Double.parseDouble(md.getMain().getTemp())) +"°C");
        setImg(md.getWeather().get(0).getMain(), roundHour(currentHour));

        adapter = new DayOfWeekAdapter(weekList);
        int cnt = adapter.getItemCount();
        if (cnt == 0){
            weekResume();
        }
    }

    @Override
    public void getAll(List<Location> locations) {

    }

    @Override
    public void add(String result) {

    }

    @Override
    public void update(String result) {

    }

    @Override
    public void findByCity(Location loc) {

    }

    @Override
    public void findBySelected(Location loc) {
        Call<WeatherApiModel> call;
        if(loc == null){
            String lat, log;
            lat = bundle.getString("latitude");
            log = bundle.getString("longitude");
            call = ApiBuilder.getInstance().getWeather(lat, log, ApiBuilder.UNITS, ApiBuilder.APP_ID);
            call.enqueue(new Callback<WeatherApiModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<WeatherApiModel> call, @NonNull retrofit2.Response<WeatherApiModel> response) {
                    weather = response.body().getList();
                    cityLocation = response.body().getCity().getName();

                    city.setText(cityLocation);
                    bundleSeeMore.putString("latitude",lat);
                    bundleSeeMore.putString("longitude",log);

                    configureInfo();
                }

                @Override
                public void onFailure(@NonNull Call<WeatherApiModel> call, @NonNull Throwable t) {

                }
            });
        }else{
            call = ApiBuilder.getInstance().getWeatherByCity(loc.getCity(), ApiBuilder.UNITS, ApiBuilder.APP_ID);
            call.enqueue(new Callback<WeatherApiModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<WeatherApiModel> call, @NonNull retrofit2.Response<WeatherApiModel> response) {
                    weather = response.body().getList();
                    cityLocation = response.body().getCity().getName();

                    city.setText(cityLocation);
                    bundleSeeMore.putString("city",cityLocation);

                    configureInfo();
                }

                @Override
                public void onFailure(@NonNull Call<WeatherApiModel> call, @NonNull Throwable t) {

                }
            });
        }
    }

    @Override
    public void delete(String result) {

    }
}
