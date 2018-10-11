package edu.hsb.proto.test.service.impl;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.domain.LocationAccuracy;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

public class GoogleLocationService implements ILocationService {

    private static final String TAG = GoogleLocationService.class.getSimpleName();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private ILocationListener listener;
    private LocationRequest locationRequest;
    private Location lastKnownLocation;
    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null && listener != null) {
                    lastKnownLocation = location;
                    listener.onLocationChanged(location);
                }
            }
        }
    };

    public GoogleLocationService(PreferenceManager preferenceManager, FusedLocationProviderClient fusedLocationProviderClient, SettingsClient settingsClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.settingsClient = settingsClient;

        // Define location request parameters
        final LocationAccuracy locationAccuracy = preferenceManager.getLocationAccuracy();
        if (locationAccuracy != null) {
            locationRequest = locationAccuracy.getLocationRequest();
        }
    }

    @Override
    public Location getLastLocation() {
        return lastKnownLocation;
    }

    @Override
    public void start(ILocationListener locationListener) {
        this.listener = locationListener;
        try {
            final Task<Location> lastLocationTask = fusedLocationProviderClient.getLastLocation();
            lastLocationTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    final Location result = task.getResult();
                    lastKnownLocation = result;
                    listener.onLocationChanged(result);
                } else {
                    Log.w(TAG, "Could not get last location: " + task.getException());
                }
            });
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission denied: " + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        this.listener = null;
    }

    @Override
    public Task<LocationSettingsResponse> checkSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        return settingsClient.checkLocationSettings(builder.build());
    }
}