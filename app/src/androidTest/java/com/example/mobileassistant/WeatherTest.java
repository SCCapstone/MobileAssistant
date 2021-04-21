package com.example.mobileassistant;

import androidx.test.core.app.ApplicationProvider;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherTest extends TestCase {
    private Weather weather = new Weather(ApplicationProvider.getApplicationContext());

    @Test
    public void testWeatherSearch() {
        // Variables
        String str; // input
        String result; // result from method
        String expected; // expected result

        /* TESTING CURRENT */
        // Testing weather with correct city/state
        str = "What is the weather in Columbia, SC?";
        result = weather.weatherSearch(str);
        expected = "The weather in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing weather with correct city/state for tomorrow
        str = "What is the weather in Columbia SC tomorrow?";
        result = weather.weatherSearch(str);
        expected = "The 8-day forecast in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current temperature with correct city/state
        str = "What is the current temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current high temperature with correct city/state
        str = "What is the currently the high temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The high temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current low temperature with correct city/state
        str = "What is the currently the low temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The low temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current wind speed with correct city/state
        str = "What is the current wind speed in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The wind speed in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current humidity level with correct city/state
        str = "What is the current humidity level in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The humidity level in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current pressure with correct city/state
        str = "What is the current pressure in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The pressure in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing current weather description with correct city/state
        str = "What is the current weather description in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "You can expect ";
        assertTrue(result.contains(expected));

        /* TESTING 8-DAY */
        // Testing 8-day forecast with correct city/state
        str = "What is the 8-day forecast in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day forecast in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day temperature with correct city/state
        str = "What is the 8-day temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day high temperature with correct city/state
        str = "What is the 8-day high temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day high temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day low temperature with correct city/state
        str = "What is the 8-day low temperature in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day low temperature in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day wind speed with correct city/state
        str = "What is the 8-day wind speed in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day wind speed in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day humidity level with correct city/state
        str = "What is the 8-day humidity level in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day humidity level in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day pressure with correct city/state
        str = "What is the 8-day pressure in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day pressure in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing 8-day description with correct city/state
        str = "What is the 8-day weather description in Columbia SC?";
        result = weather.weatherSearch(str);
        expected = "The 8-day weather description in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        /* TESTING DEFAULT WEATHER */
        // Testing default along with chatFlag active
        str = "weather";
        result = weather.weatherSearch(str);
        expected = "Did you want the current weather or an 8-day forecast?";
        assertEquals(expected, result);

        str = "current";
        result = weather.weatherSearch(str);
        expected = "What weather information would you like to know?\nType in \"temperature\", \"high\", \"low\", \"wind speed\", \"humidity\", \"pressure\" , \"description\", or \"all\"";
        assertEquals(expected, result);

        str = "all";
        result = weather.weatherSearch(str);
        expected = "What is your City and State?";
        assertEquals(expected, result);

        str = "Columbia South Carolina";
        result = weather.weatherSearch(str);
        expected = "The weather in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        // Testing incorrect weather input
        weather = new Weather(ApplicationProvider.getApplicationContext()); // reset weather
        str = "Asking robot about weather information incorrectly";
        result = weather.weatherSearch(str);
        System.out.println(result);
        expected = "Sorry I could not get the correct information. Did you want the current weather or an 8-day forecast?";
        assertEquals(expected, result);
    }

    @Test
    public void testFindWeatherType() {
        // Variables
        String str; // input
        String result; // result from method
        String expected; // expected result

        // ALL
        str = "What is the weather in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "ALL";
        assertEquals(expected, result);

        str = "Incorrect str being sent";
        result = weather.findWeatherType(str);
        expected = "ALL";
        assertEquals(expected, result);

        // LOW
        str = "What is the low temperature in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "LOW";
        assertEquals(expected, result);

        // HIGH
        str = "What is the high temperature in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "HIGH";
        assertEquals(expected, result);

        // TEMPERATURE
        str = "What is the temperature in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "TEMPERATURE";
        assertEquals(expected, result);

        // WIND SPEED
        str = "What is the wind speed in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "WIND SPEED";
        assertEquals(expected, result);

        // HUMIDITY
        str = "What is the humidity in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "HUMIDITY";
        assertEquals(expected, result);

        // PRESSURE
        str = "What is the pressure in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "PRESSURE";
        assertEquals(expected, result);

        // DESCRIPTION
        str = "What is the weather description in Columbia, SC?";
        result = weather.findWeatherType(str);
        expected = "DESCRIPTION";
        assertEquals(expected, result);
    }

    @Test
    public void testParseWeatherInformation() {
        // Variables
        String str; // input
        String result; // result from method
        String expected; // expected result

        // Testing Incorrect State
        str = "Columbia, Fake State";
        result = weather.parseWeatherInformation(str);
        expected = "Sorry, I cannot find your state :(";
        assertEquals(expected, result);

        // Testing Incorrect City
        str = "Fake City, South Carolina";
        result = weather.parseWeatherInformation(str);
        expected = "Sorry, I could not find your city and/or state :(";
        assertEquals(expected, result);

        // Testing Incorrect City and State
        str = "Fake City, Fake State";
        result = weather.parseWeatherInformation(str);
        expected = "Sorry, I cannot find your state :(";
        assertEquals(expected, result);

        // Testing correct state
        str = "Columbia, South Carolina";
        result = weather.parseWeatherInformation(str);
        expected = "The weather in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        str = "Columbia, SC";
        result = weather.parseWeatherInformation(str);
        expected = "The weather in Columbia, South Carolina, United States is";
        assertTrue(result.contains(expected));

        str = "New York, New York";
        result = weather.parseWeatherInformation(str);
        expected = "The weather in New York, New York, United States is";
        assertTrue(result.contains(expected));

        str = "New York, NY";
        result = weather.parseWeatherInformation(str);
        expected = "The weather in New York, New York, United States is";
        assertTrue(result.contains(expected));
    }
}