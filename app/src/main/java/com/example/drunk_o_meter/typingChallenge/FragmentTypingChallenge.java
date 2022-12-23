package com.example.drunk_o_meter.typingChallenge;

import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_CHALLENGE;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;


import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTypingChallenge extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String CONTEXT = "";

    private int baselineTextCount;
    private int typingChallengeTextCount;

    private int textPosition = 0;
    private ArrayList<Boolean> currentSampleValidations = new ArrayList<Boolean>();
    private long currentSampleTimerStart;

    private TextView baselineTextView;
    private ProgressBar progressBar;
    private TextView progressLabel;
    private Button next;
    private EditText hiddenInput;
    private SpannableStringBuilder currentSpannableString;

    // Colors for text cursor
    BackgroundColorSpan cursorColor = new BackgroundColorSpan(Color.YELLOW);

    private View layout;


    public FragmentTypingChallenge() {
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

        this.layout = inflater.inflate(R.layout.fragment_typing_challenge, container, false);

        this.baselineTextView = layout.findViewById(R.id.baselineTextView);
        this.hiddenInput = layout.findViewById(R.id.hiddenInput);
        this.progressBar = layout.findViewById(R.id.progress);
        this.progressLabel = layout.findViewById(R.id.progressLabel);
        this.next = layout.findViewById(R.id.finishTypingChallenge);
        next.setVisibility(View.INVISIBLE);


        // set context depending on invoking activity
        this.CONTEXT = getActivity().getClass().getSimpleName();

        if(this.CONTEXT.equals(getResources().getString(R.string.ONBOARDING))){
            // Update content based on baselineTextCount
            this.baselineTextCount = BASELINE_TYPING_CHALLENGE.size() + 1;
            this.progressBar.setMax(getResources().getInteger(R.integer.baselineCount));
        } else {
            this.typingChallengeTextCount = 1;
            this.progressBar.setMax(getResources().getInteger(R.integer.typingChallengeCount));
            // Reset typing challenge samples
            UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE = new ArrayList<>();
        }


        updateBaselineContent();

        hiddenInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        // Set up change listener for the hidden input field
        hiddenInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Start time when user types the first char of the sequence
                if (s.length() == 1){
                    startTimer();
                }
                if (s.length() != 0){
                    String lastChar = String.valueOf(s.toString().charAt(s.length()-1));
                    if (lastChar.equals(String.valueOf(baselineTextView.getText().toString().charAt(textPosition)))) {
                        currentSampleValidations.add(true);
                    } else {
                        currentSampleValidations.add(false);
                    }

                    textPosition = textPosition +1;

                    // Continue to next text if current text is complete
                    if (textPosition == baselineTextView.getText().toString().length()){
                        saveTypingSample();
                        incrementTextCount();
                        updateBaselineContent();
                        hiddenInput.setText("");
                        currentSampleValidations = new ArrayList<Boolean>();
                    } else {
                        updateCursor();
                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return layout;
    }

    /**
     * Save data as typing sample.
     */
    private void saveTypingSample() {
        long time = System.currentTimeMillis() - currentSampleTimerStart;
        String text = baselineTextView.getText().toString();
        String userInput = hiddenInput.getText().toString();
        int errorCount = Collections.frequency(currentSampleValidations, false);
        double error = ((double) errorCount / (double)text.length()) * 100.00;
        TypingSample sample = new TypingSample(text, userInput, time, error);

        if(this.CONTEXT.equals(getResources().getString(R.string.ONBOARDING))) {
            BASELINE_TYPING_CHALLENGE.add(sample);
        } else {
            UserData.DRUNKOMETER_ANALYSIS.TYPING_CHALLENGE.add(sample);
        }


    }

    /**
     * Start timer to measure completion time for a typing sample
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startTimer() {
        currentSampleTimerStart = System.currentTimeMillis();
    }

    /**
     * Update the coloring of the displayed text depending on the current position within the text
     */
    private void updateCursor() {
        currentSpannableString.setSpan(cursorColor,textPosition, textPosition+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        baselineTextView.setText(currentSpannableString);
    }

    private void incrementTextCount() {
        if(this.CONTEXT.equals(getResources().getString(R.string.ONBOARDING))) {
            this.baselineTextCount++;
        } else {
            this.typingChallengeTextCount++;
        }

    }


    /**
     * Update content in baseline calculation phase depending on baselineCount
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateBaselineContent() {

        int count;
        int maxCount;
        String successText;
        String buttonLabel;

        if(this.CONTEXT.equals(getResources().getString(R.string.ONBOARDING))) {
            count = baselineTextCount;
            maxCount = getResources().getInteger(R.integer.baselineCount);
            successText = getResources().getString(R.string.baselineComplete);
            buttonLabel = getResources().getString(R.string.next_completeOnboarding);

        } else {
            count = typingChallengeTextCount;
            maxCount = getResources().getInteger(R.integer.typingChallengeCount);
            successText = getResources().getString(R.string.typingChallengeComplete);
            buttonLabel = getResources().getString(R.string.next_takeSelfie);
        }
            // Determine count according to userData
            if (count <= maxCount) {
                setBaselineText(count);
                this.progressBar.setProgress(count, true);
                this.progressLabel.setText(count + "/" + maxCount);
                textPosition = 0;
                updateCursor();

            } else {

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(hiddenInput.getWindowToken(), 0);

                // Display success message screen and continue with next step
                baselineTextView.setText(successText);
                baselineTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                baselineTextView.setTypeface(null, Typeface.BOLD);
                next.setText(buttonLabel);
                next.setVisibility(View.VISIBLE);
            }

    }

    /**
     * Set displayed baseline text depending to baselineTextCount
     * @param baselineTextCount number of baseline texts that have already been recorded
     */
    private void setBaselineText(int baselineTextCount) {
        // Get text resource according to baselineTextCount
        int textId = getActivity().getResources().
                getIdentifier("text_"+baselineTextCount, "string", getActivity().getPackageName());
        String text = getResources().getString(textId);
        currentSpannableString = new SpannableStringBuilder(text);
        baselineTextView.setText(currentSpannableString);

    }
}