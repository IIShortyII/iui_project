package com.example.drunk_o_meter.userdata;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.typingChallenge.TypingSample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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

                    Log.d("D-O-M mean error", String.valueOf(UserData.MEAN_ERROR_BASELINE));
                    Log.d("D-O-M mean time", String.valueOf(UserData.MEAN_COMPLETIONTIME_BASELINE));
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
