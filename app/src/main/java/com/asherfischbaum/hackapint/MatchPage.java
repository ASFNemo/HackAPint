package com.asherfischbaum.hackapint;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;

public class MatchPage extends AppCompatActivity {

    private TextView mNameText;
    private TextView mAgeText;
    private TextView mGenderText;
    private TextView mMobileText;

    private TextView mPubNameText;
    private TextView mAddressText;
    private TextView mPostcodeText;
    private TextView mTimeText;

    private Firebase firebase;
    private GeoFire geoFire;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_page);

        final GPSTrack gps = new GPSTrack(this);

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

        Connections con = new Connections();

        geoFire = con.getGeoFire();
        final Firebase firebase = con.getFireDB();

        Double lat;
        geoFire.getLocation(GPSTrack.USER_KEY, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(gps.getLat(), gps.getLong()), 1.5);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                final String THIS_KEY = key;
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNameText.setText(dataSnapshot.child("users").child(THIS_KEY).child("name").getValue().toString());
                        mAgeText.setText(dataSnapshot.child("users").child(THIS_KEY).child("age").getValue().toString());
                        mGenderText.setText(dataSnapshot.child("users").child(THIS_KEY).child("gender").getValue().toString());
                        mMobileText.setText(dataSnapshot.child("users").child(THIS_KEY).child("mobile").getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(FirebaseError error) {

            }
        });
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
