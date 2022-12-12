package com.example.drunk_o_meter.nlp;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTextMessageInput extends Fragment {

    private Button analyzeTextMessage;

    private View layout;


    public FragmentTextMessageInput() {
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

        this.layout = inflater.inflate(R.layout.fragment_text_message_input, container, false);

        // Set next button
        this.analyzeTextMessage = layout.findViewById(R.id.analyzeTextMessage);

        // Inflate the layout for this fragment
        return layout;
    }

}