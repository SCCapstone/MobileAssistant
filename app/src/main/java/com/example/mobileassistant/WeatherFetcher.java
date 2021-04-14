package com.example.mobileassistant;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.api.client.json.Json;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;

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
        //System.out.println("*******************************");

        // CONSTANTS
        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        String BASE_API_KEY = "&appid=2093fb3293296b41430ed8a791b6f99c";
        String BASE_UNITS = "&units=Imperial";

        // Location
        String location = (String) objects[0];

        //Current weather or 8-day forecast?
        boolean is8DayForecast = (boolean) objects[1];

        // What info do they want?
        String weatherInfo = (String) objects[2];

        // Context
        Context context = (Context) objects[3]; // passes in "this" from Home_screen

        // Build url String
        String urlString = BASE_URL + location + ",US" + BASE_API_KEY + BASE_UNITS;

        String ret_response = ""; // The response that the bot will use
        String result = ""; // The result from the api

        try {
            // Connect to api
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Error checking
            int responseCode = conn.getResponseCode();
            if (responseCode != 200)
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            else {
                Scanner sc = new Scanner(url.openStream());

                while (sc.hasNext()) {
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

            if (!is8DayForecast) {
                // Json Main
                JSONObject json_main = new JSONObject(json.getString("main"));
                String temperature = json_main.getString("temp") + "°F"; // Temperature
                String temp_min = json_main.getString("temp_min") + "°F"; // low temperature
                String temp_max = json_main.getString("temp_max") + "°F"; // high temperature
                String humidity = json_main.getString("humidity") + "%"; // humidity with percentage
                String pressure = json_main.getString("pressure") + " hPa"; // pressure in hPa

                JSONObject json_wind = new JSONObject(json.getString("wind")); // wind
                String wind_speed = json_wind.getString("speed") + " mph"; // wind speed

                JSONArray json_weather = json.getJSONArray("weather"); // Weather is an array
                String description = json_weather.getJSONObject(0).getString("description");// weather description

                // build output based on what the user wants
                if (weatherInfo.equalsIgnoreCase("temperature"))
                    ret_response += "The temperature in " + trueLocation + " is " + temperature;

                else if (weatherInfo.equalsIgnoreCase("high"))
                    ret_response += "The high temperature in " + trueLocation + " is " + temp_max;

                else if(weatherInfo.equalsIgnoreCase("low"))
                    ret_response += "The low temperature in " + trueLocation + " is " + temp_min;

                else if(weatherInfo.equalsIgnoreCase("wind speed"))
                    ret_response += "The wind speed in " + trueLocation + " is " + wind_speed;

                else if(weatherInfo.equalsIgnoreCase("humidity"))
                    ret_response += "The humidity level in " + trueLocation + " is " + humidity;

                else if(weatherInfo.equalsIgnoreCase("pressure"))
                    ret_response += "The pressure in " + trueLocation + " is " + pressure;

                else if(weatherInfo.equalsIgnoreCase("description"))
                    ret_response += "You can expect " + description + " in " + trueLocation;

                else{
                    ret_response += "The weather in " + trueLocation + " is:"
                            + "\nTemperature:\t" + temperature
                            + "\nHigh:\t" + temp_max
                            + "\nLow:\t" + temp_min
                            + "\nWind Speed:\t" + wind_speed
                            + "\nHumidity Level:\t" + humidity
                            + "\nPressure:\t" + pressure
                            + "\nDescription:\t" + description;
                }
                //Disconnect the HttpURLConnection stream
                conn.disconnect();
            }
            else { // 8-day forecast
                //Disconnect the HttpURLConnection stream
                conn.disconnect();

                // Build new connection for 8-day forecast
                String NEW_BASE_URL = "https://api.openweathermap.org/data/2.5/onecall?" +
                        "lat=" + latitude + "&lon=" + longitude + "&exclude=current,minutely,hourly,alerts&";

                urlString = NEW_BASE_URL + BASE_API_KEY + BASE_UNITS;

                String result2 = "";
                // Connect to api
                URL url2 = new URL(urlString);
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setRequestMethod("GET");
                conn2.connect();

                // Error checking
                int responseCode2 = conn2.getResponseCode();
                if (responseCode2 != 200)
                    throw new RuntimeException("HttpResponseCode: " + responseCode2);
                else {
                    Scanner sc = new Scanner(url2.openStream());

                    while (sc.hasNext()) {
                        result2 += sc.nextLine();
                    }
                    sc.close();
                }

                // String to JSON
                JSONObject json2 = new JSONObject(result2); // JSON
                JSONArray json_daily = json2.getJSONArray("daily"); // 8 day array forecast

                // Date converter for unix timestamp
                Date date = new Date();

                // Building output, building the heading
                if (weatherInfo.equalsIgnoreCase("temperature"))
                    ret_response += "The 8-day temperature in " + trueLocation + " is:";

                else if (weatherInfo.equalsIgnoreCase("high"))
                    ret_response += "The 8-day high temperature in " + trueLocation + " is:";

                else if(weatherInfo.equalsIgnoreCase("low"))
                    ret_response += "The 8-day low temperature in " + trueLocation + " is:";

                else if(weatherInfo.equalsIgnoreCase("wind speed"))
                    ret_response += "The 8-day wind speed in " + trueLocation + " is:";

                else if(weatherInfo.equalsIgnoreCase("humidity"))
                    ret_response += "The 8-day humidity level in " + trueLocation + " is:";

                else if(weatherInfo.equalsIgnoreCase("pressure"))
                    ret_response += "The 8-day pressure in " + trueLocation + " is:";

                else if(weatherInfo.equalsIgnoreCase("description"))
                    ret_response += "The 8-day description in " + trueLocation + " is:";

                else
                    ret_response += "The 8-day forecast in " + trueLocation + " is:";



                for (int i = 0; i < json_daily.length(); i++) {
                    long dt = Long.parseLong(json_daily.getJSONObject(i).getString("dt"));
                    date.setTime(dt * 1000); // Date and time

                    // max and min temperature
                    JSONObject json_temp = new JSONObject(json_daily.getJSONObject(i).getString("temp"));
                    String temperature = json_temp.getString("day") + "°F";
                    String max_temp = json_temp.getString("max") + "°F";
                    String min_temp = json_temp.getString("min") + "°F";

                    String wind_speed = json_daily.getJSONObject(i).getString("wind_speed") + "mph"; // wind speed
                    String humidity = json_daily.getJSONObject(i).getString("humidity") + "%"; // humidity
                    String pressure = json_daily.getJSONObject(i).getString("pressure") + "hPa"; // pressure in hPa

                    // Weather Description
                    JSONArray json_weather = json_daily.getJSONObject(i).getJSONArray("weather");
                    String description = json_weather.getJSONObject(0).getString("description");

                    // Building output, building the body
                    ret_response += "\n" + date.toString();
                    if (weatherInfo.equalsIgnoreCase("temperature"))
                        ret_response += "\n\tTemperature: " + temperature;

                    else if (weatherInfo.equalsIgnoreCase("high"))
                        ret_response += "\n\tHigh: " + max_temp + "";

                    else if(weatherInfo.equalsIgnoreCase("low"))
                        ret_response += "\n\tLow: " + min_temp + "";

                    else if(weatherInfo.equalsIgnoreCase("wind speed"))
                        ret_response += "\n\tWind Speed: " + wind_speed;

                    else if(weatherInfo.equalsIgnoreCase("humidity"))
                        ret_response += "\n\tHumidity Level: " + humidity;

                    else if(weatherInfo.equalsIgnoreCase("pressure"))
                        ret_response += "\n\tPressure: " + pressure;

                    else if(weatherInfo.equalsIgnoreCase("description"))
                        ret_response += "\n\tDescription: " + description;

                    else {
                        ret_response += "\n\tTemperature: " + temperature
                                + "\n\tHigh: " + max_temp
                                + "\n\tLow: " + min_temp
                                + "\n\tWind Speed: " + wind_speed
                                + "\n\tHumidity Level: " + humidity
                                + "\n\tPressure: " + pressure
                                + "\n\tDescription: " + description;
                    }



                }
                conn2.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("*******************************");
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
