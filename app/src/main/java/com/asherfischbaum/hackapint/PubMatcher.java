package com.asherfischbaum.hackapint;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bogdanbuduroiu on 06/02/2016.
 */
public class PubMatcher extends AsyncTask<Void, Void, Void> {

    private Double lat;
    private Double lng;

    final String API_KEY = "AIzaSyBrJ1pFSTBP_wiyaqObnX4a1X2LLLx_Ubg";
    final String BASE_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public PubMatcher(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ArrayList<Pub> pubs = getPubs(lat, lng);

        return null;
    }

    public ArrayList<Pub> getPubs(Double lat, Double lng) {

        try{
            URL url;
            String loc = lat + "" + lng;
            ArrayList<Pub> pubs = new ArrayList<>();

            Uri builtUri = Uri.parse(BASE_API_URL).buildUpon()
                    .appendQueryParameter("location", "50.9341890,-1.3956850")
                    .appendQueryParameter("radius", "1000")
                    .appendQueryParameter("types", "bar")
                    .appendQueryParameter("key", API_KEY)
                    .build();

            url = new URL(builtUri.toString());

            Log.v("JSON", url.toString());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream in = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                Log.v("JSON", line);
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i<results.length(); i++) {

                String name;
                JSONObject geometry;
                JSONObject location;
                Double lati;
                Double lngi;
                String vicinity;

                JSONObject result = results.getJSONObject(i);
                name = result.getString("name");
                geometry = result.getJSONObject("geometry");
                location = geometry.getJSONObject("location");
                lati = Double.parseDouble(location.getString("lat"));
                lngi = Double.parseDouble(location.getString("lng"));
                vicinity = result.getString("vicinity");

                pubs.add(new Pub(name, lati, lngi, vicinity));
            }

            return pubs;
        }
        catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Pub {

    public Pub(String name, Double lat, Double lng, String vicinity) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.vicinity = vicinity;
    }

    private String name;

    private Double lat;

    private Double lng;

    private String vicinity;

    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getVicinity() {
        return vicinity;
    }
}