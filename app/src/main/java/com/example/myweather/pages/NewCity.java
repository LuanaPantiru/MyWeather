package com.example.myweather.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweather.R;
import com.example.myweather.api.ApiBuilder;
import com.example.myweather.data.AddOperation;
import com.example.myweather.data.Location;
import com.example.myweather.data.LocationRepository;
import com.example.myweather.model.CurrentDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCity extends Fragment implements LocationRepository{

    private ImageView back;
    private EditText input;
    private ImageView search;
    private ImageView icon;
    private TextView city;
    private TextView description;
    private Button save;
    private String message;

    public NewCity() { super(R.layout.new_city);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        back = v.findViewById(R.id.back);
        input = v.findViewById(R.id.searchCity);
        search = v.findViewById(R.id.search);
        icon = v.findViewById(R.id.imageView);
        city = v.findViewById(R.id.cityName);
        description = v.findViewById(R.id.weatherCondition);
        save = v.findViewById(R.id.save);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                String cityName = input.getEditableText().toString();
                if(cityName == null || cityName.length() == 0){
                    Toast.makeText(getActivity(), "Write a city", Toast.LENGTH_SHORT).show();
                }
                else{
                    Call<CurrentDay> call = ApiBuilder.getInstance().getByCity(cityName, ApiBuilder.UNITS, ApiBuilder.APP_ID);
                    call.enqueue(new Callback<CurrentDay>() {
                        @Override
                        public void onResponse(Call<CurrentDay> call, Response<CurrentDay> response) {
                            if(response.body() != null){

                                CurrentDay currentDay = response.body();
                                city.setText(response.body().getName());
                                setImg(currentDay.getWeather().get(0).getMain());
                                message = "Sky in general: " + currentDay.getWeather().get(0).getMain()+ "\n" +
                                        "Min temperature: " + currentDay.getMain().getTemp_min() + "°C\n" +
                                        "Max temperature: " + currentDay.getMain().getTemp_max() + "°C\n"+
                                        "Feels like: " + currentDay.getMain().getFeels_like() + "°C\n"+
                                        "Humidity: " + currentDay.getMain().getHumidity() + "%\n"+
                                        "Wind speed: " + currentDay.getWind().getSpeed()+ " km/h";
                                description.setText(message);
                                save.setVisibility(v.VISIBLE);
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Location loc = new Location(city.getText().toString(),false);
                                        new AddOperation(NewCity.this).execute(loc);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                                                R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                                        Fragment frg = new Cities();
                                        fragmentTransaction.replace(R.id.cities,frg);
                                        fragmentTransaction.commit();
                                    }
                                });
                            }else{
                                Toast.makeText(getActivity(), "The city doesn't exist", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<CurrentDay> call, Throwable t) {
                        }
                    });
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void setImg(String description){
        switch (description){
            case "Clouds":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.clouds,getActivity().getApplicationContext().getTheme()));
                break;
            case "Rain":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.rain,getActivity().getApplicationContext().getTheme()));
                break;
            case "Snow":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.snow,getActivity().getApplicationContext().getTheme()));
                break;
            case "Clear":
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("HH");
                int time = Integer.parseInt(dateFormat.format(date));
                if(time>7 && time<21){
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.sun,getActivity().getApplicationContext().getTheme()));
                }else{
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.moon,getActivity().getApplicationContext().getTheme()));
                }
                break;
        }
    }

    @Override
    public void getAll(List<Location> locations) {

    }

    @Override
    public void add(String result) {
        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(String result) {

    }

    @Override
    public void findByCity(Location loc) {

    }

    @Override
    public void findBySelected(Location loc) {

    }

    @Override
    public void delete(String result) {

    }
}
