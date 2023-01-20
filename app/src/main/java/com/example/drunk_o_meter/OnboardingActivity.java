package com.example.drunk_o_meter;

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

import com.example.drunk_o_meter.recommender.CSVFile;
import com.example.drunk_o_meter.recommender.DrinkType;
import com.example.drunk_o_meter.recommender.PreferencesFragment;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallenge;
import com.example.drunk_o_meter.typingChallenge.FragmentTypingChallengeIntro;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    //TODO: Add 1) user age question & 2) weight & sex

    private String stage;
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        loadAlcoholData();

        stage = getIntent().getStringExtra("stage");
        setStage(stage);
    }

    /**
     * Depending on the stage the user is in, provide the corresponding content of the onboarding flow.
     * @param stage the stage the user is in ("username" or "typingChallenge" or "preferences")
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStage(String stage) {

        switch (stage) {
            case "username":
                UsernameFragment usernameFragment = new UsernameFragment();
                loadFragment(usernameFragment, "fragmentUsername");
                break;

            case "typingChallenge":
                FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
                loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
                break;

            case "preferences":
                PreferencesFragment preferencesFragment = new PreferencesFragment();
                loadFragment(preferencesFragment, "preferencesFragment");

        }
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

        android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container_onboarding);

        if(fragment == null)
        {
            ft.add(R.id.fragment_container_onboarding, frag, tag);
        } else
        {
            ft.replace(R.id.fragment_container_onboarding, frag, tag);
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    /**
     * Reads the alcohol data from the csv and saves it to the local storage
     */
    public void loadAlcoholData() {
        //TODO: Kategorien überprüfen --> Aperol eher wie Beer und Wein
        InputStream inputStream = getResources().openRawResource(R.raw.alcohol_data);
        CSVFile csvFile = new CSVFile(inputStream);
        //list of string arrays with format: [Drink_Name, Drink_Type, Drink_Subtype, Drink_Liter, Drink_Alcohol_Pure_Gramm]
        List alcoholList = csvFile.read();

        ArrayList<String> wine = new ArrayList<>();
        ArrayList<String> beer = new ArrayList<>();
        ArrayList<String> cocktails = new ArrayList<>();
        ArrayList<String> shots = new ArrayList<>();
        ArrayList<String> hot = new ArrayList<>();

        for (Object item: alcoholList) {
            String[] alcohol = (String []) item;
            switch (alcohol[1]) {
                case "Wine": wine.add(alcohol[0]); break;
                case "Beer": beer.add(alcohol[0]); break;
                case "Cocktail": cocktails.add(alcohol[0]); break;
                case "Shot": shots.add(alcohol[0]); break;
                case "Hot Drink": hot.add(alcohol[0]); break;
            }
        }

        UserData.DRINKS.put(DrinkType.WINE, wine);
        UserData.DRINKS.put(DrinkType.BEER, beer);
        UserData.DRINKS.put(DrinkType.COCKTAIL, cocktails);
        UserData.DRINKS.put(DrinkType.SHOT, shots);
        UserData.DRINKS.put(DrinkType.HOT, hot);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void saveUsername(View view) {
        EditText usernameInput = findViewById(R.id.usernameInput);
        String username = String.valueOf(usernameInput.getText());
        if (username.length() != 0) {
            USERNAME = username;
            FragmentTypingChallengeIntro fragmentTypingChallengeIntro = new FragmentTypingChallengeIntro();
            loadFragment(fragmentTypingChallengeIntro, "fragmentTypingChallengeIntro");
            this.stage = "typingChallenge";

            Log.d("D-O-M USERNAME", USERNAME);
            DataHandler.storeSettings(this);

        }
    }

    public void startTypingChallenge(View view) {
        FragmentTypingChallenge fragmentTypingChallenge = new FragmentTypingChallenge();
        loadFragment(fragmentTypingChallenge, "fragmentTypingChallenge");
    }

    public void finishTypingChallenge(View view) {
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        loadFragment(preferencesFragment, "preferencesFragment");
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void goToHomeScreen(View view) {
        DataHandler.storeSettings(this);
        Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
        OnboardingActivity.this.startActivity(intent);
    }

}
