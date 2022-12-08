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

    /**
     * Username
     */
    public static String USERNAME ="";

    /**
     * List of all current typing challenge samples
     */
    public static Set<TypingSample> TYPING_CHALLENGE = new HashSet<TypingSample>();


}
