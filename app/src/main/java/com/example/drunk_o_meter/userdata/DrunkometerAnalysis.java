package com.example.drunk_o_meter.userdata;

import android.graphics.Bitmap;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import java.util.ArrayList;

/**
 * The DrunkometerAnalysis class groups all data that is gathered during one analysis flow
 */
public class DrunkometerAnalysis {

    public ArrayList<TypingSample> TYPING_CHALLENGE;
    // Typing Challenge
    public double MEAN_ERROR_CHALLENGE;
    public double MEAN_COMPLETIONTIME_CHALLENGE;

    public Bitmap SELFIE;
    public double SELFIE_DRUNKENNESS_SCORE; // TODO safe drunkenness score

    // Text Message
    public TextMessage TEXT_MESSAGE = null;
}
