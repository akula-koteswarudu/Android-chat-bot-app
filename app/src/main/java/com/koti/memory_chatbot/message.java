package com.koti.memory_chatbot;

public class message {
    int whos;
    String text;

    public message(int whos, String text) {
        this.whos = whos;
        this.text = text;
    }

    public int getWhos() {
        return whos;
    }

    public String getText() {
        return text;
    }
}
