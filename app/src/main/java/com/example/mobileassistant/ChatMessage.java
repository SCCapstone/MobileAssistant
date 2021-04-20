package com.example.mobileassistant;

import android.os.Parcel;
import android.os.Parcelable;

// This class is used to store information about a user/bot message
public class ChatMessage implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isUser ? (byte) 1 : (byte) 0);
        dest.writeString(this.content);
    }

    public void readFromParcel(Parcel source) {
        this.isUser = source.readByte() != 0;
        this.content = source.readString();
    }

    protected ChatMessage(Parcel in) {
        this.isUser = in.readByte() != 0;
        this.content = in.readString();
    }

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}
