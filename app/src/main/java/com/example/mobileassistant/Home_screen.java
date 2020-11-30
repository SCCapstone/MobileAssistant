package com.example.mobileassistant;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Home_screen extends AppCompatActivity {

    // Attributes used for the buttons to switch between screens
    private Button button_profile;
    private Button button_home;
    private Button button_settings;

    // Attributes used for sending messages
    private ListView chatListView;
    private FloatingActionButton btnSend;
    private EditText messageEditText;
    private ChatMessageAdapter chatMessageAdapter;

    //set up the bot
    public Bot bot;
    public static Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //hoping that this fixes network errors for gsearch
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if(SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);

        // Button to swap to Profile screen
        button_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Profile_screen(); // opens Profile class/screen
            }
        });

        // Button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Settings_screen(); // opens Settings class/screen
            }
        });

        // Getting variables for attributes to send messages
        chatListView = (ListView) findViewById(R.id.chatListView);
        btnSend = (FloatingActionButton) findViewById(R.id.button_send);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        chatMessageAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        chatListView.setAdapter(chatMessageAdapter);

        // Sends the message when the button is clicked
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                //bot
                String response = chat.multisentenceRespond(messageEditText.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendUserMessage(message);
                messageEditText.setText("");
                chatListView.setSelection(chatMessageAdapter.getCount() - 1);
            }
        });

        // Used to copy the assets from the assets/bots/ folder to Android's external storage
        copyAsset("bots");

        // Gets the working directory for the AIML files
        // The external storage directory leads to a virtual SD card where the AIML assets are copied to
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.mobileassistant/files";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Alice", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);
    }

    /**
     * Copy the asset at the specified path to this app's data directory. If the
     * asset is a directory, its contents are also copied.
     *
     * @param path
     * Path to asset, relative to app's assets directory.
     */
    private void copyAsset(String path) {
        AssetManager manager = getAssets();

        // If we have a directory, we make it and recurse. If a file, we copy its
        // contents.
        try {
            String[] contents = manager.list(path);

            // The documentation suggests that list throws an IOException, but doesn't
            // say under what conditions. It'd be nice if it did so when the path was
            // to a file. That doesn't appear to be the case. If the returned array is
            // null or has 0 length, we assume the path is to a file. This means empty
            // directories will get turned into files.
            if (contents == null || contents.length == 0)
                throw new IOException();

            // Make the directory.
            File dir = new File(getExternalFilesDir(null), path);
            dir.mkdirs();

            // Recurse on the contents.
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        } catch (IOException e) {
            copyFileAsset(path);
        }
    }

    /**
     * Copy the asset file specified by path to app's data directory. Assumes
     * parent directories have already been created.
     *
     * @param path
     * Path to asset, relative to app's assets directory.
     */
    private void copyFileAsset(String path) {
        File file = new File(getExternalFilesDir(null), path);
        try {
            InputStream in = getAssets().open(path);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //Request and response of user and the bot
    public static void mainFunction(String[] args) {
        MagicBooleans.trace_mode = false;
        //System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        //System.out.println("Human: "+request);
        //System.out.println("Robot: " + response);
    }
    // Sends the user's message to the chatListView
    // To debug, call the bot's message to reply in a default way
    private void sendUserMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true);
        chatMessageAdapter.add(chatMessage);
        if(chatMessage.getContent().contains("Search") || chatMessage.getContent().contains("search"))
        {
            //if the message contains the word "search", send it to gsearch, if not, continue
            gsearch a = new gsearch();
            System.out.println("printing query in home screen : " + chatMessage.getContent());
            sendBotMessage(a.doInBackground(chatMessage.getContent()));
        }
        else
        {
            sendBotMessage(chat.multisentenceRespond(message));
        }
    }

    // Sends the bot's message to the chatListView
    private void sendBotMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        chatMessageAdapter.add(chatMessage);
    }

    //switch to other screens
    // Method for opening Profile screen
    public void open_Profile_screen(){
        Intent intent = new Intent(this, Profile_screen.class);
        startActivity(intent);
    }

    // Method for opening Settings screen
    public void open_Settings_screen(){
        Intent intent = new Intent(this, Settings_screen.class);
        startActivity(intent);
    }

}