package com.example.drunk_o_meter.typingChallenge;

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
public class FragmentTypingChallengeIntro extends Fragment {

    private static String CONTEXT;
    private TextView title;
    private TextView description;
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
        // set context depending on invoking activity
        this.CONTEXT = getActivity().getClass().getSimpleName();

        // Set greeting title and typing challenge description text
        this.title = layout.findViewById(R.id.typingChallengeIntroTitle);
        this.description = layout.findViewById(R.id.typingChallengeIntroDescription);

        if(this.CONTEXT.equals(getResources().getString(R.string.ONBOARDING))) {
            title.setText("Hi, " + USERNAME + "!");
            description.setText(getResources().getString(R.string.baselineIntroDescription));
        } else {
            title.setText(getResources().getString(R.string.typingChallengeIntroTitle));
            description.setText(getResources().getString(R.string.typingChallengeIntroDescription));
        }
        this.next = layout.findViewById(R.id.startTypingChallengeBtn);
        next.setText(getResources().getString(R.string.next_typingIntro));

        // Inflate the layout for this fragment
        return layout;
    }


}