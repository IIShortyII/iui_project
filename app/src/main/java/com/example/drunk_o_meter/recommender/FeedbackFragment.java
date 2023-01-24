package com.example.drunk_o_meter.recommender;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.HomeActivity;
import com.example.drunk_o_meter.R;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {

    protected Activity contextActivity;
    private View layout;

    MaterialButtonToggleGroup toggleGroupDrinks;
    MaterialButtonToggleGroup toggleGroupOther;

    private String[] drink1;
    private String[] drink2;

    private Button activeBtn;
    private Button otherActiveBtn;

    private Button drink1Btn;
    private Button drink2Btn;
    private Button drinkOtherBtn;

    private Button wineBtn;
    private Button beerBtn;
    private Button cocktailBtn;
    private Button shotBtn;
    private Button hotBtn;
    private Button noneBtn;

    private Button continueBtn;


    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_feedback, container, false);

        //Get last recommendation
        drink1 = UserData.RECOMMENDATION.get(0);
        drink2 = UserData.RECOMMENDATION.get(1);

        //Set button texts
        drink1Btn = layout.findViewById(R.id.btnDrink1);
        drink1Btn.setText(drink1[0]);
        drink2Btn = layout.findViewById(R.id.btnDrink2);
        drink2Btn.setText(drink2[0]);
        drinkOtherBtn = layout.findViewById(R.id.btnOtherDrink);

        //Set other drinks text
        wineBtn = layout.findViewById(R.id.btnWine);
        beerBtn = layout.findViewById(R.id.btnBeer);
        cocktailBtn = layout.findViewById(R.id.btnCocktail);
        shotBtn = layout.findViewById(R.id.btnShot);
        hotBtn = layout.findViewById(R.id.btnHot);
        noneBtn = layout.findViewById(R.id.btnNone);

        continueBtn = layout.findViewById(R.id.feedbackContinue);

        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        toggleGroupDrinks = layout.findViewById(R.id.feedbackToggleGroup);
        toggleGroupDrinks.addOnButtonCheckedListener((group, checkedId, isChecked) -> updateLastDrink());

        toggleGroupOther = layout.findViewById(R.id.feedbackToggleGroupOther);
        toggleGroupOther.addOnButtonCheckedListener((group, checkedId, isChecked) -> updateOtherDrink());

        continueBtn.setOnClickListener(view1 -> saveFeedback());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            contextActivity = (Activity) context;
        }
    }

    /**
     * Update the last drink input based on selection
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void updateLastDrink() {
        Button inactiveBtn1;
        Button inactiveBtn2;
        if (toggleGroupDrinks.getCheckedButtonId() == R.id.btnDrink1) {
            toggleGroupOther.setVisibility(View.INVISIBLE);
            layout.findViewById(R.id.feedbackOtherDrink).setVisibility(View.INVISIBLE);
            activeBtn = drink1Btn;
            inactiveBtn1 = drink2Btn;
            inactiveBtn2 = drinkOtherBtn;
        } else if (toggleGroupDrinks.getCheckedButtonId() == R.id.btnDrink2) {
            toggleGroupOther.setVisibility(View.INVISIBLE);
            layout.findViewById(R.id.feedbackOtherDrink).setVisibility(View.INVISIBLE);
            activeBtn = drink2Btn;
            inactiveBtn1 = drink1Btn;
            inactiveBtn2 = drinkOtherBtn;
        } else {
            activeBtn = drinkOtherBtn;
            toggleGroupOther.setVisibility(View.VISIBLE);
            layout.findViewById(R.id.feedbackOtherDrink).setVisibility(View.VISIBLE);
            inactiveBtn1 = drink1Btn;
            inactiveBtn2 = drink2Btn;
        }

        activeBtn.setBackgroundColor(getResources().getColor(R.color.primaryVariant));
        activeBtn.setTextColor(getResources().getColor(R.color.black));
        inactiveBtn1.setBackgroundColor(Color.TRANSPARENT);
        inactiveBtn1.setTextColor(getResources().getColor(R.color.primaryVariant));
        inactiveBtn2.setBackgroundColor(Color.TRANSPARENT);
        inactiveBtn2.setTextColor(getResources().getColor(R.color.primaryVariant));
    }

    /**
     * Update the other option input based on selection
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void updateOtherDrink() {
        if (otherActiveBtn != null) {
            otherActiveBtn.setBackgroundColor(Color.TRANSPARENT);
            otherActiveBtn.setTextColor(getResources().getColor(R.color.primaryVariant));
        }

        if (toggleGroupOther.getCheckedButtonId() == R.id.btnWine) {
            otherActiveBtn = wineBtn;
        } else if (toggleGroupOther.getCheckedButtonId() == R.id.btnBeer) {
            otherActiveBtn = beerBtn;
        } else if (toggleGroupOther.getCheckedButtonId() == R.id.btnCocktail) {
            otherActiveBtn = cocktailBtn;
        } else if (toggleGroupOther.getCheckedButtonId() == R.id.btnShot) {
            otherActiveBtn = shotBtn;
        } else if (toggleGroupOther.getCheckedButtonId() == R.id.btnHot) {
            otherActiveBtn = hotBtn;
        } else {
            otherActiveBtn = noneBtn;
        }

        otherActiveBtn.setBackgroundColor(getResources().getColor(R.color.primaryVariant));
        otherActiveBtn.setTextColor(getResources().getColor(R.color.black));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void saveFeedback() {
        double alcoholGramNow = 0;
        if (activeBtn == drink1Btn) {
            String alcoholGramTxt = drink1[4];
            alcoholGramTxt = alcoholGramTxt.replace(",", ".");
            alcoholGramNow = Double.parseDouble(alcoholGramTxt);
        } else if (activeBtn == drink2Btn) {
            String alcoholGramTxt = drink2[4];
            alcoholGramTxt = alcoholGramTxt.replace(",", ".");
            alcoholGramNow = Double.parseDouble(alcoholGramTxt);
        } else if (otherActiveBtn != null){
            String otherDrinkCategory = otherActiveBtn.getText().toString();

            //use mediate amount of alcohol from category
            switch (otherDrinkCategory) {
                //TODO check drink categories
                case "Wine":
                    alcoholGramNow = 18.384;
                    break;
                case "Beer":
                    alcoholGramNow = 20.61333333;
                    break;
                case "Cocktail":
                    alcoholGramNow = 20.45433333;
                    break;
                case "Shot":
                    alcoholGramNow = 4.915555556;
                    break;
                case "Hot Drink":
                    alcoholGramNow = 19.6;
                    break;
            }
        } else {
            return;
        }

        double alcoholGramTotal = UserData.GRAM_OF_ALCOHOL + alcoholGramNow;
        UserData.GRAM_OF_ALCOHOL = alcoholGramTotal;

        DataHandler.storeSettings(contextActivity);
        ((HomeActivity)contextActivity).loadTypingChallengeAfterFeedback();
    }
}