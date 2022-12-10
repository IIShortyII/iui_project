package com.example.drunk_o_meter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;

public class DrunkometerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunkometer);
    }

    /**
     * Clear local storage for debugging purposes and go back to start
     * @param view the current activity context
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void clearLocalStorage(View view) {
        UserData.USERNAME = "";
        UserData.BASELINE_TYPING_CHALLENGE = new ArrayList<>();
        DataHandler.storeSettings(this);

        Intent intent = new Intent(DrunkometerActivity.this, MainActivity.class);
        DrunkometerActivity.this.startActivity(intent);
        Log.d("D-O-M", "internal storage cleared");
    }
}