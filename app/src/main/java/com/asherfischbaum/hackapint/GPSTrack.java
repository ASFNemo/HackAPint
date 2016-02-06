package com.asherfischbaum.hackapint;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Andrea on 06/02/2016.
 */
public class GPSTrack implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private Firebase dbRef;
    private GeoFire geoFire;
    final String ACCESS_KEY = "kyT8ImvzNUhDKKCGLGbxFgJ60923LldfWuTeSt5m";

    //need a way to get USER_KEY by authentication form db
    public final static String USER_KEY = "-K9o7msbxHVznV9UHqhB";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Activity activity;
    private Context context;
    private Location location;

    private final static String TAG = GPSTrack.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //when you create the tracker form an activity you have to pass 'this' as an argument to the constructor
    public GPSTrack(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private void initDB() {
        Firebase.setAndroidContext(context);
        dbRef = new Firebase("https://grabapint.firebaseio.com/");
        dbRef.authWithCustomToken(ACCESS_KEY, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v(TAG, "AUTHENTICATED");
                updateLocation();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
            }
        });

        geoFire = new GeoFire(dbRef);
    }

    private void updateLocation() {

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    public String getLat(){
        if(location!=null)
            return String.valueOf(location.getLatitude());
        else{
            Log.d(TAG, "LOCATION IS NULL");
            return null;
        }
    }

    public String getLong(){
        if(location!=null)
            return String.valueOf(location.getLongitude());
        else{
            Log.d(TAG,"LOCATION IS NULL");
            return null;
        }
    }


    public void pushLocationToDB(){
        geoFire.setLocation(USER_KEY, new GeoLocation(Double.parseDouble(getLat()), Double.parseDouble(getLong())));
    }


    @Override
    public void onConnected(Bundle bundle) {
        initDB();
        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error

                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void disconnectGAPI(){
        mGoogleApiClient.disconnect();
    }

}
