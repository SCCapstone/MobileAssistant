package com.example.mobileassistant;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Weather Class that will be called in the Home_screen
 * This class will attempt to obtain the right information before
 * calling the WeatherFetcher Class
 */
public class Weather {
    // Varaibles
    private WeatherFetcher weatherFetcher;
    private boolean isDefaultWeather;
    private boolean is8DayForecast;
    private String info;
    public int weatherAction;
    private int chatFlag;
    private Context home_screen_context;

    /**
     * Weather contructor
     * @param home_screen_context
     */
    public Weather(Context home_screen_context){
        this.home_screen_context = home_screen_context;

        weatherFetcher = new WeatherFetcher();
        isDefaultWeather = false;
        is8DayForecast = false;
        info = "";
        weatherAction = 0;
        chatFlag = 0;
    }

    public int getChatFlag(){
        return this.chatFlag;
    }

    /**
     * Will be called in home_screen class
     * This method decided whether the user input the correct information to be parsed out.
     * Example of a correct input would be: "What is the _____ in ______"
     * Example of an incorrect input would be: "weather in _____"
     *
     * Correct input will automatically get the results back to the user.
     * Incorrect input will go to defaultWeatherSearch(...) which will ask users a series of questions
     * @param message
     * @return
     */
    public String weatherSearch(String message){
        String ret_string = "";

        /**
         * Goes here if user just types in "weather" in chat
         */
        if (message.equalsIgnoreCase("weather")  && isDefaultWeather == false){
            isDefaultWeather = true;
            ret_string += defaultWeatherSearch(message);
            return ret_string;
        }

        /**
         * if statement if for default weather asking questions and user answers back
         */
        if (isDefaultWeather){
            ret_string += defaultWeatherSearch(message);
        }
        else{
            /**
             * Tries to parse out correct information
             */
            String cap_message = message.toUpperCase();

            // Regex info
            Pattern pattern = Pattern.compile("((?<=WHAT IS THE )|(?<=HOW IS THE )|(?<=WHAT WILL ))(.+) IN (.+)"); // Create a Pattern object

            // Now create matcher object.
            Matcher matcher = pattern.matcher(cap_message);

            if (matcher.find()) {
                // Find the type of weather the user would like to know
                String weatherType = matcher.group(2);
                info = findWeatherType(weatherType);

                if (cap_message.contains("TOMORROW") || cap_message.contains("DAYS"))
                    is8DayForecast = true;

                String location = matcher.group(3); // Should be location
                ret_string = parseWeatherInformation(location);
                info = "";
            }
            else { // Could not parse information correctly, default to asking question

                ret_string += "Sorry I could not get the correct information. ";
                isDefaultWeather = true;

                ret_string += defaultWeatherSearch(message);
            }
        }

        return ret_string;
    }

    /**
     * This method is used to help figure out what the user would like.
     * ie: temperature, high, low, ...
     * @param weatherType
     * @return
     */
    public String findWeatherType(String weatherType){
        String ret_string = "";
        String cap_weatherType = weatherType.toUpperCase();

        if (cap_weatherType.contains("LOW")){
            ret_string = "LOW";
        }
        else if (cap_weatherType.contains("HIGH"))
            ret_string = "HIGH";

        else if(cap_weatherType.contains("TEMPERATURE"))
            ret_string = "TEMPERATURE";

        else if(cap_weatherType.contains("WIND SPEED"))
            ret_string = "WIND SPEED";

        else if(cap_weatherType.contains("HUMIDITY"))
            ret_string = "HUMIDITY";

        else if(cap_weatherType.contains("PRESSURE"))
            ret_string = "PRESSURE";

        else if(cap_weatherType.contains("DESCRIPTION"))
            ret_string = "DESCRIPTION";

        else
            ret_string = "ALL";

        // Strip away the weather type to check if user asks for current or 8-day
        String temp = cap_weatherType.replace(ret_string, "");
        temp = temp.trim();
        if (temp.contains("8"))
            is8DayForecast = true;

        return ret_string;
    }

    /**
     * Default weather search will ask users a series of questions to get the right
     * information needed for the specific weather
     * @param message
     * @return
     */
    private String defaultWeatherSearch(String message){
        this.chatFlag = 4; // keeps conversation going with chat bot
        String ret_string = "";

        // Allows the bot to keep the conversation about the same subject going
        if (weatherAction == 0) {
            ret_string += "Did you want the current weather or an 8-day forecast?";
            weatherAction++;
        }
        else if (weatherAction == 1){
            if (message.contains("8"))
                is8DayForecast = true;
            else
                is8DayForecast = false;

            // Ask user if they would like to know a specific weather topic
            ret_string += "What weather information would you like to know?" +
                    "\nType in \"temperature\", \"high\", \"low\", \"wind speed\", \"humidity\", \"pressure\" , \"description\", or \"all\"";
            weatherAction++;
        }
        else if (weatherAction == 2){
            if (message.equalsIgnoreCase("temperature")
                    || message.equalsIgnoreCase("high")
                    || message.equalsIgnoreCase("low")
                    || message.equalsIgnoreCase("wind speed")
                    || message.equalsIgnoreCase("humidity")
                    || message.equalsIgnoreCase("pressure")
                    || message.equalsIgnoreCase("description")
                    || message.equalsIgnoreCase("all"))
                info = message;
            else{
                // default to "all" if user does not type any listed
                ret_string += "Hmm I am not sure about that one, I will default your weather information to \"all\"\n";
                info = "all";
            }

            ret_string += "What is your City and State?";
            weatherAction++;
        }
        else {
            ret_string += parseWeatherInformation(message); // Parse out city/state information

            //reset globals
            weatherAction = 0;
            chatFlag = 0;
            info = "";
            is8DayForecast = false;
            isDefaultWeather = false;
        }

        return ret_string;
    }

    /**
     * Parses out the location that the user has inputted.
     * If it is not a valid location, then the bot will let the user know.
     * If location is valid then the bot will be able to search the weather API
     * and will return the user with weather information
     * @param aLocation
     * @return
     */
    public String parseWeatherInformation(String aLocation) {
        String ret_string = "";

        // Filter out user message to find a valid state name
        String location = aLocation.toUpperCase();

        // Get rid of any special characters like: ',' , '?' , etc.
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        Matcher match= pattern.matcher(location);
        while(match.find())
        {
            String s= match.group();
            location = location.replace(s, "");
        }

        // We split city and state into arrays
        String[] locations = location.split(" ");
        String temp = "";
        String validStateName = "";
        int marker = 0;

        // We start at the end of the array to find the states first
        for (int i = locations.length - 1; i >= 0; i--) {
            // If there is a word "tomorrow", that means this will be an 8-day forecast
            if(locations[i].equals("TOMORROW"))
                continue;
            temp = locations[i] + " " + temp;
            temp = temp.trim(); // trim leading/trailing white spaces

            // checks if temp has a valid state name, if it does, then convert that to abbreviated state name
            if (!State.stateToAbbr(temp).equalsIgnoreCase("UNKNOWN")) {
                validStateName = State.stateToAbbr(temp); // Abbreviated state name
                marker = i;
                break;
            }
            // checks if abbreviated state name is already given by the user
            else if (!State.abbrToState(temp).toString().equals("UNKNOWN")) {
                validStateName = temp; // Already abbreviated state name
                marker = i;
                break;
            }
        }

        // check if we have validStateName
        if (validStateName.equals("")) {
            ret_string += "Sorry, I cannot find your state :(";
        }
        else {
            // Now re loop to get the rest of the city name
            String city = "";
            for (int i = 0; i < marker; i++) {
                city = city + " " + locations[i];
            }
            city = city.trim().replace(" ", "+");

            // We finally have the right location syntax for openweathermap.org
            location = city + "," + validStateName + ",US";

            WeatherFetcher weatherFetcher = new WeatherFetcher(); // Create weather fetcher
            ret_string += weatherFetcher.doInBackground(location, is8DayForecast, info, this.home_screen_context); // Run weather API

            // Check if response is valid
            if (ret_string.equals(""))
                ret_string += "Sorry, I could not find your city and/or state :(";
        }

        //reset globals
        weatherAction = 0;
        chatFlag = 0;
        info = "";
        is8DayForecast = false;
        isDefaultWeather = false;

        return ret_string;
    }
}
