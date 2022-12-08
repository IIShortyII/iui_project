package com.example.drunk_o_meter.typingChallenge;

import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_SAMPLES;
import static com.example.drunk_o_meter.userdata.UserData.TYPING_CHALLENGE_SAMPLES;
import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTypingChallengeIntro extends Fragment {

    private TextView title;
    private Button next;

    private View layout;


    public FragmentTypingChallengeIntro() {
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

        this.layout = inflater.inflate(R.layout.fragment_typing_challenge_intro, container, false);

        // Set greeting title and typing challenge description text
        this.title = layout.findViewById(R.id.typingChallengeIntroTitle);
        title.setText("Hi, " + USERNAME + "!");

        // Set next button
        this.next = getActivity().findViewById(R.id.nextBtn);
        next.setText(getResources().getString(R.string.next_typingIntro));
        next.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return layout;
    }

}