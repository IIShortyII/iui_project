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
    public static ArrayList<TypingSample> TYPING_CHALLENGE = new ArrayList<>();

    public static double MEAN_ERROR_CHALLENGE;

    public static double MEAN_COMPLETIONTIME_CHALLENGE;

    /**
     * Calculate the mean value of the provided variable
     * @param type the type of calculation needed - baseline or challenge
     * @param variable the variable used to calculate the mean - error or completiontime
     * @return the mean of the provided variable
     */
    public static double calculateMean(String type, String variable){
        double mean = 0.0;
        int sampleSize = 0;

            // calculate mean of baseline typing samples
            if (type.equals("baseline")){
                sampleSize = BASELINE_TYPING_CHALLENGE.size();
                for(int i =0; i< sampleSize; i++) {
                    if (variable.equals("error")) {
                        mean = mean + BASELINE_TYPING_CHALLENGE.get(i).getError();
                    } else if (variable.equals("completiontime")) {
                        mean = mean + BASELINE_TYPING_CHALLENGE.get(i).getTime();
                    }
                }

                // caluclate eman of typing challenge samples
            } else if (type.equals("challenge")){
                sampleSize = TYPING_CHALLENGE.size();
                for(int i =0; i< sampleSize; i++) {
                    if (variable.equals("error")) {
                        mean = mean + TYPING_CHALLENGE.get(i).getError();
                    } else if (variable.equals("completiontime")) {
                        mean = mean + TYPING_CHALLENGE.get(i).getTime();
                    }
                }
            }

        return mean / sampleSize;
    }



}
