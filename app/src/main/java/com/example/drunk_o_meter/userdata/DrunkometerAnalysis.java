package com.example.drunk_o_meter.userdata;

import android.graphics.Bitmap;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;

/**
 * The DrunkometerAnalysis class groups all data that is gathered during one analysis flow
 */
public class DrunkometerAnalysis {

    // FINAL SCORE
    public int DRUNKENNESS_SCORE;

    public ArrayList<TypingSample> TYPING_CHALLENGE;
    // Typing Challenge
    public double MEAN_ERROR_CHALLENGE;
    public double MEAN_COMPLETIONTIME_CHALLENGE;

    public Bitmap SELFIE;
    public double SELFIE_DRUNK_PREDICTION;

    // Text Message
    public TextMessage TEXT_MESSAGE = null;
}
