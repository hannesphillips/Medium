package com.example.calvinkwan.medium20;

import java.util.Date;


public class publicMessageChat {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public String left;

    public publicMessageChat(String left, String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.left = left;

        messageTime = new Date().getTime();
    }

    public publicMessageChat() {
    }

//    public publicMessageChat(boolean left, String message)
//    {
//        super();
//        this.left = left;
//        this.messageText = message;
//
//    }
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }
    public String getMessageId() {
        return left;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
