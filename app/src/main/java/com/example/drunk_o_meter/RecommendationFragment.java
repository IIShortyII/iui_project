package com.example.drunk_o_meter;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    private View layout;


    //TODO: get from analysis
    private int drunkennessScoreInt = 4;
    private boolean safeToText = true;

    /**
     * Indicator that text analysis was conducted
     */
    private static final String TEXT_ANALYSIS_CONDUCTED = "textAnalysisConducted";
    private boolean textAnalysisConducted;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param textAnalysisConducted Whether text analysis was done (=true) or skipped (=false).
     * @return A new instance of fragment SettingsFragment.
     */
    public static RecommendationFragment newInstance(boolean textAnalysisConducted) {
        RecommendationFragment fragment = new RecommendationFragment();
        Bundle args = new Bundle();
        args.putBoolean(TEXT_ANALYSIS_CONDUCTED, textAnalysisConducted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            textAnalysisConducted = getArguments().getBoolean(TEXT_ANALYSIS_CONDUCTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.layout = inflater.inflate(R.layout.fragment_recommendation, container, false);

        TextView drunkennessScore = layout.findViewById(R.id.recommendation_drunkennessScore_value);

        TextView drink1Name = layout.findViewById(R.id.recommendation_drink1_name);
        TextView drink1Amount = layout.findViewById(R.id.recommendation_drink1_amount);
        ImageView drink1Image = layout.findViewById(R.id.recommendation_drink1_image);
        TextView drink2Title = layout.findViewById(R.id.recommendation_drink2_name);
        TextView drink2Amount = layout.findViewById(R.id.recommendation_drink2_amount);
        ImageView drink2Image = layout.findViewById(R.id.recommendation_drink2_image);

        LinearLayout textAnalysisResult = layout.findViewById(R.id.recommendation_safeToText);
        TextView messageReceiver = layout.findViewById(R.id.recommendation_safeToText_receiver);
        TextView safeToTextValue = layout.findViewById(R.id.recommendation_safeToText_value);
        ImageButton copyMessageContent = layout.findViewById(R.id.recommendation_copyMessageContent);

        drunkennessScore.setText(calculateDrunkennessScore());

        //Only show result of text analysis if a message was entered
        if (textAnalysisConducted) {
            //TODO: get from message
            messageReceiver.setText("Example Receiver");
            if (safeToText) {
                safeToTextValue.setText("Safe To Text");
                copyMessageContent.setVisibility(View.VISIBLE);
            } else {
                safeToTextValue.setText("Not Safe To Text");
                copyMessageContent.setVisibility(View.GONE);
            }
        } else {
            textAnalysisResult.setVisibility(View.GONE);
        }

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