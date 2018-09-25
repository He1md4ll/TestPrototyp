package edu.hsb.proto.test.domain;

import com.google.android.gms.location.LocationRequest;

public enum LocationAccuracy {

    EXACT(10 * 1000, 3 * 1000, 10, LocationRequest.PRIORITY_HIGH_ACCURACY),
    BALANCE(30 * 1000, 10 * 1000, 100, LocationRequest.PRIORITY_HIGH_ACCURACY),
    POWER(60 * 1000, 20 * 1000, 500, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    private int interval;
    private int passiveInterval;
    private int smallestDisplacement;
    private int priority;

    LocationAccuracy(int interval, int passiveInterval, int smallestDisplacement, int priority) {
        this.interval = interval;
        this.passiveInterval = passiveInterval;
        this.smallestDisplacement = smallestDisplacement;
        this.priority = priority;
    }

    public LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(passiveInterval);
        locationRequest.setSmallestDisplacement(smallestDisplacement);
        locationRequest.setPriority(priority);
        return locationRequest;
    }
}