package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.DRUNKOMETER_ANALYSIS_LIST;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    //TODO: add tab for past recommendations?

    /**
     * Main UI component for the navigation bar
     */
    BottomNavigationView bottomNavigationView;

    /**
     * The tab, which shows the main functionality to find the (drinks) recommendation
     */
    DrunkometerFragment drunkometerFragment;

    /**
     * The tab, which shows the chats that were not send in combination with the selfie taken in the same challenge --> also move safe-to-text challenge here?
     */
    ChatsFragment chatsFragment;


    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Bottom Nav", "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Log.d("Bottom Nav", "found");
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.drunkometer);
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

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.flFragment);

        if(fragment == null)
        {
            ft.add(R.id.flFragment, frag, tag);
        } else
        {
            ft.replace(R.id.flFragment, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    /**
     *
     * Setup navigation view (=tabs)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("Bottom Nav", "nav item selected");
        //create fragments
        drunkometerFragment = new DrunkometerFragment();
        chatsFragment = new ChatsFragment();

        switch (item.getItemId()) {
            case R.id.drunkometer:
                getFragmentManager().beginTransaction().replace(R.id.flFragment, drunkometerFragment).commit();
                return true;

            case R.id.chats:
                getFragmentManager().beginTransaction().replace(R.id.flFragment, chatsFragment).commit();
                return true;
        }
        return false;
    }

    /**
     * Start drunkometer analysis
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startDrunkometerAnalysis(View view) {
        // Create new DrunkometerAnalysis Object for this analysis
        UserData.DRUNKOMETER_ANALYSIS = new DrunkometerAnalysis();

        FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
        loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
        bottomNavigationView.setVisibility(View.GONE);
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
        UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE = UserData.calculateMean("error",UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE = UserData.calculateMean("completiontime", UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        Log.d("D-O-M challenge error", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE));
        Log.d("D-O-M challenge time", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE));

            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    File.separator + "drunkometer_selfie.jpeg";
            Intent cameraIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = new File(imagePath);
            Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
        Bitmap compressed = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

        UserData.DRUNKOMETER_ANALYSIS.SELFIE = compressed;
        finishSelfie();
    }



    /**
     * Continue to text message fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void finishSelfie() {
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

        addDrunkoMeterAnalysis();
        DataHandler.storeSettings(this);
        /*RecommendationFragment recommendationFragment = new RecommendationFragment();
        loadFragment(recommendationFragment, "recommendationFragment");*/
        // TODO @Kathi: Go to recommender activity + bottomNavigationView.setVisibility(View.VISIBLE);
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
            // Add text message to current DRUNKOMETER_ANALYSIS object
            UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE = textMessage;
            addDrunkoMeterAnalysis();
            DataHandler.storeSettings(this);
        }
    }

    private void addDrunkoMeterAnalysis() {
        DrunkometerAnalysis newAnalysis = new DrunkometerAnalysis();
        newAnalysis.MEAN_COMPLETIONTIME_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE;
        newAnalysis.MEAN_ERROR_CHALLENGE = UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE;
        newAnalysis.SELFIE = UserData.DRUNKOMETER_ANALYSIS.SELFIE;
        newAnalysis.SELFIE_DRUNKENNESS_SCORE = UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNKENNESS_SCORE;
        if (UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE != null){
            newAnalysis.TEXT_MESSAGE = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE;
        }
        DRUNKOMETER_ANALYSIS_LIST.add(newAnalysis);
    }
}