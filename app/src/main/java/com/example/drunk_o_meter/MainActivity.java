package com.example.drunk_o_meter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int BASELINE_TEXT_COUNT = 10;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fetch settings
        try {
            DataHandler.loadSettings(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // determine mode
        if (UserData.USERNAME.length() == 0){
            // go to onboarding to provide username
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("stage", "username");
            MainActivity.this.startActivity(intent);
        } else if (UserData.BASELINE_TYPING.size() != BASELINE_TEXT_COUNT){
            // go to onboarding to provide baseline typing samples
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("stage", "baseline");
            MainActivity.this.startActivity(intent);

        } else {
            // go to actual app
            Intent intent = new Intent(MainActivity.this, DrunkometerActivity.class);
            MainActivity.this.startActivity(intent);

        }
    }
}