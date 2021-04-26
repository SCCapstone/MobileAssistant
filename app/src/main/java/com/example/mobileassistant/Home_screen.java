package com.example.mobileassistant;


import android.app.Activity;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.data.DataHolder;
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
import java.util.Calendar;


public class  Home_screen extends AppCompatActivity {


    // set up inactivity recording && animation view
    Handler handler;
    Runnable r;
    boolean notTalking = true;
    // sharedPreferences used to save light or dark mode
    SharedPreferences sharedPreferences;
    boolean darkMode;

    // Attributes used for the buttons to switch between screens
    private Button button_profile;
    private Button button_home;
    private Button button_settings;

    // ACCOUNT PREFERENCES
    private static final String ACCOUNT_PREFERENCES = "ACCOUNT";
    private static final String ACCOUNT_FIRST_NAME = "FIRST_NAME";
    private static final String ACCOUNT_LAST_NAME = "LAST_NAME";
    private static final String ACCOUNT_DOB = "DOB";

    // Attributes used for sending messages
    private ListView chatListView;
    private FloatingActionButton btnSend;
    private EditText messageEditText;
    private ChatMessageAdapter chatMessageAdapter;
    private ArrayList<ChatMessage> chatMessages;

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
    public int createEventsAction = -1; // used to keep conversation going
    public int HelpActionIter = -1; //used to discern where the user is in the help command

    // Key value for String that is used in the traffic intent setup
    private static final String EXTRA_MESSAGE = "com.example.mobileassistant.MESSAGE";

    // Global Variables
    // chatFlag is used for when the bot is asking the user a question and so that they can keep
    // the same conversation going.
    private int chatFlag = 0;
    private int firstNews = 0;
    private boolean game2IsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        if (!preferences.getBoolean("onboarding_complete", false)) {

            Intent onboarding = new Intent(this, OnboardingActivity.class);
            startActivity(onboarding);

            finish();
            return;
        }

        // get mode of the app night/light
        sharedPreferences = getSharedPreferences("NIGHT", MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("isDarkModeOn", false);

        setAnimationView(0); // default off animation view

        //hoping that this fixes network errors for gsearch
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);

        // Button to swap to Profile screen
        button_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                open_Profile_screen(); // opens Profile class/screen
            }
        });

        // Button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                open_Settings_screen(); // opens Settings class/screen
            }
        });

        // Getting variables for attributes to send messages
        chatListView = findViewById(R.id.chatListView);
        btnSend = findViewById(R.id.button_send);
        messageEditText = findViewById(R.id.messageEditText);

        //check to see whether there are any previous chat history;
        // if not, initialize a new instance of an arraylist to store chat messages
        //get intent extra is used when another intent starts this one
        //saved instance state is used when the activity is destroyed and restarted
       if (savedInstanceState == null) {
           if(getIntent().getParcelableArrayListExtra("chatMessages") != null){
               chatMessages = getIntent().getParcelableArrayListExtra("chatMessages");
           }else {
               chatMessages = new ArrayList();
           }
        } else{
           chatMessages = savedInstanceState.getParcelableArrayList("chatMessages");
       }

       //tie together the listview with chat messages
        chatMessageAdapter = new ChatMessageAdapter(this,chatMessages);
        chatListView.setAdapter(chatMessageAdapter);

        //if there are more than 0 chat messages, then set the last message as the one just above the input box
        if(chatMessageAdapter.getCount() > 0) {
            chatListView.setSelection(chatMessageAdapter.getCount() - 1);
        }

        // change animation view from "off" to "start" then "on"
        messageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnimationView(3);
            }
        });

        messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    setAnimationView(3);
                hideKeyboard(v);
            }
        });

        // Sends the message when the button is clicked
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                ChatMessage chatMessage = new ChatMessage(message, true);
                chatMessageAdapter.add(chatMessage);

                if (messageEditText.getText().toString() == "") {
                    return;
                }

                if (chatFlag == 0) {
                    sendUserMessage(message);
                } else {
                    findChatFlag(message, chatFlag);
                }

                messageEditText.setText("");
                chatListView.setSelection(chatMessageAdapter.getCount() - 1);
                chatMessageAdapter.notifyDataSetChanged();
                hideKeyboard(messageEditText);
            }
        });

        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    btnSend.callOnClick();
                }
                return false;
            }
        });

        // Used to copy the assets from the assets/bots/ folder to Android's external storage
        copyAsset("bots");

        // Gets the working directory for the AIML files
        // The external storage directory leads to a virtual SD card where the AIML assets are copied to
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.mobileassistant/files";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Alice", MagicStrings.root_path, "chat");
        chat = new Chat(bot);


        // set up no activity handler to switch to "sleeping" mode when there is no activity for 30 secs
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // resize sleeping animation view to a smaller size while there is a chat
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        10.0f
                );
                if (darkMode) {
                    robotOffAnimationView_dark.setLayoutParams(param);
                } else robotOffAnimationView_light.setLayoutParams(param);
                setAnimationView(0);
            }
        };
        startHandler();

        // Set first/last name through bot
        SharedPreferences accPref = getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);
        String accountFirstName = accPref.getString(ACCOUNT_FIRST_NAME, null);
        String accountLastName = accPref.getString(ACCOUNT_LAST_NAME, null);
        String accountDOB = accPref.getString(ACCOUNT_DOB, null);

        // Get age
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int birthYear = Integer.parseInt(accountDOB.substring(accountDOB.length()-4));

        // Send to bot
        chat.multisentenceRespond("my first name is " + accountFirstName);
        chat.multisentenceRespond("my last name is " + accountLastName);
        chat.multisentenceRespond("my name is " + accountFirstName + " " + accountLastName);
        chat.multisentenceRespond("my birthday is " + accountDOB.replace("/", " "));
        chat.multisentenceRespond("I was born on " + accountDOB.replace("/", " "));
        chat.multisentenceRespond("my age is " + (year - birthYear));

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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


    // Sends the user's message to the chatListView
    // To debug, call the bot's message to reply in a default way
    private Weather weather = new Weather(this);

    //redirects to a specific function implemented in our bot by classified user input from checkForAction()
    public void sendUserMessage(String message) {
        notTalking = false; // used to set up corresponding animation mode
        eventRequest = message;  // used for events commands
        int action = checkForAction(message);

        //redirects to rock paper scissors
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
            } else {
                sendBotMessage("Please re-enter the request with the event label inside double quotes");
                createEventsAction = -1;
                id = null;
            }
        }

        // google search
        else if (action == 3) {
            gsearch a = new gsearch();
            sendBotMessage(a.doInBackground(message));
            if ((message.contains("news") || message.contains("headlines")) && firstNews == 0) {
                String botMessage = "Your results are above. You can search for specific news or general news. For example, if you would like news about Columbia SC, simply type in Columbia SC News. You can also specify the number of results by including something like 'top 5' or '5 results' in your message.";
                sendBotMessage(botMessage);
                firstNews = 1;
            }
        }

        // weather
        else if (action == 4 || action == 9) {
            runWeatherSearch(message);
        }

        // Opens the Google Maps app at the directions page to a certain location
        else if (action == 5) {
            MapLauncher mapLauncher = new MapLauncher(Home_screen.this);
            mapLauncher.openDirections(message);
        }

        // Launches the Google MapActivity for traffic near the user's last known location
        else if (action == 7) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        }

        //starts a new game
        else if (action == 8) {
            confirmGamesAction(message);
        } else if (action == 10) {
            HelpActionIter = 0;
            confirmHelpAction(message);
        }

        // Launches the Google MapsActivity for traffic near a place
        else if (action == 11) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        } else {
            String ph = chat.multisentenceRespond(message);
            sendBotMessage(ph);
            if (ph.equals("Im not sure about that one.") || ((ph.contains("Google") || ph.contains("search")) && !ph.startsWith("I can help you create"))) {
                if (ph.startsWith("I might be a") || ph.startsWith("I can help you")) {

                } else {
                    gsearch b = new gsearch();
                    sendBotMessage(b.doInBackground(message));
                }
            }
        }
    }

    // Sends the bot's message to the chatListView
    public void sendBotMessage(String message) {
        setAnimationView(2);  // talking animation view
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
            if (resultCode == RESULT_OK) {
                result = data.getStringExtra("feedback");
                // send the result into the chat box
                sendBotMessage(result);
            } else {
                // if cannot get the result, gives user feedback
                Toast.makeText(this, "Something is not right", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Checks for which action to take
    public int checkForAction(String message) {
        String[] eventKeywords = {"event", "events"};
        String[] searchKeywords = {"search", "look up", "news", "headlines"};
        String[] weatherKeywords = {"weather", "forecast", "temperature", "high", "low", "wind speed", "humidity", "description", "pressure"};
        String[] mapKeywords = {"directions to", "directions"};
        String[] trafficKeywords = {"traffic", "show traffic"};
        String[] trafficPlaceKeywords = {"traffic in", "traffic to", "traffic near"};
        String[] gameKeywords = {"game 1", "rock paper scissors"};
        String[] helpKeywords = {"help", "tutorial", "instructions", "what can you do", "commands", "functions"};

        // I put them all in one for loop because I did not want to have 3 separate for loops
        // Be sure to use the array with the highest length
        // 0 = None, 1 = search events, 2 = create events, 3 = google search, 4 = weather search
        for (int i = 0; i < weatherKeywords.length; i++) {

            // Show/Create Event keywords
            if (i < eventKeywords.length && message.toLowerCase().contains(eventKeywords[i])) {
                if (message.toLowerCase().contains("show")) {
                    // System.out.println("show event request! go to action 1");
                    return 1;
                } else if (message.toLowerCase().contains("create")) {
                    // System.out.println("create event request! go to action 2");
                    return 2;
                }
            }

            // Search keywords
            if (i < searchKeywords.length && message.toLowerCase().contains(searchKeywords[i]))
                return 3;

            // Weather keywords
            if (message.toLowerCase().contains(weatherKeywords[i]) && (message.toLowerCase().contains("what is")
                    || message.toLowerCase().contains("what's")
                    || message.toLowerCase().contains("how is")
                    || message.toLowerCase().contains("how's")
                    || message.toLowerCase().contains("what will")))
                return 9;
            else if (message.toLowerCase().contains("weather") || message.toLowerCase().contains("forecast"))
                return 4;

            // Map keywords
            if (i < mapKeywords.length && message.toLowerCase().contains(mapKeywords[i]))
                return 5;

            // Traffic keywords
            if (i < trafficKeywords.length && message.toLowerCase().contains(trafficKeywords[i])) {
                // If the keywords contain traffic keywords for a place, return 11 for that intent
                for (int j = 0; j < trafficPlaceKeywords.length; ++j) {
                    if (message.toLowerCase().contains(trafficPlaceKeywords[j])) {
                        return 11;
                    }
                }
                // Otherwise, return 7 for the intent for local traffic
                return 7;
            }

            // Games
            if (i < gameKeywords.length && message.toLowerCase().contains(gameKeywords[i]))
                return 8;

            if (i < helpKeywords.length && message.toLowerCase().contains(helpKeywords[i])) {
                return 10;
            }
        }
        return 0;
    }

    // Run weather search
    public void runWeatherSearch(String message) {
        String botMessage = weather.weatherSearch(message);
        sendBotMessage(botMessage);
        chatFlag = weather.getChatFlag();
    }

    // This will take us to which conversation they are currently in
    public void findChatFlag(String message, int num) {
        // 1 = show events, 2 = create events, 3 = Google search,
        // 4 = weather, 5 = map search, 6 = news

        if (num == 1) {
            searchEvents(message);
        } else if (num == 2) {
            createEvents(message);
        } else if (num == 4) {
            runWeatherSearch(message);
        } else if (num == 8) {
            confirmGamesAction(message);
        } else if (num == 10) {
            confirmHelpAction(message);
        }
    }

    // Method for show events
    public void searchEvents(String message) {
        create = false;

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
            // search for event, by default, just show the next upcoming event
            if (newStr.toString().isEmpty() && message.toLowerCase().contains("events"))
                number = 5;
                // search for events, by default, show the next 5 upcoming events
            else if (newStr.toString().isEmpty() && message.toLowerCase().contains("event"))
                number = 1;
                // get the number of events request
            else number = Integer.parseInt(newStr.toString());
        }
        // start AccessCalendar activity to get results
        Intent intent = new Intent(this, AccessCalendar.class);
        startActivityForResult(intent, LAUNCH_CALENDAR);
    }

    // Method for create events
    public void createEvents(String message) {
        chatFlag = 2; // Lets bot know to keep the conversation going
        String botMessage = "";
        create = true;

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
            } else {
                //loca = false;
                location = null;
                botMessage = "Do you want to add attendees? yes/no";
                createEventsAction = createEventsAction + 2;
                sendBotMessage(botMessage);
            }
        }

        // at 2, save location, goes to 3
        // ask if want to add attendees
        else if (createEventsAction == 2) {
            location = message;
            createEventsAction++;
            botMessage = "Do you want to add attendees? yes/no";
            // createEventsAction++;
            sendBotMessage(botMessage);
        }

        // at 3, check yes/no
        // if yes, ask to enter attendees, goes to last one
        // if no, create events
        else if (createEventsAction == 3) {
            if (message.equalsIgnoreCase("yes")) {
                botMessage = "Please enter list of attendees' emails, separate by comma";
                createEventsAction++;
                sendBotMessage(botMessage);
            } else {
                attend = null;
                //reset globals
                createEventsAction = 0;
                chatFlag = 0;

                Intent intent = new Intent(this, AccessCalendar.class);
                startActivityForResult(intent, LAUNCH_CALENDAR);
            }
        }

        // at last one, reset variables, save attendees info, and create events
        else if (createEventsAction == 4) {
            attend = message;

            //reset globals
            createEventsAction = 0;
            chatFlag = 0;

            Intent intent = new Intent(this, AccessCalendar.class);
            startActivityForResult(intent, LAUNCH_CALENDAR);
        }
    }


    //determines how the chat progresses the game
    public int gameAction = 0;
    public void confirmGamesAction(String message) {
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

    //the chat will continue to return to this command so long as the help action has not been ended.
    public void confirmHelpAction(String message) {
        chatFlag = 10;
        if (HelpActionIter == 0) {
            String botMessage = "I can help you with understanding what I can do, if you would like. I have a variety of capabilities that you can learn about including event management, weather, news, and many others. For info on events, please type events. For weather, please type weather. For news or general searching, please type search. For traffic and maps, please type map. Finally, if you would like to see the tutorial which you received when you first opened the app, you can type tutorial to open it. When you no longer need help, simply type exit.";
            HelpActionIter++;
            sendBotMessage(botMessage);
            return;
        }
        //if this is not the first message, one of the responses will be one of these.
        if (HelpActionIter == 1) {
            if (message.toLowerCase().contains("events")) {
                String botMessage = "I can help you create and check your events within my app! To create an event, type create event. I will then ask you a series of questions about the event you wish to create. To show your already made events, type show event. Make sure that your google account is linked to my app!";
                sendBotMessage(botMessage);
            } else if (message.toLowerCase().contains("weather")) {
                String botMessage = "If you would like information on the weather in your area or in any area, simply type weather or something like \"What is the forecast in Columbia, South Carolina?\" This will trigger my weather protocol and I can help you find specific or general information about any city you wish!";
                sendBotMessage(botMessage);
            } else if (message.toLowerCase().contains("search")) {
                String botMessage = "This is my default response if it does not seem like we are conversing, but I do not understand what you would like me to do. You can manually initiate a search by including the word search in your message. You can also specify the number of results, by including a stipulation such as \"top 5\" or \"5 results\" in your message to me. Asking me for news works in a similar way, you should include the word news, or headlines, in your request.";
                sendBotMessage(botMessage);
            } else if (message.toLowerCase().contains("map")) {
                String botMessage = "You can ask me for directions to a location near (or far from) you. To do so, simply include \"directions to\" in your request, along with the location which you would like to reach. You can also ask for traffic in your area by just including \"traffic\" or traffic around a certain place by asking for \"traffic near/to/in\" a place.";
                sendBotMessage(botMessage);
            } else if (message.toLowerCase().contains("tutorial")) {
                Intent onboarding = new Intent(this, OnboardingActivity.class);
                startActivity(onboarding);

                finish();
                return;
            } else if (message.toLowerCase().contains("exit")) {
                String botMessage = "Let me know if you need help again! Just type \"help\" at any time!";
                HelpActionIter = -1;
                chatFlag = 0;
                sendBotMessage(botMessage);
            } else {
                String botMessage = "I did not recognize what you are asking for help with, please respond with either \"events\", \"weather\", \"search\", or \"map\". You can also type \"exit\" to exit. Thank you!";
                sendBotMessage(botMessage);
            }
        }
    }

    //implements the game
    public void rockPaperScissors() {
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


    //variables for lottie
    LottieAnimationView talkingAnimationView_dark;
    LottieAnimationView robotStartAnimationView_dark;
    LottieAnimationView robotOffAnimationView_dark;
    LottieAnimationView robotOnAnimationView_dark;
    LottieAnimationView talkingAnimationView_light;
    LottieAnimationView robotStartAnimationView_light;
    LottieAnimationView robotOffAnimationView_light;
    LottieAnimationView robotOnAnimationView_light;
    // 0: robotOff, 1: robotOn, 2:robotTalking, 3: robotStart

    // Method for pick corresponding animation view
    public void setAnimationView(int state) {
        // get all the animation views
        talkingAnimationView_dark = findViewById(R.id.talking_animationViewDark);
        robotStartAnimationView_dark = findViewById(R.id.robotStartViewDark);
        robotOffAnimationView_dark = findViewById(R.id.RobotOffViewDark);
        robotOnAnimationView_dark = findViewById(R.id.robotOnViewDark);
        talkingAnimationView_light = findViewById(R.id.talking_animationViewLight);
        robotStartAnimationView_light = findViewById(R.id.robotStartViewLight);
        robotOffAnimationView_light = findViewById(R.id.RobotOffViewLight);
        robotOnAnimationView_light = findViewById(R.id.robotOnViewLight);

        // resize talking animation view to a smaller size
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                10.0f
        );
        talkingAnimationView_light.setLayoutParams(param);
        talkingAnimationView_dark.setLayoutParams(param);

        // chat is active
        if (state == 2) {
            // Hide unnecessary animation views
            robotStartAnimationView_dark.setVisibility(View.GONE);
            robotOffAnimationView_dark.setVisibility(View.GONE);
            robotOnAnimationView_dark.setVisibility(View.GONE);
            robotStartAnimationView_light.setVisibility(View.GONE);
            robotOffAnimationView_light.setVisibility(View.GONE);
            robotOnAnimationView_light.setVisibility(View.GONE);
            if (darkMode) {
                // shows dark talking animation view
                talkingAnimationView_light.setVisibility(View.GONE);
                talkingAnimationView_dark.setVisibility(View.VISIBLE);
            } else {
                // shows light talking animation view
                talkingAnimationView_dark.setVisibility(View.GONE);
                talkingAnimationView_light.setVisibility(View.VISIBLE);
            }
        }
        // no activity for 30 secs or robot is sleeping
        else if (state == 0) {
            talkingAnimationView_dark.setVisibility(View.GONE);
            robotStartAnimationView_dark.setVisibility(View.GONE);
            robotOnAnimationView_dark.setVisibility(View.GONE);
            talkingAnimationView_light.setVisibility(View.GONE);
            robotStartAnimationView_light.setVisibility(View.GONE);
            robotOnAnimationView_light.setVisibility(View.GONE);
            if (darkMode) {
                robotOffAnimationView_light.setVisibility(View.GONE);
                robotOffAnimationView_dark.setVisibility(View.VISIBLE);
            } else {
                robotOffAnimationView_dark.setVisibility(View.GONE);
                robotOffAnimationView_light.setVisibility(View.VISIBLE);
            }
        }

        // user click the chat, active the bot
        else if (state == 3) {
            countSwitch();
        }

        // bot is active
        else if (state == 1) {
            talkingAnimationView_dark.setVisibility(View.GONE);
            robotStartAnimationView_dark.setVisibility(View.GONE);
            robotOffAnimationView_dark.setVisibility(View.GONE);
            talkingAnimationView_light.setVisibility(View.GONE);
            robotStartAnimationView_light.setVisibility(View.GONE);
            robotOffAnimationView_light.setVisibility(View.GONE);
            if (darkMode) {
                robotOnAnimationView_light.setVisibility(View.GONE);
                robotOnAnimationView_dark.setVisibility(View.VISIBLE);
            } else {
                robotOnAnimationView_dark.setVisibility(View.GONE);
                robotOnAnimationView_light.setVisibility(View.VISIBLE);
            }

        }
        // countSwitch().cancel();
    }

    // methods for tracking inactivity
    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }

    public void startHandler() {
        handler.postDelayed(r, 30 * 1000);
    }

    // timer methods for switching "robotStart" and "robotOn" animation view
    // display "robotStart" for 5 seconds then switch to "robotOn" mode
    public void countSwitch() {
        robotOffAnimationView_dark.setVisibility(View.GONE);
        robotOffAnimationView_light.setVisibility(View.GONE);
        CountDownTimer ct = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (notTalking) {
                    robotOnAnimationView_dark.setVisibility(View.GONE);
                    talkingAnimationView_dark.setVisibility(View.GONE);
                    robotOnAnimationView_light.setVisibility(View.GONE);
                    talkingAnimationView_light.setVisibility(View.GONE);
                    if (darkMode) {
                        robotStartAnimationView_light.setVisibility(View.GONE);
                        robotStartAnimationView_dark.setVisibility(View.VISIBLE);
                    } else {
                        robotStartAnimationView_dark.setVisibility(View.GONE);
                        robotStartAnimationView_light.setVisibility(View.VISIBLE);
                    }
                } else {
                    robotStartAnimationView_dark.setVisibility(View.GONE);
                    robotOnAnimationView_dark.setVisibility(View.GONE);
                    robotStartAnimationView_light.setVisibility(View.GONE);
                    robotOnAnimationView_light.setVisibility(View.GONE);
                    if (darkMode) {
                        talkingAnimationView_light.setVisibility(View.GONE);
                        talkingAnimationView_dark.setVisibility(View.VISIBLE);
                    } else {
                        talkingAnimationView_dark.setVisibility(View.GONE);
                        talkingAnimationView_light.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (notTalking) {
                    robotStartAnimationView_dark.setVisibility(View.GONE);
                    robotStartAnimationView_light.setVisibility(View.GONE);
                    talkingAnimationView_dark.setVisibility(View.GONE);
                    talkingAnimationView_light.setVisibility(View.GONE);
                    if (darkMode) {
                        robotOnAnimationView_light.setVisibility(View.GONE);
                        robotOnAnimationView_dark.setVisibility(View.VISIBLE);
                    } else {
                        robotOnAnimationView_dark.setVisibility(View.GONE);
                        robotOnAnimationView_light.setVisibility(View.VISIBLE);
                    }

                } else {
                    robotStartAnimationView_dark.setVisibility(View.GONE);
                    robotOnAnimationView_dark.setVisibility(View.GONE);
                    robotStartAnimationView_light.setVisibility(View.GONE);
                    robotOnAnimationView_light.setVisibility(View.GONE);
                    if (darkMode) {
                        talkingAnimationView_light.setVisibility(View.GONE);
                        talkingAnimationView_dark.setVisibility(View.VISIBLE);
                    } else {
                        talkingAnimationView_dark.setVisibility(View.GONE);
                        talkingAnimationView_light.setVisibility(View.VISIBLE);
                    }
                }

            }
        }.start();
    }

    //switch to other screens
    // Method for opening Profile screen
    public void open_Profile_screen() {
        Intent intent = new Intent(this, Profile_screen.class);
        intent.putExtra("chatMessages", chatMessages);
        startActivity(intent);
    }

    // Method for opening Settings screen
    public void open_Settings_screen() {
        Intent intent = new Intent(this, Settings_screen.class);
        intent.putExtra("chatMessages", chatMessages);
        startActivity(intent);
    }

    //restores the chat messages for restart of the activity
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        chatMessages = savedInstanceState.getParcelableArrayList("chatMessages");
    }

    //saves the chat messages for the next restart of the activity
    // (will disappear once a new intent starts or the app is closed)
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("chatMessages", chatMessages);
    }

}