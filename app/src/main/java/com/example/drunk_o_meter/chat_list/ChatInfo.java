package com.example.drunk_o_meter.chat_list;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatInfo {
    /**
     * The date the chat message was created
     */
    public String date;

    /**
     * The time the chat message was created
     */
    public String time;

    /**
     * The content of the chat message
     */
    public String content;

    /**
     * The short content of the chat message shown in the overview
     */
    public String content_short;

    /**
     * Whether the chat message was declared as "safe to text"
     */
    public boolean safeToText;


    public ChatInfo(String content, boolean safeToText) {
        this.date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        this.time = new SimpleDateFormat("HH:mm").format(new Date());
        this.content = content;
        //TODO: Adapt number when real chats exist
        this.content_short = StringUtils.abbreviate(content, 15);
        this.safeToText = safeToText;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getContent_short() {
        return content_short;
    }

    public boolean isSafeToText() {
        return safeToText;
    }

    public void setSafeToText(boolean safeToText) {this.safeToText = safeToText; }
}
