package com.example.drunk_o_meter;

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

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.Gender;
import com.example.drunk_o_meter.userdata.UserData;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDataFragment extends Fragment {

    private Button next;
    MaterialButtonToggleGroup toggleGroup;

    private View layout;
    protected Activity contextActivity;


    public PersonalDataFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.layout = inflater.inflate(R.layout.fragment_personal_data, container, false);

        // Set next button
        this.next = layout.findViewById(R.id.saveUsername);
        next.setText(getResources().getString(R.string.next_name));
        next.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        toggleGroup = getView().findViewById(R.id.toggleGroup);
        updateGender();
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> updateGender());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            contextActivity = (Activity) context;
        }
    }

    /**
     * Update the gender input based on selection
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void updateGender() {
        Button activeBtn;
        Button inactiveBtn;
        if (toggleGroup.getCheckedButtonId() == R.id.btnFemale) {
            UserData.GENDER = Gender.FEMALE;
            activeBtn = getView().findViewById(R.id.btnFemale);
            inactiveBtn = getView().findViewById(R.id.btnMale);
        } else {
            UserData.GENDER = Gender.MALE;
            inactiveBtn = getView().findViewById(R.id.btnFemale);
            activeBtn = getView().findViewById(R.id.btnMale);
        }

        activeBtn.setBackgroundColor(getResources().getColor(R.color.primaryVariant));
        activeBtn.setTextColor(getResources().getColor(R.color.black));
        inactiveBtn.setBackgroundColor(Color.TRANSPARENT);
        inactiveBtn.setTextColor(getResources().getColor(R.color.primaryVariant));
        DataHandler.storeSettings(contextActivity);
    }

}