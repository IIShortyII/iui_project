package com.example.drunk_o_meter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.drunk_o_meter.nlp.NlpPipeline;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupApplication();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onResume() {
        super.onResume();
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
        if (UserData.USERNAME.length() == 0){
            // go to onboarding to provide username
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
            Intent intent = new Intent(MainActivity.this, DrunkometerActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }
}