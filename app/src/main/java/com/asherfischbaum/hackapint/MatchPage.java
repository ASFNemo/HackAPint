package com.asherfischbaum.hackapint;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private Double lat = MainScreen.getLat();
    private Double lng = MainScreen.getLng();

    private String OTHER_KEY;
    private final Activity matchPage = this;

    private User matchedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_page);

        GPSTrack gps = new GPSTrack(this);

        ImageButton mGoogleMapsButton = (ImageButton)findViewById(R.id.googleMapsButton);
        mGoogleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opePreferredLocationInMap();
            }
        });

        //personDetails
        mNameText = (TextView) findViewById(R.id.nameText);
        mAgeText = (TextView) findViewById(R.id.ageText);
        mGenderText = (TextView) findViewById(R.id.genderText);
        mMobileText = (TextView) findViewById(R.id.mobileText);
        mPubNameText = (TextView) findViewById(R.id.pubNameText);
        mAddressText = (TextView) findViewById(R.id.addressText);
        mPostcodeText = (TextView) findViewById(R.id.postcodeText);
        mTimeText = (TextView) findViewById(R.id.timeText);

        //PubDetails
        mPubNameText = (TextView) findViewById(R.id.pubNameText);
        mAddressText = (TextView) findViewById(R.id.addressText);
        mTimeText = (TextView) findViewById(R.id.timeText);

        fillPubInfo();




        Connections con = new Connections();

        geoFire = con.getGeoFire();
        final Firebase firebase = con.getFireDB();

        //ACCEPT BUTTON TURNS hasAccepted to true on DB
        ImageButton mAccept;
        mAccept = (ImageButton) findViewById(R.id.Accept);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.child("users").child(GPSTrack.USER_KEY).child("hasAccepted").setValue(true);
                Log.d("ACCEPT PRESSED:","The user has sent a notification to the other user");
            }
        });

        ImageButton mBackButton;
        mBackButton = (ImageButton) findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchPage.this, MainScreen.class);
                startActivity(intent);

                //cancel data sent

            }
        });



        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 1.5);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(!key.equals(GPSTrack.USER_KEY)) {
                    final String THIS_KEY = key;
                    OTHER_KEY = key;
                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //match!
                            Matcher matcher = new Matcher(THIS_KEY, GPSTrack.USER_KEY);
                            matcher.sendRequest();

                            try {
                                Thread.sleep(1000);
                            }catch (Exception e){

                            }

                            matcher.checkMutual();
                            matcher.runtheCheck();
                            //check if after 20 seconds if it is still waiting change key

                            if(matcher.checkMutual()){
                                mNameText.setText(dataSnapshot.child("users").child(THIS_KEY).child("name").getValue().toString());
                                mAgeText.setText(dataSnapshot.child("users").child(THIS_KEY).child("age").getValue().toString());
                                mGenderText.setText(dataSnapshot.child("users").child(THIS_KEY).child("gender").getValue().toString());
                                mMobileText.setText(dataSnapshot.child("users").child(THIS_KEY).child("mobile").getValue().toString());

                                matchedUser.setGender(dataSnapshot.child("users").child(THIS_KEY).child("gender").getValue().toString());
                                matchedUser.setName(dataSnapshot.child("users").child(THIS_KEY).child("name").getValue().toString());
                                matchedUser.setLat(Double.parseDouble(dataSnapshot.child(THIS_KEY).child("lat").getValue().toString()));
                                matchedUser.setLng(Double.parseDouble(dataSnapshot.child(THIS_KEY).child("lat").getValue().toString()));
                                matchedUser.setAge(Integer.parseInt(dataSnapshot.child("users").child(THIS_KEY).child("age").getValue().toString()));
                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
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

        /*
        Try to fetch acceptButton if was pressed
         */
        firebase.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            /*
            Launch Toast if other user pressed Button
             */
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if((boolean) dataSnapshot.child(OTHER_KEY).child("hasAccepted").getValue()){
                    String name = dataSnapshot.child(OTHER_KEY).child("name").getValue().toString();
                    int time = getDiffTime(); //replace with difference form timestamp
                    Toast.makeText(matchPage, name + " confirmed he will be there in " +time+ " minutes.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //THIS IS FOR THE BUTTON WHERE THE USER IS TOLD THE PUB AND ADDRESS AND THEY CAN GO TO GOOGLE MAPS.

    private void opePreferredLocationInMap(){
        //location
        String location = mAddressText.getText().toString();


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

    Calendar meetingTime ;


    private void fillPubInfo(){
        //get Meeting time (20 minutes form now)
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "hh:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, 20);
        meetingTime = c;
        mTimeText.setText(sDateFormat.format(c.getTime()));

        //fill rest of it
        //mAddressText.setText();
        //mPubNameText.setText();
    }


    private int getDiffTime(){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        long result = ((meetingTime.getTime().getTime()/60000) - (now.getTime().getTime()/60000));
        return (int) result;
    }

}
