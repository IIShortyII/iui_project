package com.example.drunk_o_meter;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendationActivity extends AppCompatActivity {

    //TODO: if drunkennessScore == drunkAF --> no drink2 amount (=go home)
    public TextView drunkennessScore;

    public TextView drink1Name;
    public TextView drink1Amount;
    public ImageView drink1Image;

    public TextView drink2Title;
    public TextView drink2Amount;
    public ImageView drink2Image;

    //TODO: get from analysis
    private int drunkennessScoreInt = 4;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        //TODO: evtl mit ViewHolder ersetzen
        drunkennessScore = findViewById(R.id.recommendation_drunkennessScore_value);
        drink1Name = findViewById(R.id.recommendation_drink1_name);
        drink1Amount = findViewById(R.id.recommendation_drink1_amount);
        drink1Image = findViewById(R.id.recommendation_drink1_image);
        drink2Title = findViewById(R.id.recommendation_drink2_name);
        drink2Amount = findViewById(R.id.recommendation_drink2_amount);
        drink2Image = findViewById(R.id.recommendation_drink2_image);

        drunkennessScore.setText(calculateDrunkennessScore());

        //TODO: exchange with correct value for "drunk AF"
        if (drunkennessScoreInt == 4) {
            drink1Name.setText("TaZwiWa");
            drink1Amount.setText("As much as possible");
            drink1Image.setImageDrawable(getResources().getDrawable(R.drawable.tazwiwa));

            drink2Title.setText("Go home");
            drink2Amount.setText("ASAP!");
            drink2Image.setImageDrawable(getResources().getDrawable(R.drawable.gohome));
        }
    }

    public String calculateDrunkennessScore() {
        //TODO: calculate drunkenness score text based on level
        String drunkennessScoreTxt = "No value arrived from analysis";
        switch (drunkennessScoreInt) {
            case 0: drunkennessScoreTxt = "Sober and ready to party"; break;
            case 1: drunkennessScoreTxt = "Heating up"; break;
            case 2: drunkennessScoreTxt = "Ready to tear up the dancefloor"; break;
            case 3: drunkennessScoreTxt = "Drunk AF"; break;
        }

        return drunkennessScoreTxt;
    }

    /* Copied from OnboardingActivity --> needed?

    public void loadFragment(android.app.Fragment frag, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container_onboarding);

        if (fragment == null) {
            ft.add(R.id.fragment_container_onboarding, frag, tag);
        } else {
            ft.replace(R.id.fragment_container_onboarding, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }
     */
}