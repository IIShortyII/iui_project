package com.example.drunk_o_meter.nlp;

import java.util.Date;

/**
 * The TextMessage class represents a text message the user types in for sentiment analysis.
 * The Drunk-o-meter also provides an archive of all analyzed text messages.
 */
public class TextMessage {

    private String recipient;
    private String message;
    private String sentimentAnalysis;
    private Date date;

    public TextMessage(String recipient, String message, String sentimentAnalysis, Date date) {
        this.recipient = recipient;
        this.message = message;
        this.sentimentAnalysis = sentimentAnalysis;
        this.date = date;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public Date getDate() {
        return date;
    }
}
