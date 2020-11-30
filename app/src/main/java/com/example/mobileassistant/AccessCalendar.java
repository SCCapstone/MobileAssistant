package com.example.mobileassistant;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class AccessCalendar extends AppCompatActivity {

   // A Google Calendar API service object used to access the API
    Calendar service;
    GoogleAccountCredential credential;
    private TextView mStatusText;
    private TextView mResultsText;
    final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();
    // final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    // final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    // Google Calendar Scopes
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY};
    private static final String TAG = "calendar";
    ProgressDialog pd; // progress report use to debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         // Default view to show the results
         LinearLayout activityLayout = new LinearLayout(this);
         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.MATCH_PARENT);
         activityLayout.setLayoutParams(lp);
         activityLayout.setOrientation(LinearLayout.VERTICAL);
         activityLayout.setPadding(16, 16, 16, 16);

         ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT,
               ViewGroup.LayoutParams.WRAP_CONTENT);

         mStatusText = new TextView(this);
         mStatusText.setLayoutParams(tlp);
         mStatusText.setTypeface(null, Typeface.BOLD);
         mStatusText.setText("Retrieving data...");
         activityLayout.addView(mStatusText);

         mResultsText = new TextView(this);
         mResultsText.setLayoutParams(tlp);
         mResultsText.setPadding(16, 16, 16, 16);
         mResultsText.setVerticalScrollBarEnabled(true);
         mResultsText.setMovementMethod(new ScrollingMovementMethod());
         activityLayout.addView(mResultsText);

         setContentView(activityLayout);

         pd = new ProgressDialog(this);
         pd.setMessage("Calling Google Calendar API ...");
         // SharedPreferences to save google account info
         SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
         // Initialize credentials and service object.
         credential = GoogleAccountCredential.usingOAuth2(
               getApplicationContext(), Arrays.asList(SCOPES))
               .setBackOff(new ExponentialBackOff())
               .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
               //.setSelectedAccount(new Account(PREF_ACCOUNT_NAME, "com.example.mobileassistant"));

         service = new Calendar.Builder(
              HTTP_TRANSPORT, jsonFactory, credential)
              .setApplicationName("MobileAssistant")
              .build();
    }

      /**
      * Called whenever this activity is pushed to the foreground, such as after
      * a call to onCreate().
      */
      @Override
      protected void onResume() {
         super.onResume();
         // if google play services available, get the results
          // otherwise give user feedback
         if (isGooglePlayServicesAvailable()) {
            refreshResults();
         } else {
            mStatusText.setText("Google Play Services is required: " +
                  "after installing, please close and relaunch this app.");
         }
      }

      /**
       * Called when an activity launched here
       */
      @Override
      protected void onActivityResult(
              int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         switch(requestCode) {
            // if google play service available, get results
             // otherwise check the availability
            case REQUEST_GOOGLE_PLAY_SERVICES:
               if (resultCode == RESULT_OK) {
                  refreshResults();
               } else {
                  isGooglePlayServicesAvailable();
               }
               break;
            // if an google account exits, use that account
             // otherwise, give user feedback
            case REQUEST_ACCOUNT_PICKER:
               if (resultCode == RESULT_OK && data != null &&
                       data.getExtras() != null) {
                  String accountName =
                          data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                  if (accountName != null) {
                     credential.setSelectedAccountName(accountName);
                     //credential.setSelectedAccount(new Account(PREF_ACCOUNT_NAME, "com.example.mobileassistant"));
                     SharedPreferences settings =
                             getPreferences(Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor = settings.edit();
                     editor.putString(PREF_ACCOUNT_NAME, accountName);
                     editor.commit();
                     refreshResults();
                  }
               } else if (resultCode == RESULT_CANCELED) {
                  mStatusText.setText("Account unspecified.");
               }
               break;
            // if google account authorized, get results
             // otherwise, ask the user to pick one account
            case REQUEST_AUTHORIZATION:
               if (resultCode == RESULT_OK) {
                  refreshResults();
               } else {
                  chooseAccount();
               }
               break;
         }

         super.onActivityResult(requestCode, resultCode, data);
      }

      /**
       * get data from Google Calendar API to display.
       * if no google account been chose, ask the user to pick one account
       */
      private void refreshResults() {
         if (credential.getSelectedAccountName() == null) {
            chooseAccount();
         } else {
            if (isDeviceOnline()) {
               new CalendarAsyncTask(this).execute();
            } else {
               mStatusText.setText("No network connection available.");
            }
         }
      }

      /**
       * clear old data and reset UI
       */
      public void clearResultsText() {
         runOnUiThread(new Runnable() {
            @Override
            public void run() {
               mStatusText.setText("Retrieving data…");
               mResultsText.setText("");
            }
         });
      }

      /**
       * Fill the data TextView with results
       */
      public void updateResultsText(final List<String> dataStrings) {
         runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if (dataStrings == null) {
                  mStatusText.setText("Error retrieving data!");
               } else if (dataStrings.size() == 0) {
                  mStatusText.setText("No data found.");
               } else {
                  mStatusText.setText("Data retrieved using" +
                          " the Google Calendar API:");
                  mResultsText.setText(TextUtils.join("\n\n", dataStrings));
               }
            }
         });
      }

      /**
       * Show a status message in the list header TextView
       */
      public void updateStatus(final String message) {
         runOnUiThread(new Runnable() {
            @Override
            public void run() {
               mStatusText.setText(message);
            }
         });
      }

      /**
       * start google login activity to ask the user to pick an account
       */
      private void chooseAccount() {
         startActivityForResult(
                 credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }

      /**
       * Check network connection
       */
      private boolean isDeviceOnline() {
         ConnectivityManager connMgr =
                 (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
         return (networkInfo != null && networkInfo.isConnected());
      }

      /**
       * Check that Google Play services APK is installed and up to date. Will
       * launch an error dialog for the user to update Google Play Services if
       * possible.
       */
      private boolean isGooglePlayServicesAvailable() {
         final int connectionStatusCode =
                 GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
         if (connectionStatusCode != ConnectionResult.SUCCESS) {
             if (GoogleApiAvailability.getInstance().isUserResolvableError(connectionStatusCode)) {
                 GoogleApiAvailability.getInstance().getErrorDialog(this,connectionStatusCode , REQUEST_GOOGLE_PLAY_SERVICES)
                         .show();
             }else {
                 Log.i(TAG, "This device is not supported.");
                 finish();
             }return false;
         }
          return true;
      }



      /**
       * Display an error dialog showing that Google Play Services is missing
       * or out of date.
       */
      void showGooglePlayServicesAvailabilityErrorDialog(
              final int connectionStatusCode) {
         runOnUiThread(new Runnable() {
            @Override
            public void run() {
               Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                       AccessCalendar.this,connectionStatusCode , REQUEST_GOOGLE_PLAY_SERVICES);
               dialog.show();
            }
         });
      }

   }