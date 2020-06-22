package com.example.frosty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.frosty.Registration.RegisterActivity;
import com.example.frosty.Registration.UserNameActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(1500);
        if (FirebaseAuth.getInstance().getCurrentUser() !=null)                         //for not asking for login again and agin
        {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Intent intent= new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
