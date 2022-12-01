package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_SAMPLES;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.drunk_o_meter.nlp.TypingSample;
import com.example.drunk_o_meter.userdata.UserData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class OnboardingActivity extends AppCompatActivity {
    private String stage;
    private int baselineTextCount;

    private int textPosition = 0;
    private ArrayList<Boolean> currentSampleValidations = new ArrayList<Boolean>();
    private long currentSampleTimerStart;

    private TextView baselineTextView;
    private ProgressBar progressBar;
    private TextView progressLabel;
    private Button continueToDrunkometer;
    private EditText hiddenInput;
    private SpannableStringBuilder currentSpannableString;

    // Colors for text cursor
    BackgroundColorSpan cursorColor = new BackgroundColorSpan(Color.YELLOW);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        stage = getIntent().getStringExtra("stage");
        setStage(stage);

    }

    /**
     * Depending on the stage the user is in, provide the corresponding content of the onboarding flow.
     * @param stage the stage the user is in ("username" or "baseline")
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStage(String stage) {
        if(stage.equals("username")){
           findViewById(R.id.baselineContainer).setVisibility(View.INVISIBLE);

       // } else if (stage.equals("onboarding")) {

            // Set up UI components
            findViewById(R.id.baselineContainer).setVisibility(View.VISIBLE);
            this.baselineTextView = findViewById(R.id.baselineTextView);
            this.hiddenInput = findViewById(R.id.hiddenInput);
            this.progressBar = findViewById(R.id.progress);
            this.progressLabel = findViewById(R.id.progressLabel);
            this.continueToDrunkometer = findViewById(R.id.continueToDrunkometer);
            continueToDrunkometer.setVisibility(View.INVISIBLE);

            // Show keyboard
            hiddenInput.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            // Update content based on baselineTextCount
            this.baselineTextCount = BASELINE_TYPING_SAMPLES.size() + 1;
            updateBaselineContent();

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
                            incrementBaselineTextCount();
                            updateBaselineContent();
                            hiddenInput.setText("");
                            currentSampleValidations = new ArrayList<Boolean>();
                        } else {
                            updateCursor();
                        }
                    }

                }
            });

        }
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
        Log.d("DRUNK-O-METER", text + "");

        TypingSample sample = new TypingSample(text, userInput, time, error);
        BASELINE_TYPING_SAMPLES.add(sample);
        // TODO: save on device
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

    private void incrementBaselineTextCount() {
        this.baselineTextCount++;
    }


    /**
     * Update content in baseline calculation phase depending on baselineCount
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateBaselineContent() {
        // Determine baselineTextCount according to userData
        if(baselineTextCount <= getResources().getInteger(R.integer.baselineCount)){
        setBaselineText(baselineTextCount);
        this.progressBar.setProgress(this.baselineTextCount, true);
        this.progressLabel.setText(baselineTextCount + "/10");
        textPosition = 0;
        updateCursor();

        } else {

            // Hide keyboard
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            // Display success message screen and proceed to drunkometerActivity
            baselineTextView.setText(getResources().getString(R.string.baselineComplete));
            baselineTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            baselineTextView.setTypeface(null, Typeface.BOLD);
            continueToDrunkometer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set displayed baseline text depending to baselineTextCount
     * @param baselineTextCount number of baseline texts that have already been recorded
     */
    private void setBaselineText(int baselineTextCount) {
        // Get text resource according to baselineTextCount
        int textId = this.getResources().
                getIdentifier("text_"+baselineTextCount, "string", this.getPackageName());
        String text = getResources().getString(textId);
        currentSpannableString = new SpannableStringBuilder(text);
        baselineTextView.setText(currentSpannableString);

    }

    /**
     * Proceed to DrunkometerActivity
     */
    public void continueToDrunkometer(View view) {
        Intent intent = new Intent(OnboardingActivity.this, DrunkometerActivity.class);
        OnboardingActivity.this.startActivity(intent);
    }

}
