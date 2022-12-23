package com.example.drunk_o_meter.userdata;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;

/**
 * The DrunkometerAnalysis class groups all data that is gathered during one analysis flow
 */
public class DrunkometerAnalysis {

    // Typing Challenge
    public static ArrayList<TypingSample> TYPING_CHALLENGE = new ArrayList<>();
    public static double MEAN_ERROR_CHALLENGE;
    public static double MEAN_COMPLETIONTIME_CHALLENGE;

    // TODO: Selfie - bitmap image and drunkeness score?

    // Text Message
    public static TextMessage TEXT_MESSAGE;
}
