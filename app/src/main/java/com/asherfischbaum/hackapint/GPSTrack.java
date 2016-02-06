package com.asherfischbaum.hackapint;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Andrea on 06/02/2016.
 */
public class GPSTrack implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Firebase dbRef;

    final String ACCESS_KEY = "kyT8ImvzNUhDKKCGLGbxFgJ60923LldfWuTeSt5m";

    //need a way to get USER_KEY by authentication form db
    final String USER_KEY = "-K9o7msbxHVznV9UHqhA";

    private GoogleApiClient mGoogleApiClient;
    private Context context;

    //when you create the tracker form an activity you have to pass 'this' as an argument to the constructor
    public GPSTrack(Context context) {
        this.context = context;
        initDB();
        dbRef.authWithCustomToken(ACCESS_KEY, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
            }
        });

    }

    private void initDB() {
        Firebase.setAndroidContext(context);
    }

    private Location getLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //this code implements permission check --
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    public String getLat(){
        return String.valueOf(getLocation().getLatitude());
    }

    public String getLong(){
        return String.valueOf(getLocation().getLongitude());
    }

    public String getAltitude(){
        return String.valueOf(getLocation().getAltitude());
    }

    public void pushToDB(){
        dbRef.child("locations").child(USER_KEY).child("lat").setValue(getLat());
        dbRef.child("locations").child(USER_KEY).child("long").setValue(getLong());
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
