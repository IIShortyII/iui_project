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

import com.example.drunk_o_meter.recommender.CSVFile;
import com.example.drunk_o_meter.recommender.DrinkType;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.DrunkometerAnalysis;
import com.example.drunk_o_meter.userdata.UserData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    //TODO: Adapt text style to fit to Kathrin's
    private View layout;

    private ArrayList<String[]> preferredWines;
    private ArrayList<String[]> preferredBeers;
    private ArrayList<String[]> preferredCocktails;
    private ArrayList<String[]> preferredShots;
    private ArrayList<String[]> preferredHots;

    //TODO: user chooses one option -> save to User Data & ask about that the next time the drunkometer was started

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

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.layout = inflater.inflate(R.layout.fragment_recommendation, container, false);

        TextView drunkennessScoreTxt = layout.findViewById(R.id.recommendation_drunkennessScore_value);

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

        loadAlcoholData();

        int drunkennessScoreInt = calculateDrunkennessScore();

        switch (drunkennessScoreInt) {
            //get random drink from the preferred lists
            case 0:
                break;
            case 1:
                //TODO -> all drinks possible
                break;
            case 2:
                //TODO -> only Cocktails, Hot Drinks, Beer, Wine
                break;
            case 3:
                //TODO -> only Beer, Wine
                break;
            case 4:
                drink1Name.setText("TaZwiWa");
                drink1Amount.setText("As much as possible");
                drink1Image.setImageDrawable(getResources().getDrawable(R.drawable.tazwiwa));

                drink2Title.setText("Go home");
                drink2Amount.setText("ASAP!");
                drink2Image.setImageDrawable(getResources().getDrawable(R.drawable.gohome));
                break;
        }

        drunkennessScoreTxt.setText(getDrunkennessScoreText(drunkennessScoreInt));

        //Only show result of text analysis if a message was entered
        if (textAnalysisConducted) {
            String receiver = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getRecipient();
            messageReceiver.setText(receiver);

            boolean safeToText = calculateSafeToText(drunkennessScoreInt);

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

        // Inflate the layout for this fragment
        return layout;
    }

    /**
     * Main function of the recommender that combines all factors collected in the analysis and calculates the drunkenness
     * @return int for drunkenness between 0 and 4
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public int calculateDrunkennessScore(){
        //TODO: calculate drunkenness score text based on level
        //@Dennis hier die Drunkenness berechnen

        // Typing Challenge
        double mean_error_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
        double mean_completiontime_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;

        // Baseline Typing error and completiontime (um einen signifikanten Unterschied zwischen den Werten aus challenge und baseline zu ermitteln)
        double mean_error_baseline = UserData.MEAN_ERROR_BASELINE;
        double mean_completiontime_baseline = UserData.MEAN_COMPLETIONTIME_BASELINE;

        // Drunkenness Prediction aus der drunk face analysis // TODO: fehlt noch
        double selfieDrunkPrediction = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION;

        // Weaving analysis results
        // Wert <= 35 == normal (not drunk)
        // Wert >= 35 && <=  70 == drunk
        // Wert > 70 = WASTED
        int weavingPenaltyPoints = UserData.DRUNKOMETER_ANALYSIS.PenaltyPoint;

        //@dennis hier den drunkennessScoreInt berechnen [0;4]
        int drunkennessScoreInt = 4;

        // Save finished drunkometerAnalysis to local storage
        addDrunkoMeterAnalysisToList(drunkennessScoreInt);
        DataHandler.storeSettings(this.getActivity());

        return drunkennessScoreInt;
    }

    /**
     *
     * @param drunkennessScoreInt
     * @return human readable form of drunkenness calculation result
     */
    public String getDrunkennessScoreText(int drunkennessScoreInt) {
        String drunkennessScoreTxt = "No value arrived from analysis";
        switch (drunkennessScoreInt) {
            case 0: drunkennessScoreTxt = "Sober and ready to party"; break;
            case 1: drunkennessScoreTxt = "Heating up"; break;
            case 2: drunkennessScoreTxt = "On fire"; break;
            case 3: drunkennessScoreTxt = "Ready to tear up the dance floor"; break;
            case 4: drunkennessScoreTxt = "Drunk AF"; break;
        }

        return drunkennessScoreTxt;
    }

    //TODO: is this redundant if we already calculate it in Home Activity?
    public boolean calculateSafeToText(int drunkennessScoreInt) {
        String sentiment = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getSentimentAnalysis();
        // @Kathi TODO hier noch das Sentiment aus der Text Message Analyse. Die möglichen Sentiments findest du in nlp/Sentiment
        boolean safeToText = false;

        return safeToText;
    }

    /**
     * Add all information of the drunkometer Analysis object to the list ob drunkometer analysis
     */
    private void addDrunkoMeterAnalysisToList(int drunkennessScoreInt) {
        DrunkometerAnalysis newAnalysis = new DrunkometerAnalysis();
        newAnalysis.DRUNKENNESS_SCORE = drunkennessScoreInt;
        newAnalysis.MEAN_COMPLETIONTIME_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;
        newAnalysis.MEAN_ERROR_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
        newAnalysis.SELFIE = UserData.DRUNKOMETER_ANALYSIS.SELFIE;
        newAnalysis.SELFIE_DRUNK_PREDICTION = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION;
        if (UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE != null){
            newAnalysis.TEXT_MESSAGE = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE;
        }

        // remove oldest analysis if there are only 4 analysis saved
        if (DRUNKOMETER_ANALYSIS_LIST.size() == 4) {
            DRUNKOMETER_ANALYSIS_LIST.remove(0);
        }
            UserData.DRUNKOMETER_ANALYSIS_LIST.add(newAnalysis);
    }

    /**
     * Reads the alcohol data from the user preferences
     */
    public void loadAlcoholData() {
        //TODO: Kategorien überprüfen --> Aperol eher wie Beer und Wein (extra Kategorie Aperitiv?), Kategorie harter Alkohol
        ArrayList wineList = UserData.DRINKS.get(DrinkType.WINE);
        ArrayList beerList = UserData.DRINKS.get(DrinkType.BEER);
        ArrayList cocktailsList = UserData.DRINKS.get(DrinkType.COCKTAIL);
        ArrayList shotsList = UserData.DRINKS.get(DrinkType.SHOT);
        ArrayList hotList = UserData.DRINKS.get(DrinkType.WINE);

        preferredWines = new ArrayList<>();
        preferredBeers = new ArrayList<>();
        preferredCocktails = new ArrayList<>();
        preferredShots = new ArrayList<>();
        preferredHots = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.alcohol_data);
        CSVFile csvFile = new CSVFile(inputStream);
        //list of string arrays with format: [Drink_Name, Drink_Type, Drink_Subtype, Drink_Liter, Drink_Alcohol_Pure_Gramm]
        List alcoholList = csvFile.read();

        //create arrayList of the user's preferred alcoholica containing all information
        for (Object item: alcoholList) {
            String[] alcohol = (String []) item;
            switch (alcohol[1]) {
                case "Wine":
                    if (wineList.contains(alcohol[0])) {
                    preferredWines.add(alcohol);
                    };
                    break;
                case "Beer":
                    if (beerList.contains(alcohol[0])) {
                        preferredBeers.add(alcohol);
                    };
                    break;
                case "Cocktail":
                    if (cocktailsList.contains(alcohol[0])) {
                        preferredCocktails.add(alcohol);
                    };
                    break;
                case "Shot":
                    if (shotsList.contains(alcohol[0])) {
                        preferredShots.add(alcohol);
                    };
                    break;
                case "Hot Drink":
                    if (hotList.contains(alcohol[0])) {
                        preferredHots.add(alcohol);
                    };
                    break;
            }
        }
    }
}