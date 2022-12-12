package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.TEXT_MESSAGE_LIST;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DrunkometerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunkometer);

        // Setup start view of drunkomenter
        FragmentDrunkometerStart fragmentDrunkometerStart = new FragmentDrunkometerStart();
        loadFragment(fragmentDrunkometerStart, "fragmentDrunkometerStart");
    }

    /**
     * Load fragment into fragment container
     * @param frag The fragment to be loaded
     * @param tag The tag of the fragment to be loaded
     */
    public void loadFragment(android.app.Fragment frag, String tag)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container_drunkometer);

        if(fragment == null)
        {
            ft.add(R.id.fragment_container_drunkometer, frag, tag);
        } else
        {
            ft.replace(R.id.fragment_container_drunkometer, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    /**
     * Start drunkometer analysis
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startDrunkometerAnalysis(View view) {
        FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
        loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
    }

    /**
     * Continue to typing challenge
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startTypingChallenge(View view) {
        FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
        loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");

    }

    /**
     * Continue to drunk selfie fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void finishTypingChallenge(View view) {
        UserData.MEAN_ERROR_CHALLENGE = UserData.calculateMean("challenge", "error");
        UserData.MEAN_COMPLETIONTIME_CHALLENGE = UserData.calculateMean("challenge", "completiontime");
        Log.d("D-O-M challenge error", String.valueOf(UserData.MEAN_ERROR_CHALLENGE));
        Log.d("D-O-M challenge time", String.valueOf(UserData.MEAN_COMPLETIONTIME_CHALLENGE));

        // TODO: load drunk selfie fragment

    }

    /**
     * Analyse the provided text message with NLP to determine the sentiment, and based on this and
     * the drunkenness score, continue to next fragment that suggests an action (or simply add information
     * to Kathis recommender activity?)
     * @param view
     */
    public void analyzeTextMessage(View view) {
        EditText recipientInput = view.findViewById(R.id.textMessageRecipientInput);
        EditText textMessageInput = view.findViewById(R.id.textMessageTextInput);
        Date date = Calendar.getInstance().getTime();

        // TODO trigger NLP
        String sentimentAnalysis = "result of nlp";

        // TODO trigger result fragment with sentiment analysis

        // Save text message to list of text messages associated with the user
        TextMessage textMessage = new TextMessage(recipientInput.getText().toString(), textMessageInput.getText().toString(), sentimentAnalysis, date);
        TEXT_MESSAGE_LIST.add(textMessage);
    }
}