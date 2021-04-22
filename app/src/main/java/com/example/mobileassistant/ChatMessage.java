package com.example.mobileassistant;

// This class is used to store information about a user/bot message
public class ChatMessage {
    private boolean isUser;
    private String content;

    // Constructor used to create a message
    public ChatMessage(String message, boolean user) {
        content = message;
        isUser = user;
    }

    // Returns the content stored within the message (currently only allows text)
    public String getContent() {
        return content;
    }

    // Sets the message content
    public void setContent(String content) {
        this.content = content;
    }

    // Returns whether or not a message is intended to be used for the user or the bot
    public boolean isUser() {
        return isUser;
    }

    // Sets the isUser attribute
    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

}
