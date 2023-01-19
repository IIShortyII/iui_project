package com.example.drunk_o_meter.recommender;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.drunk_o_meter.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment {

    private View layout;
    private static String CONTEXT;

    private ArrayList<String> wineList;
    private ArrayList<String> beerList;
    private ArrayList<String> cocktailsList;
    private ArrayList<String> shotsList;
    private ArrayList<String> hotList;

    public PreferencesFragment() {
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
        this.layout = inflater.inflate(R.layout.fragment_preferences, container, false);
        // set context depending on invoking activity
        this.CONTEXT = getActivity().getClass().getSimpleName();

        loadAlcoholData();

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Drink Type: Wine
        RecyclerView recyclerViewWine = getView().findViewById(R.id.preferences_wine_list);
        recyclerViewWine.setHasFixedSize(true);
        PreferenceItemAdapter adapterWine = new PreferenceItemAdapter(DrinkType.WINE, wineList);
        recyclerViewWine.setAdapter(adapterWine);

        //Drink Type: Beer
        RecyclerView recyclerViewBeer = getView().findViewById(R.id.preferences_beer_list);
        recyclerViewBeer.setHasFixedSize(true);
        PreferenceItemAdapter adapterBeer = new PreferenceItemAdapter(DrinkType.BEER, beerList);
        recyclerViewBeer.setAdapter(adapterBeer);

        //Drink Type: Cocktail
        RecyclerView recyclerViewCocktail = getView().findViewById(R.id.preferences_cocktails_list);
        recyclerViewCocktail.setHasFixedSize(true);
        PreferenceItemAdapter adapterCocktail = new PreferenceItemAdapter(DrinkType.COCKTAIL, cocktailsList);
        recyclerViewCocktail.setAdapter(adapterCocktail);

        //Drink Type: Shot
        RecyclerView recyclerViewShot = getView().findViewById(R.id.preferences_shots_list);
        recyclerViewShot.setHasFixedSize(true);
        PreferenceItemAdapter adapterShot = new PreferenceItemAdapter(DrinkType.SHOT, shotsList);
        recyclerViewShot.setAdapter(adapterShot);

        //Drink Type: Hot Drinks
        RecyclerView recyclerViewHot = getView().findViewById(R.id.preferences_hot_list);
        recyclerViewWine.setHasFixedSize(true);
        PreferenceItemAdapter adapterHot = new PreferenceItemAdapter(DrinkType.HOT, hotList);
        recyclerViewHot.setAdapter(adapterHot);

        //Only show continue when in onboarding
        Button continueButton = getView().findViewById(R.id.continueToHome);
        if (CONTEXT.equals(getResources().getString(R.string.ONBOARDING))) {
            continueButton.setVisibility(View.VISIBLE);
            //TODO: set marginBottom of scrollview programatically when button appears
            //value like: android:layout_marginBottom="90dp"
        }
    }

    /**
     * Reads the alcohol data from the csv and saves it to the local variables
     */
    public void loadAlcoholData() {
        //TODO: Kategorien überprüfen --> Aperol eher wie Beer und Wein
        InputStream inputStream = getResources().openRawResource(R.raw.alcohol_data);
        CSVFile csvFile = new CSVFile(inputStream);
        //list of string arrays with format: [Drink_Name, Drink_Type, Drink_Subtype, Drink_Liter, Drink_Alcohol_Percent, Drink_Alcohol_Pure_Gramm]
        List alcoholList = csvFile.read();

        wineList = new ArrayList<>();
        beerList = new ArrayList<>();
        cocktailsList = new ArrayList<>();
        shotsList = new ArrayList<>();
        hotList = new ArrayList<>();

        for (Object item: alcoholList) {
            String[] alcohol = (String []) item;
            switch (alcohol[1]) {
                case "Wine": wineList.add(alcohol[0]); break;
                case "Beer": beerList.add(alcohol[0]); break;
                case "Cocktail": cocktailsList.add(alcohol[0]); break;
                case "Shot": shotsList.add(alcohol[0]); break;
                case "Hot Drink": hotList.add(alcohol[0]); break;
            }
        }
    }
}