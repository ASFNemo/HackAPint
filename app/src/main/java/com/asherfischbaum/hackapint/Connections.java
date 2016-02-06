package com.asherfischbaum.hackapint;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;

/**
 * Created by Andrea on 06/02/2016.
 */
public class Connections {

    private static Firebase fireDB;
    private static GeoFire geoFire;

    final String ACCESS_KEY = "kyT8ImvzNUhDKKCGLGbxFgJ60923LldfWuTeSt5m";

    //need a way to get USER_KEY by authentication form db
    private final static String USER_KEY = "-K9o7msbxHVznV9UHqhA";

    public Connections() {
        fireDB = new Firebase("https://grabapint.firebaseio.com/");
        fireDB.authWithCustomToken(ACCESS_KEY, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }
        });

        geoFire = new GeoFire(fireDB);
    }

    public GeoFire getGeoFire() {
        return geoFire;
    }

    public Firebase getFireDB() {
        return fireDB;
    }
}
