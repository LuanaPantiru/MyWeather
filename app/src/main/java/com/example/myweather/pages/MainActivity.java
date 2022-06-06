package com.example.myweather.pages;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myweather.R;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    protected LocationManager locationManager;
    protected String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10800000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                Bundle bundle = new Bundle();
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navbar);
                bottomNav.setOnItemSelectedListener(navListener);
                Fragment frg = new HomePage();
                frg.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frg).commit();
            }
        });
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomePage();
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude",latitude);
                        bundle.putString("longitude",longitude);
                        selectedFragment.setArguments(bundle);
                        break;
                    case R.id.nav_add_location:
                        selectedFragment = new Cities();
                        break;
                        case R.id.nav_log_out:
                            gotoLogIn();
                            break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                }
                return true;
            };

    private void gotoLogIn() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LogIn.class);
        intent.putExtra("LOG_OUT", true);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


}