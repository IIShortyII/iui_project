package com.example.drunk_o_meter.typingChallenge;
/**
 * The TypingSample class holds a typing sample of the user, consisting of the original text, the
 * sample the user has typed, and the time it took the user to type the text.
 */
public class TypingSample {
    /**
     * The time it took the user to type the text in milliseconds
     */
    private long time;

    /**
     * The relative error the user produced in his typing the text;
     */
    private final double error;
    /**
     * The original text
     */
    private String text;

    /**
     * The sample text provided by the user
     */
    private String sample;

    public String getText() {
        return text;
    }

    public String getSample() {
        return sample;
    }

    public long getTime() {
        return time;
    }

    public double getError() {return error; }



    public TypingSample(String text, String sample, long time, double error) {
        this.text = text;
        this.sample = sample;
        this.time = time;
        this.error = error;
    }

}
