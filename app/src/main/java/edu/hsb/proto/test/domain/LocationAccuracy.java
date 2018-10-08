package edu.hsb.proto.test.domain;

import com.google.android.gms.location.LocationRequest;

public enum LocationAccuracy {

    HIGH(0, 0, 0, LocationRequest.PRIORITY_HIGH_ACCURACY),
    BALANCE(1, 5, 1, LocationRequest.PRIORITY_HIGH_ACCURACY),
    LOW(5, 10, 1, LocationRequest.PRIORITY_HIGH_ACCURACY);

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