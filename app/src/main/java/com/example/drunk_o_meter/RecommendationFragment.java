package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.DRUNKOMETER_ANALYSIS_LIST;

import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.DrunkometerAnalysis;
import com.example.drunk_o_meter.userdata.UserData;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    //TODO: Adapt text style to fit to Kathrin's
    private View layout;

    private int drunkennessScoreInt = 4; // TODO get from analysis (integrate python) @Kathi this is the score from the drunk face detection analysis, right?

    //TODO: get from analysis
    private boolean safeToText = true;

    /**
     * Indicator that text analysis was conducted
     * @Kathi: das brauchst du gar nicht: if (this.text_message != null), dann ist textAnalysisConducted true
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

    @RequiresApi(api = Build.VERSION_CODES.R)
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
        // @Kathi du kannst stattdessen if (UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE != null) schreiben
        if (textAnalysisConducted) {
            String receiver = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getRecipient();
            messageReceiver.setText(receiver);

            // @Kathi TODO hier noch das Seniment aus der Text Message Analyse. Die möglichen Sentiments findest du in nlp/Sentiment
            String sentiment = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getSentimentAnalysis();

            // @Kathi TODO hier ist der Wert aus der Weaving Analysis
            // Wert <= 35 == normal (not drunk)
            // Wert >= 35 && <=  70 == drunk
            // Wert > 70 = WASTED
            int weavingPenaltyPoints = UserData.DRUNKOMETER_ANALYSIS.PenaltyPoint;

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

        @RequiresApi(api = Build.VERSION_CODES.R)
        public String calculateDrunkennessScore() {
            //TODO: calculate drunkenness score text based on level

            // @Kathi hier alle relevanten Infos aus der Analysis:

            // Typing Challenge
            double mean_error_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
            double mean_completiontime_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;

            // Baseline Typing error and completiontime (um einen signifikanten Unterschied zwischen den Werten aus challenge und baseline zu ermitteln)
            double mean_error_baseline = UserData.MEAN_ERROR_BASELINE;
            double mean_completiontime_baseline = UserData.MEAN_COMPLETIONTIME_BASELINE;

            // Drunkenness Prediction aus der drunk face analysis // TODO: fehlt noch
            double selfieDrunkPrediction = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION;

            String drunkennessScoreTxt = "No value arrived from analysis";
            switch (drunkennessScoreInt) {
                case 0: drunkennessScoreTxt = "Sober and ready to party"; break;
                case 1: drunkennessScoreTxt = "Heating up"; break;
                case 2: drunkennessScoreTxt = "Ready to tear up the dancefloor"; break;
                case 3: drunkennessScoreTxt = "Drunk AF"; break;
            }

            // Save finished drunkometerAnalysis to local storage
            addDrunkoMeterAnalysisToList();
            DataHandler.storeSettings(this.getActivity());

            return drunkennessScoreTxt;
    }

    /**
     * Add all information of the drunkometer Analysis object to the list ob drunkometer analysis
     */
    private void addDrunkoMeterAnalysisToList() {
            DrunkometerAnalysis newAnalysis = new DrunkometerAnalysis();
            newAnalysis.DRUNKENNESS_SCORE = drunkennessScoreInt;
            newAnalysis.MEAN_COMPLETIONTIME_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;
            newAnalysis.MEAN_ERROR_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
            newAnalysis.SELFIE = UserData.DRUNKOMETER_ANALYSIS.SELFIE;
            newAnalysis.SELFIE_DRUNK_PREDICTION = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION;
            if (UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE != null){
                newAnalysis.TEXT_MESSAGE = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE;
            }
            UserData.DRUNKOMETER_ANALYSIS_LIST.add(newAnalysis);
        }
}