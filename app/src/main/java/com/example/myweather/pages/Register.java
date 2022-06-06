package com.example.myweather.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;

public class Register extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        email = findViewById(R.id.EmailAddress);
        password= findViewById(R.id.Password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = email.getText().toString();
                String pass = password.getText().toString();
                if(user.length() == 0 || pass.length() == 0){
                    Toast.makeText(Register.this, "Email or password are not fill", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(user+pass+"data",user+"\n"+pass);
                    editor.commit();

                    Intent loginScreen = new Intent(Register.this, LogIn.class);
                    startActivity(loginScreen);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    Toast.makeText(Register.this, "Register with success", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
