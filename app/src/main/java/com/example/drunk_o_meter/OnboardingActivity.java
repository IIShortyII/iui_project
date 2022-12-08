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
import android.widget.EditText;

import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_SAMPLES;
import static com.example.drunk_o_meter.userdata.UserData.TYPING_CHALLENGE_SAMPLES;
import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

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
     * @param stage the stage the user is in ("username" or "typingChallenge")
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStage(String stage) {

        switch (stage) {
            case "username":
                FragmentUsername fragmentUsername = new FragmentUsername();
                loadFragment(fragmentUsername, "fragmentUsername");
                break;

            case "typingChallenge":
                FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
                loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
                break;

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
     * Proceed to next fragment / activity depending on the current stage
     */
    public void next(View view) {

        switch(stage) {
            case "username":
                EditText usernameInput = findViewById(R.id.usernameInput);
                String username = String.valueOf(usernameInput.getText());
                if (username.length() != 0) {
                    USERNAME = username;
                    FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
                    loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
                    this.stage = "typingChallenge";

                    Log.d("D-O-M USERNAME", USERNAME);

                    // Save username to local device storage

                }

                break;

            case "typingChallenge":
                if (BASELINE_TYPING_SAMPLES.size() == 0){
                    FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
                    loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");
                } else {
                    Intent intent = new Intent(OnboardingActivity.this, DrunkometerActivity.class);
                    OnboardingActivity.this.startActivity(intent);

                    Log.d("D-O-M BASELINE", String.valueOf(BASELINE_TYPING_SAMPLES));
                }
                break;

        }
    }



}
