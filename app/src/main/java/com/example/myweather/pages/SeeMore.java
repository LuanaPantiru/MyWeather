package com.example.myweather.pages;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.myweather.R;
import com.example.myweather.api.ApiBuilder;
import com.example.myweather.model.CurrentDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeMore extends Fragment {

    private Button share;
    private Button camera;
    private ImageView back;
    private ImageView icon;
    private TextView city;
    private TextView details;
    private String message;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Bundle bundle;

    public SeeMore() {
        super(R.layout.details);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        icon = v.findViewById(R.id.imageView);
        city = v.findViewById(R.id.cityName);
        details = v.findViewById(R.id.weatherCondition);

        bundle = getArguments();
        Call<CurrentDay> call;
        String cityName = bundle.getString("city");
        if(cityName != null){
            call = ApiBuilder.getInstance().getByCity(cityName, ApiBuilder.UNITS, ApiBuilder.APP_ID);
            call.enqueue(new Callback<CurrentDay>() {
                @Override
                public void onResponse(Call<CurrentDay> call, Response<CurrentDay> response) {
                    CurrentDay currentDay = response.body();
                    city.setText(response.body().getName());
                    setImg(currentDay.getWeather().get(0).getMain());
                    String min = Math.round(Double.parseDouble(currentDay.getMain().getTemp_min()))+ "°C";
                    String max = Math.round(Double.parseDouble(currentDay.getMain().getTemp_max()))+ "°C";
                    String feels = Math.round(Double.parseDouble(currentDay.getMain().getFeels_like()))+ "°C";
                    message = "Sky in general: " + currentDay.getWeather().get(0).getMain()+ "\n" +
                            "Min temperature: " + min +"\n" +
                            "Max temperature: " + max + "\n"+
                            "Feels like: " + feels + "\n"+
                            "Humidity: " + currentDay.getMain().getHumidity() + "%\n"+
                            "Wind speed: " + currentDay.getWind().getSpeed()+ " km/h";
                    details.setText(message);
                }

                @Override
                public void onFailure(Call<CurrentDay> call, Throwable t) {

                }
            });
        }else{
            String lat, log;
            lat = bundle.getString("latitude");
            log = bundle.getString("longitude");
            call = ApiBuilder.getInstance().getCurrentDay(lat,log, ApiBuilder.UNITS, ApiBuilder.APP_ID);
            call.enqueue(new Callback<CurrentDay>() {
                @Override
                public void onResponse(Call<CurrentDay> call, Response<CurrentDay> response) {
                    CurrentDay currentDay = response.body();
                    city.setText(response.body().getName());
                    setImg(currentDay.getWeather().get(0).getMain());
                    String min = Math.round(Double.parseDouble(currentDay.getMain().getTemp_min()))+ "°C";
                    String max = Math.round(Double.parseDouble(currentDay.getMain().getTemp_max()))+ "°C";
                    String feels = Math.round(Double.parseDouble(currentDay.getMain().getFeels_like()))+ "°C";
                    message = "Sky in general: " + currentDay.getWeather().get(0).getMain()+ "\n" +
                            "Min temperature: " + min +"\n" +
                            "Max temperature: " + max + "\n"+
                            "Feels like: " + feels + "\n"+
                            "Humidity: " + currentDay.getMain().getHumidity() + "%\n"+
                            "Wind speed: " + currentDay.getWind().getSpeed()+ " km/h";
                    details.setText(message);
                }

                @Override
                public void onFailure(Call<CurrentDay> call, Throwable t) {

                }
            });
        }


        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                Fragment frg = new HomePage();
                frg.setArguments(bundle);
                fragmentTransaction.replace(R.id.home_page,frg);
                fragmentTransaction.commit();
            }
        });

        camera = v.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });

        share = v.findViewById(R.id.Share_day);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.setType("text/plain");

//                Drawable drawable = icon.getDrawable();
//                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//                try{
//                    File file = new File(getActivity().getApplicationContext().getExternalCacheDir(),File.separator + "image.jpg");
//                    FileOutputStream fOut = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                    fOut.flush();
//                    fOut.close();
//                    file.setReadable(true, false);
//                    Uri uri = FileProvider.getUriForFile(getActivity().getApplicationContext(), BuildConfig.APPLICATION_ID+".provider", file);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    shareIntent.setType("image/jpg");
//
//                }catch (IOException e){
//                    e.printStackTrace();
//                }

                startActivity(Intent.createChooser(shareIntent, null));
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

    protected void takePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            icon.setImageBitmap(imageBitmap);
            icon.setAdjustViewBounds(true);
            icon.setMaxHeight(100000);
            icon.setMaxWidth(100000   );
            int h = icon.getMaxHeight();
            int w = icon.getMaxWidth();
        }
    }
}
