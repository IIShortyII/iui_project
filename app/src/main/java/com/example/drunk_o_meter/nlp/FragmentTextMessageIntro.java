package com.example.drunk_o_meter.nlp;

import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTextMessageIntro extends Fragment {

    private TextView title;
    private TextView description;
    private Button next;
    private Button skip;

    private View layout;


    public FragmentTextMessageIntro() {
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

        this.layout = inflater.inflate(R.layout.fragment_text_message_intro, container, false);

        // Inflate the layout for this fragment
        return layout;
    }


}