package com.example.mobileassistant;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    // variables used for calendar
    int LAUNCH_CALENDAR = 2; // launch calendar request
    String result; // result to intend AccessCalendar activity for result
    static String eventRequest; // user commands
    static int number; // number of events user wants to see
    static String id; // event label
    static String location; // event location
    static String attend; // event attendees
    static boolean create = false; // create event/ search event
    private int createEventsAction = -1; // used to keep conversation going


    // Global Variables
    // chatFlag is used for when the bot is asking the user a question and so that they can keep
    // the same conversation going.
    private int chatFlag = 0;
    private boolean game2IsOn;

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
    private Weather weather = new Weather(this);
    private void sendUserMessage(String message) {
        eventRequest = message;  // used for events commands
        int action = checkForAction(message);
        if (game2IsOn) {
            action = 8;
        }
        // search event
        if (action == 1) {
            //System.out.println("the request is: " + eventRequest);
            //System.out.println("Action 1! Go to show event request function!");
            searchEvents(message);
        }

        // create event
        else if (action == 2) {
            //System.out.println("the request is: " + eventRequest);
            //System.out.println("Action 2! Go to create event request function!");
            // check if user entered event label correctly
            if (message.contains("\"")) {
                String[] eventInfo = message.split("\"");
                id = eventInfo[1];
                createEventsAction = 0;
                createEvents(message);
            }
            else {
                sendBotMessage("Please re-enter the request with the event label inside double quotes");
                createEventsAction = -1;
                id = null;
            }
        }

        // google search
        else if(action == 3)
        {
            //if the message contains the word "search", send it to gsearch, if not, continue
            gsearch a = new gsearch();
            sendBotMessage(a.doInBackground(message));
        }

        // weather
        else if (action == 4 || action == 9){
            runWeatherSearch(message);
        }

        // Opens the Google Maps app at the directions page to a certain location
        else if (action == 5){
            MapLauncher mapLauncher = new MapLauncher(Home_screen.this);
            mapLauncher.openDirections(message);
        }

        // default bot responds
        else if(action == 6){
            confirmNewsAction(message);
        }

        // Launches the Google MapActivity for traffic
        else if (action == 7) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        //starts a new game
        else if (action == 8) {
            confirmGamesAction(message);
        } else {
            sendBotMessage(chat.multisentenceRespond(message));
            if (chat.multisentenceRespond(message).equals("Im not sure about that one.")) {
                gsearch b = new gsearch();
                sendBotMessage(b.doInBackground(message));
            }
        }
    }

        // Add an else if and check if user replies with "weather"
        //chatMessage.getContent().toLowerCase().contains(("weather"))


    // Sends the bot's message to the chatListView
    private void sendBotMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        chatMessageAdapter.add(chatMessage);
    }


    /**
     * used for get results from another activity (communicate between activities)
     * get results from AccessCalendar activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if request for access calendar, check for result and if result is correct
        // then get result from AccessCalendar activity by using the keyname "feedback"
        if (requestCode == LAUNCH_CALENDAR) {
            if(resultCode == RESULT_OK) {
                result = data.getStringExtra("feedback");
                // send the result into the chat box
                sendBotMessage(result);
            }
            else {
                // if cannot get the result, gives user feedback
                Toast.makeText(this, "Something is not right", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Checks for which action to take
    private int checkForAction(String message){
        String[] eventKeywords = {"event","events"};
        String[] searchKeywords = {"search", "look up"};
        String[] weatherKeywords = {"weather", "forecast", "temperature", "high", "low", "wind speed", "humidity", "description", "pressure"};
        String[] mapKeywords = {"directions to", "directions"};
        String[] newsKeywords = {"news", "headlines"};
        String[] trafficKeywords = {"traffic", "show traffic"};
        String[] gameKeywords = {"game 1", "rock paper scissors"}; //still need game 1

        // I put them all in one for loop because I did not want to have 3 separate for loops
        // Be sure to use the array with the highest length
        // 0 = None, 1 = search events, 2 = create events, 3 = google search, 4 = weather search
        for(int i = 0; i < weatherKeywords.length; i++){

            // Show/Create Event keywords
            if (i < eventKeywords.length && message.toLowerCase().contains(eventKeywords[i])) {
                if (message.toLowerCase().contains("show")) {
                    System.out.println("show event request! go to action 1");
                    return 1;
                }
                else if (message.toLowerCase().contains("create")) {
                    System.out.println("create event request! go to action 2");
                    return 2;
                }
            }

            // Search keywords
            if (i < searchKeywords.length && message.toLowerCase().contains(searchKeywords[i]))
                return 3;

            // Weather keywords
            if (message.toLowerCase().contains(weatherKeywords[i]) && (message.toLowerCase().contains("what is") || message.toLowerCase().contains("how is")))
                return 9;

            if (message.toLowerCase().contains("weather"))
                return 4;

            // Map keywords
            if (i < mapKeywords.length && message.toLowerCase().contains(mapKeywords[i]))
                return 5;

            // News
            if (i < newsKeywords.length && message.toLowerCase().contains(newsKeywords[i]))
                return 6;

            // Traffic keywords
            if (i < trafficKeywords.length && message.toLowerCase().contains(trafficKeywords[i]))
                return 7;

            // Games
            if (i < gameKeywords.length && message.toLowerCase().contains(gameKeywords[i]))
                return 8;
        }
        return 0;
    }

    // Run weather search
    public void runWeatherSearch(String message){
        String botMessage = weather.weatherSearch(message);
        sendBotMessage(botMessage);
        chatFlag = weather.getChatFlag();
    }

    public int newsAction = 0;
    private void confirmNewsAction(String message){
        chatFlag=6;
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
        // 1 = show events, 2 = create events, 3 = Google search,
        // 4 = weather, 5 = map search, 6 = news

        if (num == 1){
            //System.out.println("the chatFlag is: "+num+" go to search event");
            searchEvents(message);
        }
        else if(num == 2){
            //  System.out.println("the chatFlag is: "+num+" go to create event");
            createEvents(message);
        }
        else if(num ==3) {

        }
        else if(num ==4) {
            runWeatherSearch(message);
        }
        else if(num == 5){

        }
        else if(num == 6) {
            confirmNewsAction(message);
        }
        else if(num == 7){

        }
        else if(num == 8){
            confirmGamesAction(message);
        }
        }

    // Method for show events
    private void searchEvents(String message) {
        create = false;
        //System.out.println("search request is: "+message);
        //System.out.println("searching event");

        // get event label or get number of requested events
        if (message.contains("\"")) {
            String[] eventInfo = message.split("\"");
            id = eventInfo[1];
        } else {
            id = null;
            char[] findNum = message.toCharArray(); // convert string to char array
            StringBuilder newStr = new StringBuilder(); // StringBuilder to build a String
            for (char c : findNum) {
                if (Character.isDigit(c)) {
                    newStr.append(c);
                }
            }
            // by default, just show the next upcoming event
            if (newStr.toString().isEmpty())
                number = 1;
            else number = Integer.parseInt(newStr.toString());
        }
        //System.out.println("The id is: "+id);
        // start AccessCalendar activity to get results
        Intent intent = new Intent(this, AccessCalendar.class);
        startActivityForResult(intent, LAUNCH_CALENDAR);
    }

    //private int createEventsAction = 0;
    //private boolean loca = false;
    // Method for create events
    private void createEvents(String message) {
        chatFlag = 2; // Lets bot know to keep the conversation going
        String botMessage = "";
        create = true;
        //System.out.println("the create request is: "+message);
        //System.out.println("Creating event");

        // at 0 ask if want to add location, goes to 1
        if (createEventsAction == 0) {
            botMessage = ("Do you want to add a location? yes/no");
            createEventsAction++;
            sendBotMessage(botMessage);
        }

        // at 1 check yes/no, if yes ask to enter location then goes to 2
        // if no, goes to 3
        else if (createEventsAction == 1) {
            if (message.equalsIgnoreCase("yes")) {
                //loca = true;
                botMessage = "Please enter the location";
                createEventsAction++;  // goes to to ask for attendees
                sendBotMessage(botMessage);
            }
            else {
                //loca = false;
                location = null;
                botMessage = "Do you want to add attendees? yes/no";
                createEventsAction = createEventsAction+2;
                //System.out.println("in 1:" + createEventsAction);
                sendBotMessage(botMessage);
            }
        }

        // at 2, save location, goes to 3
        // ask if want to add attendees
        else if (createEventsAction == 2) {
            location = message;
            createEventsAction++;
            botMessage = "Do you want to add attendees?";
            // createEventsAction++;
            // System.out.println("in 2:" +createEventsAction);
            sendBotMessage(botMessage);
        }

        // at 3, check yes/no
        // if yes, ask to enter attendees, goes to last one
        // if no, create events
        else if (createEventsAction == 3) {
            if(message.equalsIgnoreCase("yes")) {
                botMessage = "Please enter list of attendees' emails, separate by comma";
                createEventsAction++;
                sendBotMessage(botMessage);
            }
            else {
                attend = null;
                //reset globals
                createEventsAction = 0;
                chatFlag = 0;

                Intent intent = new Intent(this,AccessCalendar.class);
                startActivityForResult(intent,LAUNCH_CALENDAR);
            }
        }

        // at last one, reset variables, save attendees info, and create events
        else if (createEventsAction == 4) {
            attend = message;

            //reset globals
            createEventsAction = 0;
            chatFlag = 0;

            Intent intent = new Intent(this,AccessCalendar.class);
            startActivityForResult(intent,LAUNCH_CALENDAR);
        }
    }
    public int gameAction = 0;
    private void confirmGamesAction (String message){
        chatFlag = 8;
        if (message.equalsIgnoreCase("no")) {
            gameAction = 2;
        }
        if (message.equalsIgnoreCase("yes")) {
            gameAction = 0;
        }
        if (gameAction == 0) {
            game2IsOn = true;
            sendBotMessage("To make a move, enter rock, paper, or scissors.");
            gameAction++;
        } else if (gameAction == 1) {
            chatFlag = 0;
            rockPaperScissors();
        } else {
            chatFlag = 0;
            sendBotMessage("Thank you for playing");
            game2IsOn = false;
            gameAction = 0;
        }
    }

    public void rockPaperScissors () {
        RockPaperScissors rps = new RockPaperScissors();
        String message = rps.checkUserGesture(messageEditText.getText().toString());
        if (message.equalsIgnoreCase("Invalid input")) {
            sendBotMessage(message);
        }
        String botMove = rps.botMove();
        sendBotMessage("Your gesture is: " + message);
        sendBotMessage("My gesture is: " + botMove);
        sendBotMessage(rps.result(message, botMove));
        sendBotMessage("Do you still want to play? Enter yes or no");
        chatFlag = 0;
    }
    //switch to other screens
    // Method for opening Profile screen
    public void open_Profile_screen () {
        Intent intent = new Intent(this, Profile_screen.class);
        startActivity(intent);
    }

    // Method for opening Settings screen
    public void open_Settings_screen () {
        Intent intent = new Intent(this, Settings_screen.class);
        startActivity(intent);
    }
}