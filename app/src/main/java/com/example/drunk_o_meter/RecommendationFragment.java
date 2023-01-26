package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.DRUNKOMETER_ANALYSIS_LIST;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.example.drunk_o_meter.userdata.Gender;
import com.example.drunk_o_meter.userdata.UserData;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    private View layout;
    protected Activity contextActivity;

    private ArrayList<String[]> preferredWines;
    private ArrayList<String[]> preferredBeers;
    private ArrayList<String[]> preferredAperitif;
    private ArrayList<String[]> preferredCocktails;
    private ArrayList<String[]> preferredShots;
    private ArrayList<String[]> preferredHots;

    /**
     * Indicator that text analysis was conducted
     */
    private boolean textAnalysisConducted;

    public RecommendationFragment() {
        // Required empty public constructor
        textAnalysisConducted = (UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE != null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            contextActivity = (Activity) context;
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
        LinearLayout copyMessageContent = layout.findViewById(R.id.recommendation_copyMessageContent);

        loadAlcoholData();

        int drunkennessScoreInt = calculateDrunkennessScore();
        //TODO @Dennis replace drink1 and drink2 with the recommended drinks --> siehe case 4
        String[] drink1 = {"Drink 1","N.a.","N.a.","0","0"};
        String[] drink2 = {"Drink 2","N.a.","N.a.","0","0"};
        Drawable drink1Img = getResources().getDrawable(R.drawable.defaultdrink, contextActivity.getTheme());
        Drawable drink2Img = getResources().getDrawable(R.drawable.defaultdrink, contextActivity.getTheme());

        switch (drunkennessScoreInt) {
            //get random drink from the preferred lists
            //DRINKS.get(DrinkType.WINE)
            //TODO @Dennis according to recommendation matrix
            case 0:

                // Shots / Long Drink, Cocktail, Hot Drink / Beer, Wine, Aperitif



                break;
            case 1:
                // Shots / Long Drink, Cocktail, Hot Drink / Beer, Wine, Aperitif

                break;
            case 2:

                //Long Drink, Cocktail, Hot Drink / Beer, Wine, Aperitif / TaZwiWa

                break;
            case 3:
                // Beer, Wine, Aperitif / TaZwiWa / Go Home
                break;
            case 4:
                // Go Home
                drink1 = new String[]{"TaZwiWa", "Sober", "N.a", "As much as possible", "0"};
                drink1Img = getResources().getDrawable(R.drawable.tazwiwa, contextActivity.getTheme());
                drink2 = new String[]{"Go home", "Sober", "N.a", "ASAP!", "0"};
                drink2Img = getResources().getDrawable(R.drawable.gohome, contextActivity.getTheme());
                break;
        }

        drink1Name.setText(drink1[0]);
        drink1Amount.setText(drink1[3]);
        drink1Image.setImageDrawable(drink1Img);

        drink2Title.setText(drink2[0]);
        drink2Amount.setText(drink2[3]);
        drink2Image.setImageDrawable(drink2Img);

        UserData.RECOMMENDATION = new ArrayList<>();
        UserData.RECOMMENDATION.add(drink1);
        UserData.RECOMMENDATION.add(drink2);

        DataHandler.storeSettings(contextActivity);
        drunkennessScoreTxt.setText(getDrunkennessScoreText(drunkennessScoreInt));

        //Only show result of text analysis if a message was entered
        if (textAnalysisConducted) {
            String receiver = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getRecipient();
            messageReceiver.setText(receiver);

            boolean safeToText = ((HomeActivity) contextActivity).calculateSafeToText(drunkennessScoreInt);

            if (safeToText) {
                safeToTextValue.setText("safe to text");
                copyMessageContent.setVisibility(View.VISIBLE);
            } else {
                safeToTextValue.setText("NOT safe to text");
                copyMessageContent.setVisibility(View.GONE);
            }
        } else {
            TextView textAnalysisTitle = layout.findViewById(R.id.recommendation_safeToText_title);
            textAnalysisTitle.setVisibility(View.GONE);
            textAnalysisResult.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return layout;
    }

    public void getDrinkfromList(){

    }


    // Inspiration for typing speed and error rate: https://www.vice.com/en/article/78kp7a/how-does-drug-use-affect-typing-speed-a-highly-scientific-investigation
    /**
     * Calculates PerMille Alcohol of User depending on taken Alcohol, Sex and user weight.
     * @return Users PerMille Alcohol Level in Double
     * @param totalGramAlcohol
     * @param userGender
     * @param userWeight
     */
    public double calculatePerMillAlcohol(double totalGramAlcohol, Gender userGender, int userWeight){
        double maleReductionFactor = 0.69;
        double femaleReductionFactor = 0.58;
        double reductionFactor;

        if(userGender == Gender.FEMALE){
            reductionFactor = femaleReductionFactor;
        }else {
            reductionFactor = maleReductionFactor;
        };

        double perMillAlcohol = totalGramAlcohol / (userWeight*reductionFactor);
        return perMillAlcohol;
    }
    /**
     * Converts PerMille Alcohol to DrunkScore
     * @return DrunkScore Double
     * @param perMill
     */
    public double perMillAlcoholToDrunkScore(double perMill){
        double drunkScore;
        if (perMill > 2.00){
            drunkScore = 4;
        }else if(perMill >1.00){
            drunkScore = 3;
        }else if(perMill >0.80){
            drunkScore = 2;
        }else if(perMill >0.30){
            drunkScore = 1;
        }else{
            drunkScore = 0;
        }
        return drunkScore;
    }
    /**
     * Calculates DrunkScore depending Text Challenge Results
     * @return DrunkScore Double
     * @param errorBase
     * @param errorChall
     * @param timeBase
     * @param timeChall
     */
    public double CalculateTextScore(double errorBase, double errorChall, double timeBase, double timeChall){
        double diffErrorRate = Math.abs(errorBase-errorChall);
        double diffTime = timeChall/timeBase;
        double drunkScore =0.0;

        // Inspiration Error: 25 (DRUNK AF) vs. 4 (TIPSY) vs. 1 (SOBER)
        if (diffErrorRate>20.00) {
            drunkScore =drunkScore+1;
        }else if (diffErrorRate>10.00){
            drunkScore =drunkScore+0.5;
        }

        // Inspiration CT: 42 WPM  (DRUNK AF) vs. 93 WPM (TIPSY) vs. 97 WPM (SOBER)
        if(diffTime>50){
            drunkScore =drunkScore+1;
        }else if (diffTime>10.00){
            drunkScore =drunkScore+0.5;
        }
        return drunkScore;
    }


    /**
     * Main function of the recommender that combines all factors collected in the analysis and calculates the drunkenness
     * @return int for drunkenness between 0 and 4
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public int calculateDrunkennessScore(){
        // Typing Challenge
        double mean_error_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
        double mean_completiontime_challenge = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;

        // Baseline Typing error and completiontime (um einen signifikanten Unterschied zwischen den Werten aus challenge und baseline zu ermitteln)
        double mean_error_baseline = UserData.MEAN_ERROR_BASELINE;
        double mean_completiontime_baseline = UserData.MEAN_COMPLETIONTIME_BASELINE;

        // Drunkenness Prediction aus der drunk face analysis
        double selfieDrunkPrediction = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION;

        // Weaving analysis results
        int weavingPenaltyPoints = UserData.DRUNKOMETER_ANALYSIS.PenaltyPoint;

        //Personal info
        int userWeight = UserData.WEIGHT;
        Gender gender = UserData.GENDER;

        //Grams of Taken Alcohol
        double takenAlcohol = UserData.GRAM_OF_ALCOHOL;

        //Calculating user PerMill alcohol intake
        double perMillAlcohol = calculatePerMillAlcohol(takenAlcohol,gender, userWeight);
        //Converting PerMill alcohol to DrunkScore
        double drunkScore = perMillAlcoholToDrunkScore(perMillAlcohol);

        //Adding WeavingAnalysis Factor
        if(weavingPenaltyPoints >70){
            drunkScore = drunkScore + 1;
        } else if (weavingPenaltyPoints > 34){
            drunkScore = drunkScore + 0.5;
        }

        //Adding TypingChallenge Factor
        drunkScore= drunkScore+CalculateTextScore(mean_error_baseline,mean_error_challenge,mean_completiontime_baseline,mean_completiontime_challenge);
        drunkScore = drunkScore*selfieDrunkPrediction+0;

        //Set drunkScore to max Value, if drunkScore is over max Value
        if (drunkScore > 4){
            drunkScore = 4;
        }
        //Convert drunkScore to Integer
        int drunkScoreInt = Integer.valueOf((int) Math.round(drunkScore));
        // Save finished drunkometer Analysis to local storage
        addDrunkoMeterAnalysisToList(drunkScoreInt);
        DataHandler.storeSettings(this.getActivity());

        return drunkScoreInt;
    }

    /**
     *
     * @param drunkennessScoreInt
     * @return human readable form of drunkenness calculation result
     */
    public String getDrunkennessScoreText(int drunkennessScoreInt) {
        String drunkennessScoreTxt = "No value arrived from analysis";
        switch (drunkennessScoreInt) {
            case 0: drunkennessScoreTxt = "\uD83E\uDD73 Sober and ready to party \uD83E\uDD73 "; break;
            case 1: drunkennessScoreTxt = "\uD83C\uDF21 Heating up\uD83C\uDF21️"; break;
            case 2: drunkennessScoreTxt = "\uD83D\uDD25 On fire \uD83D\uDD25"; break;
            case 3: drunkennessScoreTxt = "\uD83D\uDC83 Ready to tear up the dance floor \uD83D\uDD7A"; break;
            case 4: drunkennessScoreTxt = "\uD83E\uDD2A Drunk AF \uD83E\uDD2A"; break;
        }

        return drunkennessScoreTxt;
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
        ArrayList wineList = UserData.DRINKS.get(DrinkType.WINE);
        ArrayList beerList = UserData.DRINKS.get(DrinkType.BEER);
        ArrayList aperitifList = UserData.DRINKS.get(DrinkType.APERITIF);
        ArrayList cocktailsList = UserData.DRINKS.get(DrinkType.COCKTAIL);
        ArrayList shotsList = UserData.DRINKS.get(DrinkType.SHOT);
        ArrayList hotList = UserData.DRINKS.get(DrinkType.WINE);

        preferredWines = new ArrayList<>();
        preferredBeers = new ArrayList<>();
        preferredAperitif = new ArrayList<>();
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
                case "Aperitif":
                    if (aperitifList.contains(alcohol[0])) {
                        preferredAperitif.add(alcohol);
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