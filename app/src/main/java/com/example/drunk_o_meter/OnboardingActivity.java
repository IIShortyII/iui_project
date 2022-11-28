package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.BASELINE_TYPING_SAMPLES;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.function.IntBinaryOperator;

public class OnboardingActivity extends AppCompatActivity {
    private String stage;
    private int baselineTextCount;

    private TextView baselineTextView;
    private ProgressBar progressBar;
    private TextView progressLabel;
    private Button continueToDrunkometer;
    private EditText hiddenInput;

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
     * @param stage the stage the user is in ("username" or "baseline"
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
            updateBaselineContent(BASELINE_TYPING_SAMPLES.size() + 1);

       }
        }

    /**
     * Update content in baseline calculation phase depending on baselineCount
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateBaselineContent(int baselineCount) {
        // Determine baselineTextCount according to userData
        this.baselineTextCount = baselineCount;
        if(baselineTextCount <= getResources().getInteger(R.integer.baselineCount)){
        setBaselineText(baselineTextCount);
        this.progressBar.setProgress(this.baselineTextCount, true);
        this.progressLabel.setText(baselineTextCount + "/10");

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
            baselineTextView.setText(getResources().getString(textId));

    }

    /**
     * Proceed to DrunkometerActivity
     */
    public void continueToDrunkometer(View view) {
        Intent intent = new Intent(OnboardingActivity.this, DrunkometerActivity.class);
        OnboardingActivity.this.startActivity(intent);
    }

    // TODO: handle keyboard input
    // https://stackoverflow.com/questions/3349121/how-do-i-use-inputfilter-to-limit-characters-in-an-edittext-in-android
}
