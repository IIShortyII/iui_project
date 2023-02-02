package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.DRUNKOMETER_ANALYSIS_LIST;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.recommender.CSVFile;
import com.example.drunk_o_meter.recommender.DrinkType;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.DrunkometerAnalysis;
import com.example.drunk_o_meter.userdata.Gender;
import com.example.drunk_o_meter.userdata.UserData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    private String[] taZwiWa = new String[]{"TaZwiWa", "Sober", "TaZwiWa", "As much as possible", "0"};
    private String[] goHome = new String[]{"Go home", "Sober", "GoHome", "ASAP!", "0"};

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
        TextView textAnalysisTitle = layout.findViewById(R.id.recommendation_safeToText_title);
        TextView messageReceiver = layout.findViewById(R.id.recommendation_safeToText_receiver);
        TextView safeToTextValue = layout.findViewById(R.id.recommendation_safeToText_value);
        LinearLayout copyMessageContent = layout.findViewById(R.id.recommendation_copyMessageContent);

        loadAlcoholData();

        int drunkennessScoreInt = calculateDrunkennessScore();
        String[] drink1 = {};
        String[] drink2 = {};
        Drawable drink1Img = getResources().getDrawable(R.drawable.defaultdrink, contextActivity.getTheme());
        Drawable drink2Img = getResources().getDrawable(R.drawable.defaultdrink, contextActivity.getTheme());

        ArrayList<String[]> selectionForDrink1 = new ArrayList<>();
        ArrayList<String[]> selectionForDrink2 = new ArrayList<>();
        switch (drunkennessScoreInt) {
            case 0:
                // Shots, Cocktail, Hot Drink, Beer, Wine, Aperitif
                Collections.shuffle(preferredShots);
                drink1 = preferredShots.get(0);
                drink1Img = getResources().getDrawable(R.drawable.shot, contextActivity.getTheme());

                selectionForDrink2.addAll(preferredWines);
                selectionForDrink2.addAll(preferredBeers);
                selectionForDrink2.addAll(preferredAperitif);
                selectionForDrink2.addAll(preferredCocktails);
                selectionForDrink2.addAll(preferredHots);
                break;
            case 1:
                // Cocktail/Hot Drink, Shots, Beer, Wine, Aperitif
                selectionForDrink1.addAll(preferredHots);
                selectionForDrink1.addAll(preferredCocktails);

                selectionForDrink2.addAll(preferredWines);
                selectionForDrink2.addAll(preferredBeers);
                selectionForDrink2.addAll(preferredAperitif);
                selectionForDrink2.addAll(preferredShots);
                break;
            case 2:
                //Beer/Wine/Aperitif, Cocktail, Hot Drink, TaZwiWa
                selectionForDrink1.addAll(preferredBeers);
                selectionForDrink1.addAll(preferredWines);
                selectionForDrink1.addAll(preferredAperitif);

                selectionForDrink2.addAll(preferredCocktails);
                selectionForDrink2.addAll(preferredHots);
                selectionForDrink2.add(taZwiWa);
                break;
            case 3:
                // TaZwiWa, Beer/Wine/Aperitif, Go Home
                drink1 = taZwiWa;
                drink1Img = getResources().getDrawable(R.drawable.tazwiwa, contextActivity.getTheme());

                selectionForDrink2.addAll(preferredBeers);
                selectionForDrink2.addAll(preferredWines);
                selectionForDrink2.addAll(preferredAperitif);
                selectionForDrink2.add(goHome);
                break;
            case 4:
                // Go Home, GoHome
                drink1 = goHome;
                drink1Img = getResources().getDrawable(R.drawable.gohome, contextActivity.getTheme());

                drink2 = goHome;
                drink2Img = getResources().getDrawable(R.drawable.gohome, contextActivity.getTheme());
                break;
        }

        if (selectionForDrink1.size() > 0) {
            Collections.shuffle(selectionForDrink1);
            drink1 = selectionForDrink1.get(0);
            drink1Img = getImageforDrink(drink1[2]);
        }

        if (selectionForDrink2.size() > 0) {
            Collections.shuffle(selectionForDrink2);
            drink2 = selectionForDrink2.get(0);
            drink2Img = getImageforDrink(drink2[2]);
        }

        if (drink1.length == 0) {
            //something did not work -> why?
            Log.d("Drink Recommendation Failed", "Drunk Score: "+ drunkennessScoreInt + ", Values of preferred lists: " + "Wine " + preferredWines + ", Beer " + preferredBeers + ", Aperitif " + preferredAperitif + ", Cocktail " + preferredCocktails + ", Shots " + preferredShots + ", Hot Drink " + preferredHots);
            drink1 = new String[]{"Drink 1","N.a.","N.a.","0","0"};
        }

        if (drink2.length == 0) {
            //something did not work -> why?
            Log.d("Drink Recommendation Failed", "Drunk Score: "+ drunkennessScoreInt + ", Values of preferred lists: " + "Wine " + preferredWines + ", Beer " + preferredBeers + ", Aperitif " + preferredAperitif + ", Cocktail " + preferredCocktails + ", Shots " + preferredShots + ", Hot Drink " + preferredHots);
            drink2 = new String[]{"Drink 2","N.a.","N.a.","0","0"};
        }

        //TODO Video: hier gewünschte Getränke einfügen
        drink1Name.setText(drink1[0]);
        drink1Amount.setText(drink1[3]+ "l");
        drink1Image.setImageDrawable(drink1Img);

        drink2Title.setText(drink2[0]);
        drink2Title.setText(drink2[0]);
        drink2Amount.setText(drink2[3]+ "l");
        drink2Image.setImageDrawable(drink2Img);

        UserData.RECOMMENDATION = new ArrayList<>();
        UserData.RECOMMENDATION.add(drink1);
        UserData.RECOMMENDATION.add(drink2);

        DataHandler.storeSettings(contextActivity);
        drunkennessScoreTxt.setText(getDrunkennessScoreText(drunkennessScoreInt));

        //Only show result of text analysis if a message was entered
        if (textAnalysisConducted) {
            TextMessage textMessage = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE;
            String receiver = textMessage.getRecipient();
            messageReceiver.setText(receiver);

            boolean safeToText = ((HomeActivity) contextActivity).calculateSafeToText(drunkennessScoreInt, textMessage);

            //TODO Video hier safe to text anpassen --> Testen weil dann chat historie eventuell nicht passt
            if (safeToText) {
                safeToTextValue.setText("safe to text");
                copyMessageContent.setVisibility(View.VISIBLE);
            } else {
                safeToTextValue.setText("NOT safe to text");
                copyMessageContent.setVisibility(View.GONE);
            }
        } else {
            textAnalysisTitle.setVisibility(View.GONE);
            textAnalysisResult.setVisibility(View.GONE);
            textAnalysisTitle.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return layout;
    }

    public Drawable getImageforDrink(String type){
        Drawable img = getResources().getDrawable(R.drawable.defaultdrink, contextActivity.getTheme());
        switch (type) {
            case "Red":
                img = getResources().getDrawable(R.drawable.redwine, contextActivity.getTheme());
                break;
            case "White":
                img = getResources().getDrawable(R.drawable.whitewine, contextActivity.getTheme());
                break;
            case "Sparkling":
                img = getResources().getDrawable(R.drawable.sparklingwine, contextActivity.getTheme());
                break;
            case "WineMix":
                img = getResources().getDrawable(R.drawable.mixwine, contextActivity.getTheme());
                break;
            case "Beer":
                img = getResources().getDrawable(R.drawable.beer, contextActivity.getTheme());
                break;
            case "BeerMix":
                img = getResources().getDrawable(R.drawable.mixbeer, contextActivity.getTheme());
                break;
            case "Aperitif":
                img = getResources().getDrawable(R.drawable.aperitif, contextActivity.getTheme());
                break;
            case "Longdrink":
                img = getResources().getDrawable(R.drawable.longdrink, contextActivity.getTheme());
                break;
            case "Cocktail":
                img = getResources().getDrawable(R.drawable.cocktail, contextActivity.getTheme());
                break;
            case "Shot":
                img = getResources().getDrawable(R.drawable.shot, contextActivity.getTheme());
                break;
            case "Hot":
                img = getResources().getDrawable(R.drawable.hotdrink, contextActivity.getTheme());
                break;
            case "TaZwiWa":
                img = getResources().getDrawable(R.drawable.tazwiwa, contextActivity.getTheme());
                break;
            case "GoHome":
                img = getResources().getDrawable(R.drawable.gohome, contextActivity.getTheme());
                break;
        }
        return img;
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
    public double calculateTextScore(double errorBase, double errorChall, double timeBase, double timeChall){
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
        drunkScore= drunkScore+ calculateTextScore(mean_error_baseline,mean_error_challenge,mean_completiontime_baseline,mean_completiontime_challenge);

        //Adding Selfie Factor
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
        //TODO video: hier drunkennessScore eingeben den wir wollen
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