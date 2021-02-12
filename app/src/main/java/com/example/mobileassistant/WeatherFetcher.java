package com.example.mobileassistant;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class WeatherFetcher extends AsyncTask<Object, Void, String> {

    @Override
    protected String doInBackground(Object... objects)
    {
        // CONSTANTS
        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        String BASE_API_KEY = "&appid=2093fb3293296b41430ed8a791b6f99c";
        String BASE_UNITS = "&units=Imperial";

        String location = (String) objects[0]; // Passed in location

        // Context
        Context context = (Context) objects[1]; // passes in "this" from Home_screen

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

            // Json coord
            JSONObject json_coord = new JSONObject(json.getString("coord"));
            double longitude = Double.parseDouble(json_coord.getString("lon"));
            double latitude = Double.parseDouble(json_coord.getString("lat"));

            String trueLocation = getTrueLocation(context, latitude, longitude);
            ret_response += "This weather in " + trueLocation + " is:";

            // Json Main
//            System.out.println(json.toString());
            JSONObject json_main = new JSONObject(json.getString("main"));
            String temperature = json_main.getString("temp"); // Temperature
            String feels_like = json_main.getString("feels_like"); // Feels like temperature
            String temp_min = json_main.getString("temp_min"); // low temperature
            String temp_max = json_main.getString("temp_max"); // high temperature

            JSONArray json_weather = json.getJSONArray("weather"); // Weather is an array
//            System.out.println(json_main.getString("temp"));
//            System.out.println(json_weather.getJSONObject(0).getString("main"));
            String description = json_weather.getJSONObject(0).getString("description");

            ret_response += "\nTemperature:\t" + temperature
                + "\nFeels Like:\t" + feels_like
                + "\nLow:\t" + temp_min
                + "\nHigh:\t" + temp_max
                + "\nDescription:\t" + description;


            //Disconnect the HttpURLConnection stream
            conn.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return ret_response;
    }

    public String getTrueLocation(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        String full_location = city + ", " + state + ", " + country;

        return full_location;
    }
}

/** THIS IS AN EXAMPLE OF WHAT THE WEATHER JSON LOOKS LIKE
 *
 *
 * {
 *     "coord": {
 *         "lon": -81.03,
 *         "lat": 34
 *     },
 *     "weather": [
 *         {
 *             "id": 800,
 *             "main": "Clear",
 *             "description": "clear sky",
 *             "icon": "01d"
 *         }
 *     ],
 *     "base": "stations",
 *     "main": {
 *         "temp": 52.41,
 *         "feels_like": 45.03,
 *         "temp_min": 50,
 *         "temp_max": 54,
 *         "pressure": 1026,
 *         "humidity": 39
 *     },
 *     "visibility": 10000,
 *     "wind": {
 *         "speed": 5.82,
 *         "deg": 290
 *     },
 *     "clouds": {
 *         "all": 1
 *     },
 *     "dt": 1606936511,
 *     "sys": {
 *         "type": 1,
 *         "id": 3722,
 *         "country": "US",
 *         "sunrise": 1606911174,
 *         "sunset": 1606947300
 *     },
 *     "timezone": -18000,
 *     "id": 4575352,
 *     "name": "Columbia",
 *     "cod": 200
 * }
 */

