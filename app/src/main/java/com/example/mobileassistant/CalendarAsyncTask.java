package com.example.mobileassistant;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarAsyncTask extends AsyncTask<Void, Void, List<String>> {
    private AccessCalendar activity;

    // Constructor
    CalendarAsyncTask(AccessCalendar activity) {
        this.activity = activity;
    }

    // Background task to call Google Calendar API
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            activity.clearResultsText();
            activity.updateResultsText(getEventsFromApi());

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

    // get events
    public List<String> getEventsFromApi() throws IOException {
        List<String> eventStrings = new ArrayList<String>();
        // list 10 upcoming events from the calendar
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = activity.service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
           // ret = "No upcoming events";
            eventStrings.add("No upcoming events");
        } else {
            System.out.println("Events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                //ret = "%s (%s)\n" + " " + event.getSummary() + " " + start;
                eventStrings.add(String.format("%s (%s)\n", event.getSummary(), start));
            }
        }
        return eventStrings;
    }

}
