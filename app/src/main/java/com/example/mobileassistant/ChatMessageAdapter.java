package com.example.mobileassistant;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// A class that extends Android's ArrayAdapter and is used to display chat messages in the chatListView on the Home Screen
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage>{

    private static final int USER_MESSAGE = 0, BOT_MESSAGE = 1;

    // Calls the superclass (ArrayAdapter)'s constructor to create a layout of messages
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.activity_user_message, data);
    }

    // Overrides the BaseAdapter class from Android - returns the number of different types of messages
    @Override
    public int getViewTypeCount() {
        // Stores the number of different types of messages, currently there are only user and bot
        return 2;
    }

    // Determines which type of message will be displayed
    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);
        if (item.isUser())
            return USER_MESSAGE;
        else
            return BOT_MESSAGE;
    }

    // Creates the new view containing the ChatMessage that will be displayed in the chatListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == USER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_bot_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }
        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action when a chat message is clicked - currently unused
            }
        });
        return convertView;
    }

    public ArrayList<ChatMessage> getAllItems(){
        ArrayList<ChatMessage> result = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            result.add(getItem(i));
        }
        return result;
    }
}
