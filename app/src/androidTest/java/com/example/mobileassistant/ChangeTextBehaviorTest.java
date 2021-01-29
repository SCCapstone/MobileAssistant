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
public class ChangeTextBehaviorTest extends TestCase {
    private String exampleString;
    private String exampleString2;
    private String exampleString3;

    @Rule
    public ActivityScenarioRule<Start_screen> actRule = new ActivityScenarioRule<>(Start_screen.class);

    @Before
    public void initStrings() {
        exampleString = "Cooper";
        exampleString2 = "Gregory";
        exampleString3 = "4/13/1999";
    }

    @Test
    public void changeText() {
        Espresso.onView(withId(R.id.first_name)).perform(typeText(exampleString), closeSoftKeyboard()).check(matches(withText(exampleString)));
        //Espresso.onView(withId(R.id.button_change_fn)).perform(click());
        Espresso.onView(withId(R.id.last_name)).perform(typeText(exampleString2), closeSoftKeyboard()).check(matches(withText(exampleString2)));
        //Espresso.onView(withId(R.id.button_change_ln)).perform(click());
        Espresso.onView(withId(R.id.date_of_birth)).perform(typeText(exampleString3), closeSoftKeyboard()).check(matches(withText(exampleString3)));
        //Espresso.onView(withId(R.id.button_change_dob)).perform(click());

        Espresso.onView(withId(R.id.button_confirm)).perform(click());
    }
}