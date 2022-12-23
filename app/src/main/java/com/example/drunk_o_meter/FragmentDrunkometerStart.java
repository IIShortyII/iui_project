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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;
import com.example.drunk_o_meter.nlp.TextMessage;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrunkometerStart extends Fragment {

    private View layout;


    public FragmentDrunkometerStart() {
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

        TextView title = layout.findViewById(R.id.DrunkometerStartTitle);
        title.setText("Welcome back, " + USERNAME + "!");

        // Inflate the layout for this fragment
        return layout;
    }

    /**
     * Clear local storage for debugging purposes and go back to start
     * @param view the current activity context
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void clearLocalStorage(View view) {
        UserData.USERNAME = "";
        UserData.BASELINE_TYPING_CHALLENGE = new ArrayList<>();
        UserData.TEXT_MESSAGE_LIST = new ArrayList<>();
        DataHandler.storeSettings(getActivity());

        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        Log.d("D-O-M", "internal storage cleared");
    }


}