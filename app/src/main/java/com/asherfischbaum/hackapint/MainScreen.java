package com.asherfischbaum.hackapint;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class MainScreen extends AppCompatActivity {

    private ImageButton mPintButton;
    public static final String PREFS_NAME = "LocationPrefFile";

    private static Double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final Activity actContext = this;

        final GPSTrack gps = new GPSTrack(actContext);

        mPintButton = (ImageButton)findViewById(R.id.pintButton);
        mPintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //!!before uncommenting handle permission check
                //send location to DB
                gps.pushLocationToDB();
                lat = gps.getLat();
                lng = gps.getLong();

                Intent intent = new Intent(MainScreen.this, MatchPage.class);
                startActivity(intent);

                // send request of person they are meeting

                // send request of pub they are meeting.

            }
        });
    }

    public static Double getLat() {
        return lat;
    }

    public static Double getLng(){
        return lng;
    }

}
