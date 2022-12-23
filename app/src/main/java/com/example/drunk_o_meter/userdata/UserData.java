package com.example.drunk_o_meter.userdata;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserData {

    /**
     * DrunkometerAnalysis object that holds the most recent analysis data from the typing challenge,
     * the selfie and its drunkeness score, and an optional text message with sentiment analysis.
     */
    public static DrunkometerAnalysis DRUNKOMETER_ANALYSIS = new DrunkometerAnalysis();

    /**
     * List of all text messages the user has provided for sentiment analysis
     */

    //TODO: @Kathi das ist die Liste mit allen TextMessages die bei App Start aus den Local
    //      Storage geholt werden und dann im Archiv angezeigt werden müssen. Die TextMessage
    //      Klasse liegt im "nlp" Order und ich denke alle attribute einer TextMesssage sollten
    //      pro Nachricht im Archiv angezeigt werden :)
    public static ArrayList<TextMessage> TEXT_MESSAGE_LIST = new ArrayList<>();

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
                sampleSize = DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE.size();
                for(int i =0; i< sampleSize; i++) {
                    if (variable.equals("error")) {
                        mean = mean + DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE.get(i).getError();
                    } else if (variable.equals("completiontime")) {
                        mean = mean + DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE.get(i).getTime();
                    }
                }
            }

        return mean / sampleSize;
    }



}
