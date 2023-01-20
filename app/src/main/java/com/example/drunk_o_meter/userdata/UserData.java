package com.example.drunk_o_meter.userdata;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.recommender.DrinkType;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class UserData {


    public static DrunkometerAnalysis DRUNKOMETER_ANALYSIS = new DrunkometerAnalysis();

    public static ArrayList<DrunkometerAnalysis> DRUNKOMETER_ANALYSIS_LIST = new ArrayList<>();

    /**
     * List of all baseline typing samples
     */
    public static ArrayList<TypingSample> BASELINE_TYPING_CHALLENGE = new ArrayList<TypingSample>();

    public static double MEAN_ERROR_BASELINE;

    public static double MEAN_COMPLETIONTIME_BASELINE;

    /**
     * Check minimal age of 18
     */
    public static boolean AGE_CHECK = false;

    /**
     * Username
     */
    public static String USERNAME ="";

    /**
     * Weight
     */
    public static int WEIGHT = 0;

    /**
     * Biological gender
     */
    public static Gender GENDER = Gender.FEMALE;

    /**
     * Drink Preferences
     * DRINKS.get(DrinkType.WINE)
     */
    public static HashMap<DrinkType, ArrayList<String>> DRINKS = new HashMap<DrinkType, ArrayList<String>>()
    {{
        put(DrinkType.BEER, new ArrayList<>());
        put(DrinkType.WINE, new ArrayList<>());
        put(DrinkType.COCKTAIL, new ArrayList<>());
        put(DrinkType.SHOT, new ArrayList<>());
        put(DrinkType.HOT, new ArrayList<>());
    }};

    /**
     * Calculate the mean value of the provided variable
     * @param variable the variable used to calculate the mean - error or completiontime
     * @return the mean of the provided variable
     */
    public static double calculateMean(String variable, ArrayList<TypingSample> typingSamples){
        double mean = 0.0;
        int sampleSize = typingSamples.size();

                for(int i =0; i< sampleSize; i++) {
                    if (variable.equals("error")) {
                        mean = mean + typingSamples.get(i).getError();
                    } else if (variable.equals("completiontime")) {
                        mean = mean + typingSamples.get(i).getTime();
                    }
                }


        return mean / sampleSize;
    }

}
