package com.example.mobileassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Test;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.mobileassistant.AccessCalendar.service;

public class CalendarUnitTest extends TestCase{
    private AccessCalendar act;
    private CalendarAsyncTask cal = new CalendarAsyncTask(act);

    @Test
    public void testFindNumber() {
        String str = "show me next 5 events";
        assertEquals("5",cal.findNum(str));

        str = "show me next 12 events";
        assertEquals("12",cal.findNum(str));
    }

    @Test
    public void testGetEventTime() {
        String str = "2021-05-28T09:00:00-05:00";
        String ret = Arrays.toString(cal.getInfo(str));
        assertEquals("[2021-05-28, 9:00am]",ret);

        str = "2021-05-28T22:15:00-05:00";
        ret = Arrays.toString(cal.getInfo(str));
        assertEquals("[2021-05-28, 10:15pm]",ret);
    }

    @Test
    public void testGetEventDetails() {
        DateTime sDT = new DateTime("2021-05-28T09:00:00-05:00");
        DateTime eDT = new DateTime("2021-05-28T13:00:00-05:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(sDT);
        EventDateTime end = new EventDateTime()
                .setDateTime(eDT);
        Event event = new Event()
                .setSummary("This is a Test Event")
                .setStart(start)
                .setEnd(end);
        String ret = Arrays.toString(cal.getDetails(event));
        assertEquals("[2021-05-28, 9:00am, \"This is a Test Event\", 1:00pm]",ret);

        sDT = new DateTime("2021-05-28T20:10:00-05:00");
        eDT = new DateTime("2021-05-29T09:15:00-05:00");
        start = new EventDateTime()
                .setDateTime(sDT);
        end = new EventDateTime()
                .setDateTime(eDT);
        event = new Event()
                .setSummary("Second Test Event")
                .setStart(start)
                .setEnd(end);
        ret = Arrays.toString(cal.getDetails(event));
        assertEquals("[2021-05-28, 8:10pm, \"Second Test Event\", 9:15am]",ret);

    }

    @Test
    public void testConvertTime() {
        String str = "3:30pm";
        assertEquals("15:30:00",cal.convertTime(str));
        str = "9am";
        assertEquals("09:00:00",cal.convertTime(str));
        str = "11:00 am";
        assertEquals("11:00:00",cal.convertTime(str));
        str = "1 pm";
        assertEquals("13:00:00",cal.convertTime(str));
    }

    @Test
    public void testStartEndT() {
        Home_screen.eventRequest = "create event on May 3rd from 9am to 10 am";
        String ret = Arrays.toString(cal.startEndT());
        assertEquals("[09:00:00, 10:00:00]",ret);

        Home_screen.eventRequest = "create event on May 3rd from 11:15am to 3:30 pm";
        ret = Arrays.toString(cal.startEndT());
        assertEquals("[11:15:00, 15:30:00]",ret);
    }

    @Test
    public void testGetDate() throws ParseException {
        Home_screen.eventRequest = "create event on May 3rd";
        assertEquals("05-03",cal.getDate());

        Home_screen.eventRequest = "create event on november 11th";
        assertEquals("11-11",cal.getDate());
    }

    @Test
    public void testGetWeek() {
        Home_screen.eventRequest = "create event every monday and Wed";
        assertEquals(Arrays.asList(2,4),cal.getWeek());

        Home_screen.eventRequest = "create event every Tues";
        assertEquals(Arrays.asList(3),cal.getWeek());

        Home_screen.eventRequest = "create event every Mon, Thursday, and fri";
        assertEquals(Arrays.asList(2,5,6),cal.getWeek());
    }

    @Test
    public void testEndDate() throws ParseException {
        Home_screen.eventRequest = "create event every mon and tues until May, 2021";
        assertEquals("202105",cal.endDate());

        Home_screen.eventRequest = "create event every mon and tues until jun, 2022";
        assertEquals("202206",cal.endDate());
    }

    // Results changing based on the  current date and time
    /*@Test
    public void testGetReccur() {
        List <Integer> l = new ArrayList<Integer>(){{
            add(1);
            add(3);
        }};
        Calendar c = Calendar.getInstance();
        String ret = Arrays.toString(cal.getRecurr(l,c));
        assertEquals("[04-25, 04-20]",ret);
    }*/

}
