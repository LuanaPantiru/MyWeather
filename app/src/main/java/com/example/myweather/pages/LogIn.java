package com.example.myweather.pages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myweather.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LogIn extends AppCompatActivity {

    private ImageView img;
    private TextView title;
    private EditText email;
    private EditText password;
    private Button logIn;
    private Button register;
    private LoginButton fb;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        img = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        email = findViewById(R.id.EmailAddress);
        password= findViewById(R.id.Password);
        logIn = findViewById(R.id.log_in);
        register = findViewById(R.id.register);
        fb = findViewById(R.id.facebook);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && !accessToken.isExpired()){
            Intent activityScreen = new Intent(LogIn.this, MainActivity.class);
            activityScreen.putExtra("location", bundle);
            startActivity(activityScreen);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }else{
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            ObjectAnimator image;
            ObjectAnimator text;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                image = ObjectAnimator.ofFloat(img, "translationX",width/2-150);
                text = ObjectAnimator.ofFloat(title, "translationX",-width/2+240);
            }else{
                image = ObjectAnimator.ofFloat(img, "translationX",width/2-200);
                text = ObjectAnimator.ofFloat(title, "translationX",-width/2+370);
            }
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(image,text);
            animator.setDuration(2000);
            animator.start();

            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = email.getText().toString();
                    String pass = password.getText().toString();
                    if(user.length() == 0 || pass.length() == 0){
                        Toast.makeText(LogIn.this, "Email or password are not fill", Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferences pref = getSharedPreferences("PREFS", MODE_PRIVATE);
                        String details = pref.getString(user+pass+"data","Username or Password is Incorrect.");
                        if(!details.contains("Username or Password is Incorrect.")) {
                            Intent activityScreen = new Intent(LogIn.this, MainActivity.class);
                            activityScreen.putExtra("location", bundle);
                            startActivity(activityScreen);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Username or Password is Incorrect.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent registerScreen = new Intent(LogIn.this, Register.class);
                    startActivity(registerScreen);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });

            callbackManager = CallbackManager.Factory.create();
            fb.setReadPermissions(Arrays.asList(EMAIL));
            fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Intent activityScreen = new Intent(LogIn.this, MainActivity.class);
                    activityScreen.putExtra("location", bundle);
                    startActivity(activityScreen);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),"Username or Password is Incorrect.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest =GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //Print them out
                        Log.d("Demo", object.toString());
                        try {
                            String name = object.getString("name");
                            Toast.makeText(getApplicationContext(),"Welcome, "+ name,Toast.LENGTH_SHORT).show();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "gender, name, id, first_name, last_name");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }

    protected void onResume(){
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean bool = extras.getBoolean("LOG_OUT");
            if(bool){
                AccessToken token;
                token = AccessToken.getCurrentAccessToken();

                if (token != null) {
                    fb.performClick();
                }
            }
        }

    }
}
