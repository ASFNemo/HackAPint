package com.asherfischbaum.hackapint;

/**
 * Created by bogdanbuduroiu on 07/02/2016.
 */
public class Pub {

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
