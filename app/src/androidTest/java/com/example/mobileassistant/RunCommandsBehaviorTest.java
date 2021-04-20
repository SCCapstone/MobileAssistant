package com.example.mobileassistant;

import androidx.test.espresso.Espresso;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RunCommandsBehaviorTest extends TestCase {
    private String nexteventString;
    private String showspecificeventString;
    private String createeventString;
    private String createeventconty;
    private String createeventcont1;
    private String createeventcont2;
    private String searchString;
    private String newsString;
    private String specificWeatherString;
    private String nonspecificWeatherString;
    private String wethcont1;
    private String wethcont2;
    private String wethcont3;
    private String directionstoString;
    private String trafficString;
    private String rpsString;
    private String rpscont1;
    private String rpscont2;
    private String helpString;
    private String helpcont1;
    private String helpcont2;


    @Rule
    public ActivityScenarioRule<Start_screen> actRule = new ActivityScenarioRule<>(Start_screen.class);

    @Before
    public void initStrings() {
        nexteventString = "show me next event";
        showspecificeventString = "show event \"Final Demo Due\"";
        createeventString = "create event \"Final Demo Due\" on Apr 26th from 10pm to 11pm";
        createeventconty = "yes";
        createeventcont1 = "Columbia SC";
        createeventcont2 = "Cooper Gregory, Dongyu Chen, Cameron Brandenburg, Henry Vy, Anna Wang";
        searchString = "search University of South Carolina top 5 results";
        newsString = "baseball news";
        specificWeatherString = "what is the forecast in Philadelphia PA";
        nonspecificWeatherString = "weather";
        wethcont1 = "current";
        wethcont2 = "all";
        wethcont3 = "Georgetown SC";
        directionstoString = "directions to Sumter SC";
        trafficString = "traffic";
        rpsString = "rock paper scissors";
        rpscont1 = "rock";
        rpscont2 = "no";
        helpString = "help";
        helpcont1 = "weather";
        helpcont2 = "exit";
    }

    @Test
    public void changeText() {
        try{
            /*
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(createeventString),closeSoftKeyboard()).check(matches(withText(createeventString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(createeventconty),closeSoftKeyboard()).check(matches(withText(createeventconty)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(createeventcont1),closeSoftKeyboard()).check(matches(withText(createeventcont1)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(createeventcont2),closeSoftKeyboard()).check(matches(withText(createeventcont2)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(nexteventString),closeSoftKeyboard()).check(matches(withText(nexteventString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(showspecificeventString),closeSoftKeyboard()).check(matches(withText(showspecificeventString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
             */
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(searchString),closeSoftKeyboard()).check(matches(withText(searchString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(newsString),closeSoftKeyboard()).check(matches(withText(newsString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(specificWeatherString),closeSoftKeyboard()).check(matches(withText(specificWeatherString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(nonspecificWeatherString),closeSoftKeyboard()).check(matches(withText(nonspecificWeatherString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(wethcont1),closeSoftKeyboard()).check(matches(withText(wethcont1)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(wethcont2),closeSoftKeyboard()).check(matches(withText(wethcont2)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(wethcont3),closeSoftKeyboard()).check(matches(withText(wethcont3)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(rpsString),closeSoftKeyboard()).check(matches(withText(rpsString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(rpscont1),closeSoftKeyboard()).check(matches(withText(rpscont1)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(rpscont2),closeSoftKeyboard()).check(matches(withText(rpscont2)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(helpString),closeSoftKeyboard()).check(matches(withText(helpString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(helpcont1),closeSoftKeyboard()).check(matches(withText(helpcont1)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(helpcont2),closeSoftKeyboard()).check(matches(withText(helpcont2)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(trafficString),closeSoftKeyboard()).check(matches(withText(trafficString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(3000);
            Espresso.pressBack();
            Espresso.onView(withId(R.id.messageEditText)).perform(typeText(directionstoString),closeSoftKeyboard()).check(matches(withText(directionstoString)));
            Espresso.onView(withId(R.id.button_send)).perform(click());
            Thread.sleep(6000);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}