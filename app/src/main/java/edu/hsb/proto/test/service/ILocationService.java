package edu.hsb.proto.test.service;

import android.location.Location;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;

public interface ILocationService {
    void start(ILocationListener locationListener);
    void stop();
    Task<LocationSettingsResponse> checkSettings();
    Location getLastLocation();
}