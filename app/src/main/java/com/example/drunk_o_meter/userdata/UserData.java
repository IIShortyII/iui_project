package com.example.drunk_o_meter.userdata;

import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserData {


    /**
     * List of all baseline typing samples
     */
    public static ArrayList<TypingSample> BASELINE_TYPING_CHALLENGE = new ArrayList<TypingSample>();

    public static double MEAN_ERROR_BASELINE;

    public static double MEAN_COMPLETIONTIME_BASELINE;

    /**
     * Username
     */
    public static String USERNAME ="";

    /**
     * List of all current typing challenge samples
     */
    public static Set<TypingSample> TYPING_CHALLENGE = new HashSet<TypingSample>();

    /**
     * Calculate the mean value of the provided variable
     * @param type the variable used to calculate the mean
     * @return the mean of the provided variable
     */
    public static double calculateMean(String type){
        double mean = 0.0;
        for(int i =0; i< BASELINE_TYPING_CHALLENGE.size(); i++){
            if (type.equals("error")){
                mean = mean + BASELINE_TYPING_CHALLENGE.get(i).getError();
            } else if (type.equals("completiontime")){
                mean = mean + BASELINE_TYPING_CHALLENGE.get(i).getTime();
            }
        }
        return mean / BASELINE_TYPING_CHALLENGE.size();
    }



}
