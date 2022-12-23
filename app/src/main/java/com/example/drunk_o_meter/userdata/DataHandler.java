package com.example.drunk_o_meter.userdata;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.TypingSample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

            // store username
            userData.put("username", UserData.USERNAME);

            // store typing challenge baseline
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

            // store text message list
            JSONArray textMessageList = new JSONArray();
            for(int i=0; i< UserData.TEXT_MESSAGE_LIST.size(); i++) {
                TextMessage textMessage = UserData.TEXT_MESSAGE_LIST.get(i);
                String recipient = textMessage.getRecipient();
                String message = textMessage.getMessage();
                String sentimentAnalysis = textMessage.getSentimentAnalysis();
                String date = textMessage.getDate().toString(); // Format: Mon Dec 12 16:01:25 GMT 2022

                JSONObject JSONTextMessage = new JSONObject();
                JSONTextMessage.put("recipient", recipient).put("message", message).put("sentimentAnalysis", sentimentAnalysis).put("date", date);
                textMessageList.put(i, JSONTextMessage);
            }

            userData.put("text_message_list", textMessageList);


            Log.d("DRUNK-O-METER UserData", "Store data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            LocalStorageUtils.saveFile(context, DATA_PATH, userData.toString());
        } catch (IOException e) {
            e.printStackTrace();
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

                // fetch stored username
                if (obj.has("username")) {
                    UserData.USERNAME = obj.getString("username");
                }

                // fetch stored baseline typing samples
                if (obj.has("baseline_typing_challenge")) {
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
                    UserData.MEAN_COMPLETIONTIME_BASELINE = UserData.calculateMean("baseline", "completiontime");
                    UserData.MEAN_ERROR_BASELINE = UserData.calculateMean("baseline", "error");

                }

                if (obj.has("text_message_list")){
                    JSONArray textMessageList = obj.getJSONArray("text_message_list");
                    UserData.TEXT_MESSAGE_LIST = new ArrayList<>();
                    for(int i=0; i<textMessageList.length(); i++) {
                        JSONObject JSONtextMessage = textMessageList.getJSONObject(i);
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
                        TextMessage textMessage = new TextMessage(recipient, message, sentimentAnalysis, date);
                        UserData.TEXT_MESSAGE_LIST.add(textMessage);
                    }

                }

                Log.d("DRUNK-O-METER UserData", "Loaded User Data");
            }

        } catch (FileNotFoundException e) {
            Log.d("DRUNK-O-METER UserData", "File not found");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DRUNK-O-METER UserData", "Loading User Data Failed");
            Log.d("DRUNK-O-METER UserData", e.getMessage());
        }
    }

}
