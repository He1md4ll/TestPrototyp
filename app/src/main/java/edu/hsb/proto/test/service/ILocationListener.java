package edu.hsb.proto.test.service;

import android.location.Location;

public interface ILocationListener {
    void onLocationChanged(Location location);
}