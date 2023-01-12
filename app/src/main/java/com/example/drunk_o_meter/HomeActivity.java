package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.DRUNKOMETER_ANALYSIS_LIST;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.drunk_o_meter.chat_list.ChatDetailViewFragment;
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

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener, SensorEventListener {
    //TODO: add tab for past recommendations?

    /**
     * Main UI component for the navigation bar
     */
    BottomNavigationView bottomNavigationView;

    private SensorManager sensorManager;
    Sensor gyroscope;
    int penaltypoint = 0;
    TextView sensorname, xValue, yValue, zValue, penalty;

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.drunkometer);


        Log.d("Gyroscope", "onCreate: Initializing Sensor Services");
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorname.setText(""+ gyroscope.getName());
        sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("Gyroscope", "onCreate: Registered accelerometer listener");

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
        //create fragments
        DrunkometerFragment drunkometerFragment = new DrunkometerFragment();
        ChatsFragment chatsFragment = new ChatsFragment();

        switch (item.getItemId()) {
            case R.id.drunkometer:
                loadFragment(drunkometerFragment, "drunkometerFragment");
                return true;

            case R.id.chats:
                loadFragment(chatsFragment, "chatsFragment");
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
        //TODO @Dennis: hier BEGINNT die typing challenge, also sollte die weaving analysis hier starten
        penaltypoint = 0;

        FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
        loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");

    }

    /**
     * Continue to drunk selfie fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        penaltypoint = penaltypoint + getPenaltyPoints(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
        Log.d("Gyroscope", "onSensorChanged: X: "+ sensorEvent.values[0] + " Y: "+ sensorEvent.values[1] + " Z: "+ sensorEvent.values[2]);
    }

    public int getPenaltyPoints(float xValue, float yValue, float zValue) {
        // get Absolute Values
        xValue = Math.abs(xValue);
        yValue = Math.abs(yValue);
        zValue = Math.abs(zValue);

        // get the current maximum
        float tempMax = Math.max(xValue, yValue);
        float realMax = Math.max(tempMax, zValue);

        // give out the penalty points
        if (realMax>10){
            return 5;}
        else if(realMax>5) {
            return 2;}
        else if(realMax>2){
            return 1;}
        else return 0;
    }



    public void finishTypingChallenge(View view) {
        //TODO @Dennis: hier ENDET die typing challenge, also sollte die weaving analysis hier enden
        UserData.DRUNKOMETER_ANALYSIS.PenaltyPoint = penaltypoint;

        UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE = UserData.calculateMean("error",UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE = UserData.calculateMean("completiontime", UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        Log.d("D-O-M challenge error", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE));
        Log.d("D-O-M challenge time", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE));
        Log.d("D-O-M camera storage path: ", String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
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
        RecommendationFragment recommendationFragment = RecommendationFragment.newInstance(false);
        loadFragment(recommendationFragment, "recommendationFragment");
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


            RecommendationFragment recommendationFragment = RecommendationFragment.newInstance(true);
            loadFragment(recommendationFragment, "recommendationFragment");
        }
    }

    /**
     * Continue to home screen
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToHomeScreen(View view) {
        DrunkometerFragment drunkometerFragment = new DrunkometerFragment();
        loadFragment(drunkometerFragment, "drunkometerFragment");
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /**
     * View chat detail
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void viewChatDetails(String date, String time, String content, boolean safeToText) {
        bottomNavigationView.setVisibility(View.GONE);
        //TODO: add selfie
        ChatDetailViewFragment chatDetailViewFragment = ChatDetailViewFragment.newInstance(date, time, content, safeToText);
        loadFragment(chatDetailViewFragment, "chatDetailViewFragment");
    }

    /**
     * View chat list
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToChatView(View view) {
        ChatsFragment chatsFragment = new ChatsFragment();
        loadFragment(chatsFragment, "chatsFragment");
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /**
     * Copy message content
     */
    public void copyMessageContent(View view) {
        Log.d("Message Copy", "message content copied to clipboard");
        //TODO
    }

}