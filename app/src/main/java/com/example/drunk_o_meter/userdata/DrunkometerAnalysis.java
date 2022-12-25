package com.example.drunk_o_meter.userdata;

import android.graphics.Bitmap;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;

/**
 * The DrunkometerAnalysis class groups all data that is gathered during one analysis flow
 */
public class DrunkometerAnalysis {

    public static ArrayList<TypingSample> TYPING_CHALLENGE;
    // Typing Challenge
    public static double MEAN_ERROR_CHALLENGE;
    public static double MEAN_COMPLETIONTIME_CHALLENGE;

    // TODO: Selfie - bitmap image and drunkeness score?
    public static Bitmap SELFIE;
    public static double SELFIE_DRUNKENNESS_SCORE;

    // Text Message
    public static TextMessage TEXT_MESSAGE;
}
