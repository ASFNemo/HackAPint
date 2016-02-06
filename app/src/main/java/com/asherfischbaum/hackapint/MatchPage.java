package com.asherfischbaum.hackapint;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MatchPage extends AppCompatActivity {

    private TextView mNameText;
    private TextView mAgeText;
    private TextView mGenderText;
    private TextView mMobileText;

    private TextView mPubNameText;
    private TextView mAddressText;
    private TextView mPostcodeText;
    private TextView mTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_page);

        ImageButton mGoogleMapsButton = (ImageButton)findViewById(R.id.googleMapsButton);
        mGoogleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opePreferredLocationInMap();
            }
        });

        mNameText = (TextView) findViewById(R.id.nameText);
        mAgeText = (TextView) findViewById(R.id.ageText);
        mGenderText = (TextView) findViewById(R.id.genderText);
        mMobileText = (TextView) findViewById(R.id.mobileText);
        mPubNameText = (TextView) findViewById(R.id.pubNameText);
        mAddressText = (TextView) findViewById(R.id.addressText);
        mPostcodeText = (TextView) findViewById(R.id.postcodeText);
        mTimeText = (TextView) findViewById(R.id.timeText);


    }


      //THIS IS FOR THE BUTTON WHERE THE USER IS TOLD THE PUB AND ADDRESS AND THEY CAN GO TO GOOGLE MAPS.

    private void opePreferredLocationInMap(){
        //this is getting the set location in my weather app, so change it for the beerapp
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        // change the pref_location_key - these are strings -  to the address of the user.
        //String location = getString(R.string.pref_location_key);
        String location ="27 heton gardens, nw4 4xs";


        //this creates the link that will launch the map app
        Uri geolocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",location).build();
        //create the implicit intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);

        //launch intent
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        //else print to log ?
    }





}
