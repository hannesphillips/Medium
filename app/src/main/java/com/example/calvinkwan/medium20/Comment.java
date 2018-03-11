package com.example.calvinkwan.medium20;

/**
 * Created by Melonderr on 3/8/18.
 */

public class Comment {
    private String text, userkey, name;

    public Comment() {

    }

    public Comment(String text, String userkey, String name) {
        this.text = text;
        this.userkey = userkey;
        this.name = name;
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getUserkey() { return userkey; }

    public void setUserkey(String userkey) { this.userkey =  userkey;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
