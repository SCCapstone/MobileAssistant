package com.example.mobileassistant;

import androidx.test.espresso.Espresso;
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

    @Rule
    public ActivityScenarioRule<Start_screen> actRule = new ActivityScenarioRule<>(Start_screen.class);

    @Before
    public void initString() {
        exampleString = "Cooper";
    }

    @Test
    public void changeText_sameActivity() {
        Espresso.onView();
    }
}