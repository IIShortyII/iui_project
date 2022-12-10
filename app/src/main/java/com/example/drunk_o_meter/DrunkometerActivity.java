package com.example.drunk_o_meter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;

public class DrunkometerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunkometer);

        // Setup start view of drunkomenter
        FragmentDrunkometerStart fragmentDrunkometerStart = new FragmentDrunkometerStart();
        loadFragment(fragmentDrunkometerStart, "fragmentDrunkometerStart");
    }

    /**
     * Load fragment into fragment container
     * @param frag The fragment to be loaded
     * @param tag The tag of the fragment to be loaded
     */
    public void loadFragment(android.app.Fragment frag, String tag)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container_drunkometer);

        if(fragment == null)
        {
            ft.add(R.id.fragment_container_drunkometer, frag, tag);
        } else
        {
            ft.replace(R.id.fragment_container_drunkometer, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    /**
     * Start drunkometer analysis
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startDrunkometerAnalysis(View view) {
        FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
        loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
    }

    /**
     * Continue to typing challenge
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startTypingChallenge(View view) {
        FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
        loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");

    }
}