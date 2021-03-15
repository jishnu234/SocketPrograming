package com.example.asynctask;

public class ChatModel {

    String username;
    String message;
    int sender;

    public ChatModel(String username, String message, int sender) {
        this.username = username;
        this.message = message;
        this.sender = sender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
