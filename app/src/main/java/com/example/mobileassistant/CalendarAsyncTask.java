package com.example.mobileassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.lang.Math;

public class CalendarAsyncTask extends AsyncTask<Void, Void, List<String>> {
    private AccessCalendar activity;

    // Constructors
    CalendarAsyncTask(AccessCalendar activity) {
        this.activity = activity;
    }

    // Background task to call Google Calendar API
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            // search for event/events
            if(Home_screen.create == false)
                activity.updateResultsText(getEventsFromApi());
            // create event/events
            else activity.newEvent(addEvent());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            activity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            activity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    activity.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
            activity.updateStatus("The following error occurred: " +
                    e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // get events from the calendar
    public List<String> getEventsFromApi() throws IOException {
        List<String> eventStrings = new ArrayList<String>();
        DateTime now = new DateTime(System.currentTimeMillis());

        // get required number of upcoming events from the calendar staring from current time
        Events events = activity.service.events().list("primary")
                .setMaxResults(Home_screen.number)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        /**
         * If there is no upcoming events, send feedback to the user
         * If user entered a specific event label, then search for that event and return event details
         * Otherwise, return required number of events' details to the chat
         */
        if (items.isEmpty()) {
            eventStrings.add("No upcoming events");
        }
        else if (Home_screen.id != null && !Home_screen.id.isEmpty()) {
            Events et = activity.service.events().list("primary")
                   // .setMaxResults(Home_screen.number)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> it = et.getItems();
           // System.out.println("the searching event is: " + Home_screen.id + "the size of list is: "+it.size());

            boolean found = false; // check if found the event or not
            for (Event event: it) {
                //System.out.println("the current event is: "+event.getSummary());
                if (event.getSummary().equalsIgnoreCase(Home_screen.id)) {
                    found = true;
                    // call getDetails method to get event details
                    String[] details = getDetails(event);

                    // add event details to return list
                    // details[0]: event date, details[1]: event start time,
                    // details[2]: event label, details[3]: event end time
                    eventStrings.add(String.format("%s %s\n",
                            details[2], "on " + details[0] + ", from " + details[1] + " to " + details[3]));

                    // add other event's info to the list if available
                    if (event.getDescription() != null)
                        eventStrings.add("Description:  "+event.getDescription());
                    if (event.getLocation() != null)
                        eventStrings.add("locate at "+event.getLocation().toUpperCase());
                    if (event.getAttendees() != null) {
                        eventStrings.add("Attendees are: " + event.getAttendees());
                    }

                    break;
                }
            }

            // if event doesn't exits in calendar
            if (found == false)
                eventStrings.add("No such event found in your calendar");
        }
        else{
            if (Home_screen.number > items.size())
                eventStrings.add("You only have " + items.size() + " upcoming events!\n");
            for (Event event : items) {
                String[] details = getDetails(event);
                eventStrings.add(String.format("%s %s\n",details[2],
                        "on " + details[0] +", "+ details[1] +"-"+ details[3]));
            }
        }
        return eventStrings;
    }

    // get events start and end time
    public String[] getInfo(String t) {
        String result[] = new String[2];
        String[] temp = t.split("T"); // split date and time string
        result[0] = temp[0]; // date
        String temp1 = temp[1]; // time string
        String[] temp3 = temp1.split(":"); // split time string

        // return more user friendly format for time
        int num = Integer.parseInt(temp3[0]);
        if (num < 12)
            result[1] = num + ":" + temp3[1] + "am"; // morning time
        else if (num == 12){
            result[1] = num + ":" + temp3[1] + "pm"; // afternoon time
        }
        else {
            num = num - 12;
            result[1] = num + ":" + temp3[1] + "pm"; // afternoon time
        }

        return result;
    }

    /**
     * Get event details
     * ret[0]: event date, ret[1]: event start time
     * ret[2]: event label, ret[3]: event end time
     */
    public String[] getDetails(Event event) {
        String[] ret = new String[4];
        DateTime start = event.getStart().getDateTime();
        if (start == null) {
            start = event.getStart().getDate(); // all day events, just get the date
        }
        String info = start.toString();
        ret[0] = getInfo(info)[0]; // date
        ret[1] = getInfo(info)[1]; // start time

        ret[2] = "\""+ event.getSummary()+"\""; // label

        DateTime end = event.getEnd().getDateTime();
        String info1 = end.toString();
        ret[3] = getInfo(info1)[1]; // end time

        return ret;
    }
    
    // Insert Event/Events
    public String addEvent() throws IOException, ParseException {
        Calendar cal = Calendar.getInstance();
        String feedback; // user feedback send to Home_screen chat
        String id = Home_screen.id; // event label, from Home_screen class
        String startT = startEndT()[0]; // call starEndT method to get event start time
        String endT = startEndT()[1]; // call starEndT method to get event end time
        int year = cal.get(Calendar.YEAR); // event year, current year, from Home_screen class
        String location = Home_screen.location; // event location, from Home_screen class

        // call getWeek method if see if it is an recurring event
        List<Integer> ret = getWeek();

        // not a recurring event, just create event
        if (ret.isEmpty()) {
            String date = getDate();
            Event newEvent = createEvent(startT, endT, date, year, location, id);
            newEvent = AccessCalendar.service.events().insert("primary",newEvent).execute();
            if (newEvent != null)
                feedback = "Event has successfully created!";
            else feedback = "Not able to create this event";
        }

        // recurring events
        else {
            // call getRecurr method to get recurring info
            String[] dates = getRecurr(ret,cal);
            int count = 0; // use to check if recurring events have successfully created
            // call endDate method to get the end Date for the recurring events
            // right now only take month and year
            String ed = endDate();
            String until = ed + "01T170000Z"; // String for setRecurrence UNTIL

            // create recurring events repeat weekly for each day of the week user entered
            for (int i = 0; i < dates.length; i++) {
               // System.out.println("the events dates are: " + dates[i]);
                Event ne = createEvent(startT, endT, dates[i], year, location, id);
                ne.setRecurrence(Arrays.asList("RRULE:FREQ=WEEKLY;UNTIL="+until));
                ne = AccessCalendar.service.events().insert("primary",ne).execute();
                count++;
            }

            if (count == dates.length)
                feedback = "Events has successfully created!";
            else feedback = "Not able to create these events";
        }
            return feedback;
    }

    // method used to setting up details to create event
     public Event createEvent(String startT, String endT, String date,
                              int year, String location, String label) throws IOException {

         Event newEvent = new Event().setSummary(label); // set up event label

         if (location != null)
             newEvent.setLocation(location); // set up event location

         // set up event start time
         DateTime startTime = new DateTime(year+"-"+date+"T"+startT+"-04:00");
         EventDateTime start = new EventDateTime()
                 .setDateTime(startTime)
                 .setTimeZone("America/Los_Angeles");
         newEvent.setStart(start);

         // set up event end time
         DateTime endTime = new DateTime(year+"-"+date+"T"+endT+"-04:00");
         EventDateTime end = new EventDateTime()
                 .setDateTime(endTime)
                 .setTimeZone("America/Los_Angeles");
         newEvent.setEnd(end);

         // set up attendees
         EventAttendee[] attendees = getAttendees();
         if (attendees != null)
             newEvent.setAttendees(Arrays.asList(attendees));

        // newEvent = AccessCalendar.service.events().insert("primary",newEvent).execute();

         return newEvent;
     }


    // looking for attendees from user input string array
    public EventAttendee[] getAttendees() {
        String attends = Home_screen.attend;
        if (attends != null) {
            String[] temp = attends.split(", "); // split attendees' array to get emails
            EventAttendee[] att = new EventAttendee[temp.length]; // EventAttendess array
            for (int i = 0; i < temp.length; i++) {
                att[i] = new EventAttendee().setEmail(temp[i]);
            }
            return att;
        }
        return null;
    }

    // helper method to look for numbers inside a string
    public String findNum(String str) {
        char[] findNum = str.toCharArray(); // convert string to char array
        StringBuilder newStr = new StringBuilder(); // StringBuilder to build a String
        for (char c : findNum) {
            if (Character.isDigit(c)) {
                newStr.append(c); // combine chars to string
            }
        }
        return newStr.toString();
    }

    // parse time to correct format
    public String convertTime(String st){
        /* use current time as event start time*/
        // Calendar ca = Calendar.getInstance();
        //int hrs = ca.get(Calendar.HOUR_OF_DAY); // current hour
       // int mins = ca.get(Calendar.MINUTE); // current minutes
        String h, m;

        // convert time to correct format used for creating events
       // if (hrs < 10) h = "0"+hrs; else h = Integer.toString(hrs);
       // if (mins < 10) m = "0"+mins; else m = Integer.toString(mins);
        /*actual whole day event start time*/
        h = "00";
        m = "00";
        String ret = h+":"+m+":"+"00";

        // search inside of user's input to get time info and convert to correct format
        if(st.contains(":")) {
            String[] temp = st.split(":");
            String[] temp1 = temp[0].split(" ");
            String[] temp2 = temp[1].split(" ");
            String hr = temp1[temp1.length-1];
            String min = findNum(temp2[0]);

            if (st.contains("am") && Integer.parseInt(hr) < 10) {
                hr = "0"+hr;
            }
            else if (st.contains("pm")) {
                int n = Integer.parseInt(hr)+12;
                hr = Integer.toString(n);
            }
            if (min==null)
                min = "00";
            ret = hr +":"+min+":"+"00";
        }
        else {
            if (st.contains("am")) {
                String[] temp = st.split("am");
                String[] temp1 = temp[0].split(" ");
                String hr = temp1[temp1.length-1];
                if (Integer.parseInt(hr) < 10)
                    hr = "0"+hr;
                ret = hr +":00:"+"00";
            }
            else if(st.contains("pm")) {
                String[] temp = st.split("pm");
                String[] temp1 = temp[0].split(" ");
                String hr = temp1[temp1.length-1];
                int n = Integer.parseInt(hr)+12;
                hr = Integer.toString(n);
                ret = hr +":00:"+"00";
            }
        }
        return ret;
    }

    // looking for event start and end time
    public String[] startEndT() {
        String userReq = Home_screen.eventRequest.toLowerCase();
        String[] times = new String[2];

        // search within user inout to find event start and end time
        if(userReq.contains("from") && userReq.contains("to")) {
            String[] temp = userReq.split("from");
            String[] temp1 = temp[1].split("to");
            times[0] = convertTime(temp1[0]);
            times[1] = convertTime(temp1[1]);
        }
        // have end time only
        /*else if(userReq.contains("end")) {
            String[] temp = userReq.split("end");
            times[1] = convertTime(temp[1]);
            times[0] = "00:00:00";
            //time(userReq,temp,times,1);
        }*/

        // have start time only
        else if (convertTime(userReq)!=null) {
                times[0] = convertTime(userReq);
                times[1] = "23:59:59";
            }

        return times;
    }

    // looking for event date
    public String getDate() throws ParseException {
         String userReq = Home_screen.eventRequest.toLowerCase();
         Calendar cal = Calendar.getInstance();
         int day = cal.get(Calendar.DATE); // current day
         int mon = cal.get(Calendar.MONTH)+1; // current month with offset 1, month starts from 0
         String date;
        // System.out.println(mon+"-"+day);

         //search with user input to get date info
        // and convert to correct format used for creating events
         if (userReq.contains("on")) {
             String[] temp = userReq.split("on");
             System.out.println(temp[0]);
             System.out.println(temp[1]);
             String[] temp1 = temp[1].split(" ");
             //System.out.println("the month is: " + temp1[1]);
             String month = temp1[1];
             //System.out.println(month);
             Date dt = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
             Calendar cal1 = Calendar.getInstance();
             cal1.setTime(dt);
             mon = cal1.get(Calendar.MONTH)+1;
             day = Integer.parseInt(findNum(temp1[2]));
             // System.out.println(mon+"-"+day);
         }

         if (mon < 10 && day >10)
             date = "0"+mon+"-"+day;
         else if (mon < 10 && day < 10)
             date = "0"+mon+"-"+"0"+day;
         else if (mon > 10 && day < 10)
             date = mon+"-"+"0"+day;
         else date = mon+"-"+day;

         return date;
    }

    // get Recurring event info
    public String[] getRecurr(List<Integer> ret, Calendar cal) {
        int current = cal.get(Calendar.DAY_OF_WEEK); // current day of week
        int month = cal.get(Calendar.MONTH)+1; // current month
        int day = cal.get(Calendar.DAY_OF_MONTH); // current day of month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //get the last day of the current month
        String date = month+"-"+day;

        int difference = 0;  // helper int to get the correct date to start creating recurring events
        String[] dates = new String[ret.size()]; // return dates

        // loop to get correct start date for each day of the week that user entered
        for (int i = 0; i < ret.size(); i++) {
            int goal = ret.get(i);  // goal day

            // calculate the number of days difference
            if (current > goal) // if the current day of week past the goal day of week
                difference = 7 - (current - goal);
            else difference = goal - current; // if the goal day of week after/equal the current one
            // System.out.println("The difference is: "+difference);
            day = day + difference; // get the correct staring date

            // if start date pass over the current month, go to next month
            if (day > days) {
                System.out.println(days + " " + day + " " +Math.abs(days - day));
                day = Math.abs(days - day);
                month = month + 1;
            }

          //  System.out.println("The month is: " + month +"\n" +"the day is: "+day);

            if (month < 10 && day >10)
                date = "0"+month+"-"+day;
            else if (month < 10 && day < 10)
                date = "0"+month+"-"+"0"+day;
            else if (month > 10 && day < 10)
                date = month+"-"+"0"+day;
            else date = month+"-"+day;

            dates[i] = date;
            // System.out.println("The dates are: "+dates[i]);
            // reset variables to start over
            month = cal.get(Calendar.MONTH)+1;
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        return dates;
    }

    // get day of the week
    public List<Integer> getWeek() {
        String userReq = Home_screen.eventRequest.toLowerCase();
        List<Integer> ret = new ArrayList<Integer>();
        if (userReq.contains("sunday") || userReq.contains("sun"))
            ret.add(1);
        if (userReq.contains("monday") || userReq.contains("mon"))
            ret.add(2);
        if (userReq.contains("tuesday") || userReq.contains("tues") || userReq.contains("tue"))
            ret.add(3);
        if (userReq.contains("wednesday") || userReq.contains("wed"))
            ret.add(4);
        if (userReq.contains("thursday") || userReq.contains("thu") ||
                userReq.contains("thur") || userReq.contains("thurs"))
            ret.add(5);
        if (userReq.contains("friday") || userReq.contains("fri"))
            ret.add(6);
        if (userReq.contains("saturday") || userReq.contains("sat"))
            ret.add(7);
       /* for (int i = 0; i < ret.size(); i++) {
            System.out.println("the days are: "+ret.get(i));
        }*/
        return ret;
    }

    // end date for recurring events
    public String endDate() throws ParseException {
        String ed = null;
        String userRequest = Home_screen.eventRequest.toLowerCase();

        // search within user input to ger end date info
        if (userRequest.contains("until")) {
            String[] temp = userRequest.split("until ");
            String date = temp[1];
            String[] temp1 = date.split(", ");
            String month = temp1[0];
            String year = findNum(temp1[1]);
            Date dt = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dt);
            int mon = cal1.get(Calendar.MONTH)+1;
            if (mon < 10)
                ed = year+"0"+mon;
            else ed = year+mon;
        }

        else {
            Calendar cal = Calendar.getInstance();
            int mon = cal.get(Calendar.MONTH)+2;
            int year = cal.get(Calendar.YEAR);
            if (mon < 10)
                ed = year+"0"+mon;
            else ed = Integer.toString(year)+mon;
        }
        //System.out.println("the end date is: "+ed);
        return ed;
    }


}
