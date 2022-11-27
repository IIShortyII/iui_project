package com.example.drunk_o_meter.nlp;
/**
 * The TypingSample class holds a typing sample of the user, consisting of the original text, the
 * sample the user has typed, and the time it took the user to type the text.
 */
public class TypingSample {

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

    public int getTime() {
        return time;
    }

    /**
     * The time it took the user to type the text in milliseconds
     */
    private int time;

    public TypingSample(String text, String sample, int time) {
        this.text = text;
        this.sample = sample;
        this.time = time;
    }

}
