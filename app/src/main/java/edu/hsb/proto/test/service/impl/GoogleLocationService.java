package edu.hsb.proto.test.service.impl;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private ILocationListener listener;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null && listener != null) {
                    listener.onLocationChanged(location);
                }
            }
        }
    };

    public GoogleLocationService(Context context, PreferenceManager preferenceManager) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Define location request parameters
        final LocationAccuracy locationAccuracy = preferenceManager.getLocationAccuracy();
        if (locationAccuracy != null) {
            locationRequest = locationAccuracy.getLocationRequest();
        }

        // Check device settings
        settingsClient = LocationServices.getSettingsClient(context);
    }

    @Override
    public Location getLastLocation() {
        return null;
    }

    @Override
    public void start(ILocationListener locationListener) {
        this.listener = locationListener;
        try {
            final Task<Location> lastLocationTask = fusedLocationClient.getLastLocation();
            lastLocationTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onLocationChanged(task.getResult());
                } else {
                    Log.w(TAG, "Could not get last location: " + task.getException());
                }
            });
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission denied: " + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        this.listener = null;
    }

    @Override
    public Task<LocationSettingsResponse> checkSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        return settingsClient.checkLocationSettings(builder.build());
    }
}