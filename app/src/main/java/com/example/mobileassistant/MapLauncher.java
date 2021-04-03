package com.example.mobileassistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MapLauncher {

    private Context context;

    // Sets the context of the activity to the parameter
    public MapLauncher(Context context) {
        this.context = context;
    }

    // Opens the Google Maps app to the nagivate screen at an address
    public void openDirections(String directions) {
        Uri geoLocation = Uri.parse("geo:0,0?q=" + Uri.encode(directions));

        System.out.println("Launching maps");

        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }
}
