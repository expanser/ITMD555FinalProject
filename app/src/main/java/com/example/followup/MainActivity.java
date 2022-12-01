package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //go to home or login depend on sharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                Intent intent= new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(getApplicationContext(),sharedPreferences.contains("id") ? HomeActivity.class : LoginActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }
}