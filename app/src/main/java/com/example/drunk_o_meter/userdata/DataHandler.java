package com.example.drunk_o_meter.userdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.recommender.DrinkType;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DataHandler {

    public static final String DATA_PATH = "userdata.json";

    /**
     * Store the current state of the settings into a json file.
     * @param context The context the private file is related to.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void storeSettings(Context context) {
        JSONObject userData = new JSONObject();
        try {

            // store user information
            userData.put("ageCheck", UserData.AGE_CHECK);
            userData.put("username", UserData.USERNAME);
            userData.put("weight", UserData.WEIGHT);
            userData.put("gender", UserData.GENDER);

            // store gram of alcohol
            userData.put("gramOfAlcohol", UserData.GRAM_OF_ALCOHOL);

            // store recommendations
            storeRecommendations(userData);

            // store typing challenge baseline
            storeTypingChallenge(userData);

            // store drunkometer analysis list
           storeDrunkometerAnalysisList(userData);

            // store Drink preferences
            storeDrinkPreferences(userData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            LocalStorageUtils.saveFile(context, DATA_PATH, userData.toString());
            Log.d("D-O-M store", "Store data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Store drunkometer analysis list data in local storage
     * @param userData the object to be stored in local storage
     * @throws JSONException
     */
    private static void storeDrunkometerAnalysisList(JSONObject userData) throws JSONException {
        JSONArray drunkometerAnalysisList = new JSONArray();
        for(int i=0; i< UserData.DRUNKOMETER_ANALYSIS_LIST.size(); i++) {
            DrunkometerAnalysis drunkometerAnalysis = UserData.DRUNKOMETER_ANALYSIS_LIST.get(i);

            JSONObject JSONdrunkometerAnalysis = new JSONObject();

            // Typing Challenge
            Double mean_error_challenge = drunkometerAnalysis.MEAN_ERROR_CHALLENGE;

            Double mean_completiontime_challenge = drunkometerAnalysis.MEAN_COMPLETIONTIME_CHALLENGE;
            // Selfie
            String selfie = getStringFromBitmap(drunkometerAnalysis.SELFIE);
            double selfie_drunkenness_score = drunkometerAnalysis.SELFIE_DRUNK_PREDICTION;

            // FINAL SCORE
            int drunkenness_score = drunkometerAnalysis.DRUNKENNESS_SCORE;

            // OPTIONAL Text Message
            if (drunkometerAnalysis.TEXT_MESSAGE != null){
                TextMessage textMessage = drunkometerAnalysis.TEXT_MESSAGE;
                String recipient = textMessage.getRecipient();
                String message = textMessage.getMessage();
                String sentimentAnalysis = textMessage.getSentimentAnalysis();
                String date = textMessage.getDate().toString(); // Format: Mon Dec 12 16:01:25 GMT 2022
                JSONObject JSONTextMessage = new JSONObject();
                JSONTextMessage.put("recipient", recipient).put("message", message).put("sentimentAnalysis", sentimentAnalysis).put("date", date);
                JSONdrunkometerAnalysis.put("drunkenness_score", drunkenness_score).put("mean_error_challenge", mean_error_challenge).put("mean_completiontime_challenge", mean_completiontime_challenge).put("selfie", selfie).put("selfie_drunkenness_score", selfie_drunkenness_score).put("text_message", JSONTextMessage);
            } else {
                JSONdrunkometerAnalysis.put("drunkenness_score", drunkenness_score).put("mean_error_challenge", mean_error_challenge).put("mean_completiontime_challenge", mean_completiontime_challenge).put("selfie", selfie).put("selfie_drunkenness_score", selfie_drunkenness_score);
            }
            drunkometerAnalysisList.put(i, JSONdrunkometerAnalysis);
        }

        userData.put("drunkometer_analysis_list", drunkometerAnalysisList);
    }

    /**
     * Store typing challenge data in local storage
     * @param userData the object to be stored in local storage
     * @throws JSONException
     */
    private static void storeTypingChallenge(JSONObject userData) throws JSONException {
        JSONArray sampleArray = new JSONArray();
        for(int i=0; i< UserData.BASELINE_TYPING_CHALLENGE.size(); i++) {
            TypingSample sample = UserData.BASELINE_TYPING_CHALLENGE.get(i);
            String text = sample.getText();
            String input = sample.getInput();
            double error = sample.getError();
            long time = sample.getTime();

            JSONObject JSONsample = new JSONObject();
            JSONsample.put("text", text).put("input", input).put("error", error).put("time", time);
            sampleArray.put(i, JSONsample);
        }

        userData.put("baseline_typing_challenge", sampleArray);
    }

    /**
     * Store recommendations data in local storage
     * @param userData the object to be stored in local storage
     * @throws JSONException
     */
    private static void storeRecommendations(JSONObject userData) throws JSONException {
        JSONArray recommendationsArray = new JSONArray();
        for(int i=0; i< UserData.RECOMMENDATION.size(); i++) {
            String[] recommendation = UserData.RECOMMENDATION.get(i);
            JSONArray recommendationStringArray = new JSONArray();
            for(int j =0; j < recommendation.length; j++){
                recommendationsArray.put(j, recommendation[j]);
            }
            recommendationsArray.put(i, recommendation);
        }
        userData.put("recommendations", recommendationsArray);
    }

    /**
     * Store drink preferences data in local storage
     * @param userData the object to be stored in local storage
     * @throws JSONException
     */
    private static void storeDrinkPreferences(JSONObject userData) throws JSONException {
        ArrayList<DrinkType> drinkKeys = new ArrayList<>(UserData.DRINKS.keySet());
        for(DrinkType key : drinkKeys){
            String reference = "drink_preferences_" + key.toString();
            userData.put(reference, new JSONArray(UserData.DRINKS.get(key)));
            Log.d("D-O-M DRINK store", String.valueOf(key) + " " + String.valueOf(UserData.DRINKS.get(key).size()));
        }
    }


    /**
     * Load the stored data from a json file.
     *
     * @param context The context the private file is related to.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void loadSettings(Context context) throws IOException {
        try {
            String json = LocalStorageUtils.loadFile(context, DATA_PATH);
            if (json.length() != 0){
                JSONObject obj = new JSONObject(json);

                // fetch stored user data
                if (obj.has("username")) {
                    UserData.AGE_CHECK = obj.getBoolean("ageCheck");
                    UserData.USERNAME = obj.getString("username");
                    UserData.WEIGHT = obj.getInt("weight");
                    UserData.GENDER = Gender.valueOf(obj.getString("gender"));
                }

                // Load grams of alcohol
                if (obj.has("gramofAlcohol")){
                    UserData.GRAM_OF_ALCOHOL = obj.getDouble("gramofAlcohol");
                }

                // Load recommendations
                if(obj.has("recommendations")){
                    loadRecommendations(obj);
                }

                // fetch stored baseline typing samples
                if (obj.has("baseline_typing_challenge")) {
                    loadTypingChallenge(obj);
                }

                // load all drunkometer analysis objects into app
                if (obj.has("drunkometer_analysis_list")){
                    loadDrunkAnalysisList(obj);
                }

                // load drink preferences
                loadDrinkPreferences(obj);

                Log.d("D-O-M load", "Loaded User Data");
            }

        } catch (FileNotFoundException e) {
            Log.d("D-O-M UserData", "File not found");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("D-O-M UserData", "Loading User Data Failed");
            Log.d("D-O-M UserData", e.getMessage());
        }
    }

    /**
     * Load recommendations data from local storage
     * @param obj The obj retrieved from the storage
     * @throws JSONException
     */
    private static void loadRecommendations(JSONObject obj) throws JSONException {
        JSONArray recommendationsListJSONArray = obj.getJSONArray("recommendations");
        UserData.RECOMMENDATION = new ArrayList<>();
        for (int i = 0; i < recommendationsListJSONArray.length(); i++) {
            JSONArray recommendationJSONArray = (JSONArray) recommendationsListJSONArray.get(i);
            String[] recommendation = new String[5]; // TODO: if drink String[] length changes, adapt this one
            for (int j = 0; j < recommendationJSONArray.length(); j++) {
                recommendation[j] = recommendationJSONArray.getString(j);
            }
            UserData.RECOMMENDATION.add(recommendation);
        }
    }

    /**
     * Load typing challenge data from local storage
     * @param obj The obj retrieved from the storage
     * @throws JSONException
     */
    private static void loadTypingChallenge(JSONObject obj) throws JSONException {
        JSONArray baseline = obj.getJSONArray("baseline_typing_challenge");
        UserData.BASELINE_TYPING_CHALLENGE = new ArrayList<>();
        for(int i=0; i<baseline.length(); i++) {
            JSONObject JSONsample = baseline.getJSONObject(i);
            String text = JSONsample.getString("text");
            String input = JSONsample.getString("input");
            double error = JSONsample.getDouble("error");
            long time = JSONsample.getLong("time");
            TypingSample sample =  new TypingSample(text, input, time, error);
            UserData.BASELINE_TYPING_CHALLENGE.add(sample);
        }
        // Calculate mean completiontime and error for baseline typing samples
        UserData.MEAN_COMPLETIONTIME_BASELINE = UserData.calculateMean( "completiontime", UserData.BASELINE_TYPING_CHALLENGE);
        UserData.MEAN_ERROR_BASELINE = UserData.calculateMean("error", UserData.BASELINE_TYPING_CHALLENGE);
    }

    /**
     * Load drunk analysis list data from local storage
     * @param obj The obj retrieved from the storage
     * @throws JSONException
     */
    private static void loadDrunkAnalysisList(JSONObject obj) throws JSONException {
        JSONArray drunkometerAnalysisList = obj.getJSONArray("drunkometer_analysis_list");
        UserData.DRUNKOMETER_ANALYSIS_LIST = new ArrayList<>();
        Log.d("D-O-M loader list length", String.valueOf(drunkometerAnalysisList.length()));
        for(int i=0; i<drunkometerAnalysisList.length(); i++) {

            DrunkometerAnalysis drunkometerAnalysis = new DrunkometerAnalysis();
            JSONObject JSONdrunkometerAnalysis = drunkometerAnalysisList.getJSONObject(i);

            // DRUNKENNESS SCORE
            drunkometerAnalysis.DRUNKENNESS_SCORE = JSONdrunkometerAnalysis.getInt("drunkenness_score");

            // Typing Sample Error and Completiontime
            drunkometerAnalysis.MEAN_ERROR_CHALLENGE = JSONdrunkometerAnalysis.getDouble("mean_error_challenge");
            drunkometerAnalysis.MEAN_COMPLETIONTIME_CHALLENGE = JSONdrunkometerAnalysis.getDouble("mean_completiontime_challenge");
            Log.d("D-O-M loader ct", String.valueOf(drunkometerAnalysis.MEAN_COMPLETIONTIME_CHALLENGE));
            // Selfie bitmap and Drunkenness Score
            drunkometerAnalysis.SELFIE = getBitmapFromString(JSONdrunkometerAnalysis.getString("selfie"));
            drunkometerAnalysis.SELFIE_DRUNK_PREDICTION = JSONdrunkometerAnalysis.getDouble("selfie_drunkenness_score");
            Log.d("D-O-M loader has text message", String.valueOf(JSONdrunkometerAnalysis.has("text_message")));
            // OPTIONAL text message object
            if (JSONdrunkometerAnalysis.has("text_message")) {

                JSONObject JSONtextMessage = JSONdrunkometerAnalysis.getJSONObject("text_message");
                String recipient = JSONtextMessage.getString("recipient");
                String message = JSONtextMessage.getString("message");
                String sentimentAnalysis = JSONtextMessage.getString("sentimentAnalysis");
                String dateString = JSONtextMessage.getString("date");

                // Date Format: Mon Dec 12 16:01:25 GMT 2022
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                Date date = new Date();
                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                drunkometerAnalysis.TEXT_MESSAGE = new TextMessage(recipient, message, sentimentAnalysis, date);
            }

            UserData.DRUNKOMETER_ANALYSIS_LIST.add(drunkometerAnalysis);
            Log.d("D-O-M loader success", String.valueOf(UserData.DRUNKOMETER_ANALYSIS_LIST.size()));
        }
        }

    // Retrieve drink preferences and store in JSON object;
    private static void loadDrinkPreferences(JSONObject userData) throws JSONException {
        UserData.DRINKS = new HashMap<>();
        DrinkType[] drinkKeys = DrinkType.values();

        for(DrinkType key : drinkKeys) {
            String reference = "drink_preferences_" + key.toString();
            ArrayList<String> drinkArrayList = new ArrayList<>();
            if (userData.has(reference)) {
                JSONArray drinkJSONArray = userData.getJSONArray(reference);
                if (drinkJSONArray.length() > 0) {
                    for (int i = 0; i < drinkJSONArray.length(); i++) {
                        drinkArrayList.add((String) drinkJSONArray.get(i));
                    }
                }
                   }
            UserData.DRINKS.put(DrinkType.valueOf(key.toString()), drinkArrayList);
            Log.d("D-O-M DRINKS load", String.valueOf(UserData.DRINKS.get(key)));

        }

    }


    // https://stackoverflow.com/questions/30818538/converting-json-object-with-bitmaps

    /*
     * This functions converts Bitmap picture to a string which can be
     * JSONified.
     * */
    private static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    /*
     * This Function converts the String back to Bitmap
     * */
    private static Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
