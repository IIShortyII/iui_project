package com.example.drunk_o_meter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drunk_o_meter.chat_list.ChatDetailViewFragment;
import com.example.drunk_o_meter.nlp.FragmentTextMessageInput;
import com.example.drunk_o_meter.nlp.FragmentTextMessageIntro;
import com.example.drunk_o_meter.nlp.NlpPipeline;
import com.example.drunk_o_meter.nlp.Sentiment;
import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.recommender.FeedbackFragment;
import com.example.drunk_o_meter.recommender.PreferencesFragment;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import com.example.drunk_o_meter.userdata.DrunkometerAnalysis;
import com.example.drunk_o_meter.userdata.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener, SensorEventListener {
    //TODO: add tab for past recommendations?

    /**
     * Main UI component for the navigation bar
     */
    BottomNavigationView bottomNavigationView;

    Sensor gyroscope;
    int penaltypoint = 0;

    private File imageFile;

    // Server variables
    // private String IPV4ADDRESS = "192.168.178.156";
    private String IPV4ADDRESS = "192.168.178.156"; // TODO might have to change depending on what network the server is running
    private String PORTNUMBER = "5000";
    private byte[] byteArraySelfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.drunkometer);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * Load fragment into fragment container
     * @param frag The fragment to be loaded
     * @param tag The tag of the fragment to be loaded
     */
    public void loadFragment(android.app.Fragment frag, String tag) {
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
        PreferencesFragment preferencesFragment = new PreferencesFragment();

        switch (item.getItemId()) {
            case R.id.drunkometer:
                loadFragment(drunkometerFragment, "drunkometerFragment");
                return true;

            case R.id.chats:
                loadFragment(chatsFragment, "chatsFragment");
                return true;

            case R.id.preferences:
                loadFragment(preferencesFragment, "preferencesFragment");
                return true;
        }
        return false;
    }

    /**
     * Start drunkometer analysis by asking about the last recommendation if needed
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startDrunkometerAnalysis(View view) {
        // Create new DrunkometerAnalysis Object for this analysis
        UserData.DRUNKOMETER_ANALYSIS = new DrunkometerAnalysis();

        //Already at least one recommendation offered today
        if (UserData.RECOMMENDATION.size() != 0) {
            FeedbackFragment feedbackFragment = new FeedbackFragment();
            loadFragment(feedbackFragment, "feedbackFragment");
        } else {
            FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
            loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
        }
        bottomNavigationView.setVisibility(View.GONE);
    }

    /**
     * Show typing challenge intro
     */
    public void loadTypingChallengeAfterFeedback() {
        FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
        loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
    }

    /**
     * Continue to typing challenge
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startTypingChallenge(View view) {

        penaltypoint = 0;
        FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
        loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");

    }

    /**
     * Continue to drunk selfie fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        penaltypoint = penaltypoint + getPenaltyPoints(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
    }

    /**
     * Weaving Analysis for calculate weaving while typing
     */
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

        UserData.DRUNKOMETER_ANALYSIS.PenaltyPoint = penaltypoint;

        UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE = UserData.calculateMean("error",UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE = UserData.calculateMean("completiontime", UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE);
        Log.d("D-O-M challenge error", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_ERROR_CHALLENGE));
        Log.d("D-O-M challenge time", String.valueOf(UserData.DRUNKOMETER_ANALYSIS.MEAN_COMPLETIONTIME_CHALLENGE));
        Log.d("D-O-M camera storage path: ", String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));

        String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                File.separator + "drunkometer_selfie.jpeg";

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(imagePath);
        Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        //Open front camera
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
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
        byteArraySelfie = out.toByteArray();
        Bitmap compressed = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        UserData.DRUNKOMETER_ANALYSIS.SELFIE = compressed;

        goToTextMessageIntro();
    }

    /**
     * Continue to text message fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToTextMessageIntro() {
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
        ConstraintLayout layout_textIntro = this.findViewById(R.id.layout_textIntro);
        ConstraintLayout layout_waiting = this.findViewById(R.id.skip_waiting_layout);

        layout_textIntro.setVisibility(View.INVISIBLE);
        layout_waiting.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Wait a short time so that progress bar can load
                sendSelfieToServer(byteArraySelfie);
            }
        }, 100);
    }

    /**
     * Analyse the provided text message with NLP to determine the sentiment, and based on this and
     * the drunkenness score, continue to next fragment that suggests an action
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToWaitingScreen(View view) {
        EditText recipientInput = this.findViewById(R.id.textMessageRecipientInput);
        EditText textMessageInput = this.findViewById(R.id.textMessageTextInput);

        // Only proceed if user has provided both recipient name and text message
        if(recipientInput.getText().toString().length() != 0 && textMessageInput.getText().toString().length() !=0) {
            ConstraintLayout layout_textInput = this.findViewById(R.id.text_input_layout);
            ConstraintLayout layout_waiting = this.findViewById(R.id.waiting_layout);

            layout_textInput.setVisibility(View.INVISIBLE);
            layout_waiting.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Wait a short time so that progress bar can load
                    analyzeTextMessage(recipientInput, textMessageInput);
                }
            }, 100);
        }
    }

    /**
     * Calculate sentiments analysis
     */
    public void analyzeTextMessage(EditText recipientInput, EditText textMessageInput) {
        Date date = Calendar.getInstance().getTime(); // Format: Fri Dec 23 10:28:05 GMT+01:00 2022

        NlpPipeline nlpPipeline = new NlpPipeline();
        nlpPipeline.init();
        Sentiment sentiment = nlpPipeline.estimatingSentiment(textMessageInput.getText().toString());
        // Save text message to list of text messages associated with the user
        TextMessage textMessage = new TextMessage(recipientInput.getText().toString(), textMessageInput.getText().toString(), sentiment.toString(), date);
        // Add text message to current DRUNKOMETER_ANALYSIS object
        UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE = textMessage;

        sendSelfieToServer(byteArraySelfie);
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
    public void viewChatDetails(String date, String time, String content, String recipient, Bitmap selfie, boolean safeToText) {
        bottomNavigationView.setVisibility(View.GONE);
        ChatDetailViewFragment chatDetailViewFragment = ChatDetailViewFragment.newInstance(date, time, content, recipient, selfie, safeToText);
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
     * "Safe to text" is calculated based on the drunkenness score and the sentiments analysis
     */
    public boolean calculateSafeToText(int drunkennessScore) {
        boolean safeToText = false;
        String sentimentAnalysis = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE.getSentimentAnalysis();
        switch (drunkennessScore) {
            case 0:
                //all sentiments safe
                safeToText = true;
                break;
            case 1: case 2:
                //"very" sentiments not safe
                if (sentimentAnalysis == Sentiment.VERY_NEGATIVE.toString() || sentimentAnalysis == Sentiment.VERY_POSITIVE.toString()) {
                    safeToText = false;
                } else {
                    safeToText = true;
                }
                break;
            case 3: case 4:
                //only neutral safe
                if (sentimentAnalysis == Sentiment.NEUTRAL.toString()) {
                    safeToText = true;
                } else {
                    safeToText = false;
                }
        }
        return safeToText;
    }

    /**
     * Copy message content to clipboard if message is "safe to text"
     */
    public void copyMessageContent(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        TextMessage currentMessage = UserData.DRUNKOMETER_ANALYSIS.TEXT_MESSAGE;
        if (currentMessage == null) {
            Log.d("Message Copy", "Error: Tried to copy non-existing message to clipboard.");
            return;
        }
        String content = currentMessage.getMessage();
        ClipData clip = ClipData.newPlainText("Drunk-O-Meter Text Message", content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(),"Your message text was copied - go ahead and text it now!",Toast.LENGTH_SHORT).show();
    }

    // Server communication - SEND
    void sendSelfieToServer(byte[] byteArray){
        String postUrl= "http://8324-34-145-159-223.ngrok.io";
        //String postUrl=  "https://d05kgolj407-496ff2e9c6d22116-5000-colab.googleusercontent.com/";
        RequestBody postBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "drunkometer_selfie.jpeg", RequestBody.create(MediaType.parse("image/*jpeg"), byteArray))
                .build();

        postRequest(postUrl, postBody);
    }

    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("D-O-M", "Failed to Connect to Server");
                Log.d("D-O-M", call.request().toString());

                UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION = 1.00;

                RecommendationFragment recommendationFragment = new RecommendationFragment();
                loadFragment(recommendationFragment, "recommendationFragment");
            }

            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    //TODO: @Dennis get score from server response
                    UserData.DRUNKOMETER_ANALYSIS.SELFIE_DRUNK_PREDICTION = 1.00;

                    RecommendationFragment recommendationFragment = new RecommendationFragment();
                    loadFragment(recommendationFragment, "recommendationFragment");
                    
                    Log.d("D-O-M Server Response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}