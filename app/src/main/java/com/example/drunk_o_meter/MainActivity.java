package com.example.drunk_o_meter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    //TODO: add tab for past recommendations?

    /**
     * Main UI component for the navigation bar
     */
    BottomNavigationView bottomNavigationView;

    /**
     * The tab, which shows the main functionality to find the (drinks) recommendation
     */
    FragmentDrunkometerStart drunkometerFragment;

    /**
     * The tab, which shows the chats that were not send in combination with the selfie taken in the same challenge --> also move safe-to-text challenge here?
     */
    ChatsFragment chatsFragment;

    /**
     * TODO: remove @Kathi
     */
    RecommendationFragment recommendationFragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupApplication();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.drunkometer);
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

    /**
     *
     * Setup navigation view (=tabs)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //create fragments
        drunkometerFragment = new DrunkometerFragment();
        chatsFragment = new ChatsFragment();
        //TODO: remove after test
        recommendationFragment = new RecommendationFragment();

        switch (item.getItemId()) {
            case R.id.drunkometer:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, drunkometerFragment).commit();
                return true;

            case R.id.chats:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, chatsFragment).commit();
                return true;

            //TODO: remove after test
            case R.id.recommendation:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, recommendationFragment).commit();
                return true;
        }
        return false;
    }
}