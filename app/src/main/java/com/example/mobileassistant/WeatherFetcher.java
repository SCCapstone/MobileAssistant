package com.example.mobileassistant;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class WeatherFetcher extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... query)
    {
        // CONSTANTS
        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        String BASE_API_KEY = "&appid=2093fb3293296b41430ed8a791b6f99c";
        String BASE_UNITS = "&units=Imperial";

        String location = query[0]; // Passed in location

        String[] split_list = query[0].split(","); // Split city and country
        String city = split_list[0];
        String country = split_list[1];


        // Build url String
        String urlString = BASE_URL + location + BASE_API_KEY + BASE_UNITS;


        String ret_response = ""; // The response that the bot will use
        String result = ""; // The result from the api

        try{
            // Connect to api
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Error checking
            int responseCode = conn.getResponseCode();
            if (responseCode != 200)
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            else{
                Scanner sc = new Scanner(url.openStream());

                while (sc.hasNext()){
                    result += sc.nextLine();
                }

                sc.close();
            }

            // String to JSON
            JSONObject json = new JSONObject(result); // JSON

//            System.out.println(json.toString());
            JSONObject json_main = new JSONObject(json.getString("main"));
            String temperature = json_main.getString("temp"); // Temperature

            JSONArray json_weather = json.getJSONArray("weather");
//            System.out.println(json_main.getString("temp"));
//            System.out.println(json_weather.getJSONObject(0).getString("main"));

            ret_response += "The temperature in " + city +
                    ", " + country + " is " + temperature;


            //Disconnect the HttpURLConnection stream
            conn.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return ret_response;
    }


}

