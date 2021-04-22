package com.example.mobileassistant;

import androidx.test.espresso.Espresso;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.DatePicker;

import android.content.Intent;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsScreenBehaviorTest extends TestCase {
    private String exampleString;
    private String exampleString2;

    @Rule
    public ActivityScenarioRule<Start_screen> actRule = new ActivityScenarioRule<>(Start_screen.class);

    @Before
    public void initStrings() {
        exampleString = "Cooper";
        exampleString2 = "Gregory";
    }

    @Test
    public void intent() {
        try{
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.button_settings)).perform(click());
            Espresso.onView(withId(R.id.button_dark_mode)).perform(click());
            Thread.sleep(5000);
            Espresso.onView(withId(R.id.button_settings)).perform(click());
            Espresso.onView(withId(R.id.button_light_mode)).perform(click());
            Thread.sleep(5000);
            Espresso.onView(withId(R.id.button_settings)).perform(click());
            Espresso.onView(withId(R.id.button_signout)).perform(click());
            Thread.sleep(5000);
            Espresso.onView(withId(R.id.first_name)).perform(typeText(exampleString), closeSoftKeyboard()).check(matches(withText(exampleString)));
            Espresso.onView(withId(R.id.last_name)).perform(typeText(exampleString2), closeSoftKeyboard()).check(matches(withText(exampleString2)));
            Espresso.onView(withId(R.id.date_of_birth)).perform(click());
            Espresso.onView(withText("OK")).perform(click());
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.button_confirm)).perform(click());
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}