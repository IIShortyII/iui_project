package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.Gender;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrunkometerFragment extends Fragment {
    private View layout;
    private ImageView selfieView;


    public DrunkometerFragment() {
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

        this.layout = inflater.inflate(R.layout.fragment_drunkometer_start, container, false);
        // set context depending on invoking activity

        //TODO remove in final version
        Button button = layout.findViewById(R.id.clearLocalStorageBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v)
            {
                clearLocalStorage(v);
            }
        });

        Button startNewDayBtn = layout.findViewById(R.id.startNewDayBtn);
        startNewDayBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v)
            {
                startNewDay(v);
            }
        });

        TextView title = layout.findViewById(R.id.DrunkometerStartTitle);
        title.setText("Welcome back, " + USERNAME + "!");
        
        selfieView = layout.findViewById(R.id.selfieView);
        if(UserData.DRUNKOMETER_ANALYSIS_LIST.size() > 0){
            selfieView.setImageBitmap(UserData.DRUNKOMETER_ANALYSIS_LIST.get(UserData.DRUNKOMETER_ANALYSIS_LIST.size()-1).SELFIE);
        } else {
            Log.d("D-O-M last selfie", "no selfie");
        }

        // Inflate the layout for this fragment
        return layout;
    }

    /**
     * Clear previous drinks and start as "sober"
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startNewDay(View view) {
        UserData.RECOMMENDATION = new ArrayList<>();
        UserData.GRAM_OF_ALCOHOL = 0;
        DataHandler.storeSettings(getActivity());
    }

    /**
     * Clear local storage for debugging purposes and go back to start
     * @param view the current activity context
     */
    //TODO: remove button in final version
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void clearLocalStorage(View view) {
        UserData.AGE_CHECK = false;
        UserData.USERNAME = "";
        UserData.WEIGHT = 0;
        UserData.GENDER = Gender.FEMALE;
        UserData.BASELINE_TYPING_CHALLENGE = new ArrayList<>();
        UserData.DRUNKOMETER_ANALYSIS_LIST = new ArrayList<>();
        DataHandler.storeSettings(getActivity());

        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        Log.d("D-O-M", "internal storage cleared");
    }


}