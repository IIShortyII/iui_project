package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_SAMPLES;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.drunk_o_meter.nlp.FragmentTypingChallenge;
import com.example.drunk_o_meter.nlp.TypingSample;
import com.example.drunk_o_meter.userdata.UserData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class OnboardingActivity extends AppCompatActivity {
    private String stage;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        stage = getIntent().getStringExtra("stage");
        setStage(stage);

    }

    /**
     * Depending on the stage the user is in, provide the corresponding content of the onboarding flow.
     * @param stage the stage the user is in ("username" or "baseline")
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStage(String stage) {
        if(stage.equals("username")) {

            // } else if (stage.equals("onboarding")) {

            // Set up UI components
            FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
            loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");

        }
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

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment == null)
        {
            ft.add(R.id.fragment_container, frag, tag);
        } else
        {
            ft.replace(R.id.fragment_container, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    /**
     * Proceed to DrunkometerActivity
     */
    public void finishTyping(View view) {
        Intent intent = new Intent(OnboardingActivity.this, DrunkometerActivity.class);
        OnboardingActivity.this.startActivity(intent);
    }



}
