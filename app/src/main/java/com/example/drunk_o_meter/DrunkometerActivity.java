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

import com.example.drunk_o_meter.nlp.FragmentTextMessageInput;
import com.example.drunk_o_meter.nlp.FragmentTextMessageIntro;
import com.example.drunk_o_meter.nlp.NlpPipeline;
import com.example.drunk_o_meter.nlp.Sentiment;
import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.DrunkometerAnalysis;
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

        // Create new DrunkometerAnalysis Object for this analysis
        UserData.DRUNKOMETER_ANALYSIS = new DrunkometerAnalysis();
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
        UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE = UserData.calculateMean("challenge", "error");
        UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE = UserData.calculateMean("challenge", "completiontime");
        Log.d("D-O-M challenge error", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE));
        Log.d("D-O-M challenge time", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE));

        // TODO: load drunk selfie fragment (for debugging, we skip the selfie and go to text message immediately)
        // the finishSelfie(view) should be called after selfie part has been completed
        finishSelfie(view);
    }

    /**
     * Continue to text message fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void finishSelfie(View view) {
        FragmentTextMessageIntro fragmentTextMessageIntro = new FragmentTextMessageIntro();
        loadFragment(fragmentTextMessageIntro, "fragmentTextMessageIntro");
    }

    /**
     * Continue to text message fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToTextMessageInput(View view) {
        FragmentTextMessageInput fragmentTextMessageInput = new FragmentTextMessageInput();
        loadFragment(fragmentTextMessageInput, "fragmentTextMessageInput");
    }

    /**
     * Continue to recommendation activity
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void skipTextMessage(View view) {
        // TODO @Kathi: Go to recommender activity
    }


    /**
     * Analyse the provided text message with NLP to determine the sentiment, and based on this and
     * the drunkenness score, continue to next fragment that suggests an action (or simply add information
     * to Kathis recommender activity?)
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void analyzeTextMessage(View view) {
        EditText recipientInput = this.findViewById(R.id.textMessageRecipientInput);
        EditText textMessageInput = this.findViewById(R.id.textMessageTextInput);

        // Only proceed if user has provided both recipient name and text message
        if(recipientInput.getText().toString().length() != 0 && textMessageInput.getText().toString().length() !=0){
            Date date = Calendar.getInstance().getTime(); // Format: Fri Dec 23 10:28:05 GMT+01:00 2022

            NlpPipeline nlpPipeline = new NlpPipeline();
            nlpPipeline.init();
            Sentiment sentiment = nlpPipeline.estimatingSentiment(textMessageInput.getText().toString());

            // Save text message to list of text messages associated with the user
            TextMessage textMessage = new TextMessage(recipientInput.getText().toString(), textMessageInput.getText().toString(), sentiment.toString(), date);
            TEXT_MESSAGE_LIST.add(textMessage);
            DataHandler.storeSettings(this);

            // Add text message to current DRUNKOMETER_ANALYSIS object
            UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE = textMessage;
        }


    }
}