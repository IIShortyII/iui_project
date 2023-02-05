package com.example.drunk_o_meter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checkForPermissions();
        //setupApplication();
    }

    private void checkForPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("D-O-M permission", "no camera permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onResume() {
        super.onResume();
        checkForPermissions();
        setupApplication();
    }

    /**
     * Setup the application based on the available user data.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setupApplication() {
        // fetch settings
        try {
            DataHandler.loadSettings(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // determine mode
        if (UserData.AGE_CHECK == false) {
            // go to onboarding to check age
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("stage", "ageCheck");
            MainActivity.this.startActivity(intent);
        } else if (UserData.USERNAME.length() == 0 || UserData.WEIGHT == 0){
            // go to onboarding to provide personal data
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("stage", "username");
            MainActivity.this.startActivity(intent);
        } else if (UserData.BASELINE_TYPING_CHALLENGE.size() != getResources().getInteger(R.integer.baselineCount)){
            // Clear potential unfinished typing samples
            UserData.BASELINE_TYPING_CHALLENGE = new ArrayList<>();
            // go to onboarding to provide baseline typing samples
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("stage", "typingChallenge");
            MainActivity.this.startActivity(intent);

        } else {
            // go to actual app
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }


}