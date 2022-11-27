package com.example.drunk_o_meter.userdata;

import com.example.drunk_o_meter.userdata.UserData;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataHandler {

    public static final String DATA_PATH = "userdata.json";

    /**
     * Store the current state of the settings into a json file.
     * @param context The context the private file is related to.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void storeSettings(Context context) {
        JSONObject settings = new JSONObject();
        try {

            // store username
            settings.put("username", UserData.USERNAME);

            Log.d("DRUNK-O-METER UserData", "Store data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            LocalStorageUtils.saveFile(context, DATA_PATH, settings.toString());
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
