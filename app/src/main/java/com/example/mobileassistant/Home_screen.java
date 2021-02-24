package com.example.mobileassistant;

import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.StrictMode;
import android.text.TextUtils;
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

    // Global Variables
    // chatFlag is used for when the bot is asking the user a question and so that they can keep
    // the same conversation going.
    private int chatFlag = 0;

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
                ChatMessage chatMessage = new ChatMessage(message, true);
                chatMessageAdapter.add(chatMessage);

                if (TextUtils.isEmpty(message)) {
                    return;
                }

                if (chatFlag == 0){
                    sendUserMessage(message);
                }
                else{
                    findChatFlag(message, chatFlag);
                }

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

    // Copies assets to the external storage on the device
    // The String path refers to the folder relative to the assets folder
    // An empty string ("") will copy all the assets
    // Currently the directory on the phone is storage/emulated/0/Android/data/com.example.mobileassistant/files
    private void copyAsset(String path) {
        AssetManager manager = getAssets();

        // If we have a directory, we make it and recurse. If a file, we copy its contents.
        try {
            String[] contents = manager.list(path);

            if (contents == null || contents.length == 0)
                throw new IOException();

            // Creates a new directory
            File dir = new File(getExternalFilesDir(null), path);
            dir.mkdirs();

            // Recursively copies subfolders
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        } catch (IOException e) {
            copyFileAsset(path);
        }
    }

    // Copies the individual files within the assets directory
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
        int action = checkForAction(message);
        if (action == 1) {
            //new CalendarAsyncTask(AccessCalendar).execute();
            Intent intent = new Intent(this, AccessCalendar.class);
            startActivity(intent);
        }
        else if(action == 2)
        {
            //if the message contains the word "search", send it to gsearch, if not, continue
            gsearch a = new gsearch();
            sendBotMessage(a.doInBackground(message));
        }
        else if (action == 3){
            confirmWeatherAction(message);
        }
        else if(action == 4){
            MapLauncher mapLauncher = new MapLauncher(Home_screen.this);
            mapLauncher.openDirections(message);
        }
        else if(action == 5){
            confirmNewsAction(message);
        }
        // Opens the Google Maps app at the directions page to a certain location
        else if (message.toLowerCase().contains("directions to")){
            MapLauncher mapLauncher = new MapLauncher(Home_screen.this);
            mapLauncher.openDirections(message);
        }
        else
        {
            sendBotMessage(chat.multisentenceRespond(message));
            if(chat.multisentenceRespond(message).equals("Im not sure about that one."))
            {
                gsearch b = new gsearch();
                sendBotMessage(b.doInBackground(message));
            }
        }
    }

    // Sends the bot's message to the chatListView
    private void sendBotMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        chatMessageAdapter.add(chatMessage);
    }

    // Checks for which action to take
    private int checkForAction(String message){
        String[] eventKeywords = {"show event"};
        String[] searchKeywords = {"search", "look up"};
        String[] weatherKeywords = {"weather", "forecast", "temperature"};
        String[] mapKeywords = {"directions to"};
        String[] newsKeywords = {"news", "headlines"};

        // I put them all in one for loop because I did not want to have 3 separate for loops
        // Be sure to use the array with the highest length
        // 0 = None, 1 = google calendar, 2 = google search, 3 = weather search
        for(int i = 0; i < weatherKeywords.length; i++){

            // Event keywords
            if (i < eventKeywords.length && message.toLowerCase().contains(eventKeywords[i]))
                return 1;

            // Search keywords
            if (i < searchKeywords.length && message.toLowerCase().contains(searchKeywords[i]))
                return 2;

            // Weather
            if (message.toLowerCase().contains(weatherKeywords[i]))
                return 3;

            // Map keywords
            if (i < mapKeywords.length && message.toLowerCase().contains(mapKeywords[i]))
                return 4;

            // News
            if (i < newsKeywords.length && message.toLowerCase().contains(newsKeywords[i]))
                return 5;
        }

        return 0;
    }

    // This method will confirm if the user request for the right weather location.
    private boolean isCurrentWeather = true;
    public int weatherAction = 0;
    public int newsAction = 0;
    private WeatherFetcher weatherFetcher = new WeatherFetcher();
    private void confirmWeatherAction(String message){
        chatFlag = 3; // Lets bot know to keep the conversation going
        String botMessage = "";

        if (weatherAction == 0) {
            botMessage = "Did you want the current weather or an 8-day forecast?";
            weatherAction++;

            sendBotMessage(botMessage);
        }
        else if (weatherAction == 1){
            if (message.toLowerCase().contains("current"))
                isCurrentWeather = true;
            else
                isCurrentWeather = false;

            botMessage = "What is your City and State?";
            weatherAction++;

            sendBotMessage(botMessage);
        }
        else{
            // Still need to convert state name to abbreviated state name
            // eg: South Carolina -> SC
            String location = message.replace(" ", "");

            //reset globals
            weatherAction = 0;
            chatFlag = 0;

            WeatherFetcher weatherFetcher = new WeatherFetcher();
            sendBotMessage(weatherFetcher.doInBackground(location,isCurrentWeather, this));
        }




    }
    private void confirmNewsAction(String message){
        chatFlag=5;
        String botMessage="";

        if(newsAction==0)
        {
            botMessage="What type of news would you like? If you would like to know the news for a city, Columbia SC for example, then say Columbia SC News. You can also specify the number of results by including something like 'top 5' or '5 results' in your message.";
            newsAction++;
            sendBotMessage(botMessage);
        }
        else
        {
            newsAction=0;
            chatFlag=0;
            gsearch c = new gsearch();
            botMessage = c.doInBackground(message);
            sendBotMessage(botMessage);
        }
    }

    // This will take us to which conversation they are currently in
    private void findChatFlag(String message, int num){
        // 1 = Google calendar, 2 = Google search, 3 = weather, 4 = news

        if (num == 1){

        }
        else if(num == 2){

        }
        else if(num == 3){
            confirmWeatherAction(message);
        }
        else if(num == 5){
            confirmNewsAction(message);
        }
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