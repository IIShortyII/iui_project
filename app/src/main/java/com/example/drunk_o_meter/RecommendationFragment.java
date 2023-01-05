package com.example.drunk_o_meter;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    private View layout;

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

    public RecommendationFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.layout = inflater.inflate(R.layout.fragment_recommendation, container, false);

        //TODO: evtl mit ViewHolder ersetzen
        drunkennessScore = layout.findViewById(R.id.recommendation_drunkennessScore_value);
        drink1Name = layout.findViewById(R.id.recommendation_drink1_name);
        drink1Amount = layout.findViewById(R.id.recommendation_drink1_amount);
        drink1Image = layout.findViewById(R.id.recommendation_drink1_image);
        drink2Title = layout.findViewById(R.id.recommendation_drink2_name);
        drink2Amount = layout.findViewById(R.id.recommendation_drink2_amount);
        drink2Image = layout.findViewById(R.id.recommendation_drink2_image);

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
        // Inflate the layout for this fragment
        return layout;
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
}