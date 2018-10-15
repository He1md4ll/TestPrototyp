package edu.hsb.proto.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import edu.hsb.proto.test.service.ILocationListener;

public abstract class LocationBase extends Fragment implements ServiceConnection, ILocationListener {

    private static final String TAG = LocationBase.class.getSimpleName();

    private LocationAndroidService locationAndroidService;

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this.getContext(), LocationAndroidService.class);
        intent.setAction(LocationAndroidService.INTENT_START_SERVICE);
        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LocationAndroidService.LocationAndroidServiceBinder binder =
                (LocationAndroidService.LocationAndroidServiceBinder) service;
        locationAndroidService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.w(TAG, "Service disconnected unexpectedly");
    }

    @Override
    public abstract void onLocationChanged(Location location);

    public void startLocationMonitoring() {
        if (locationAndroidService != null) {
            locationAndroidService.startMonitoring(this);
        }
    }

    public void stopLocationMonitoring() {
        if (locationAndroidService != null) {
            locationAndroidService.stopMonitoring();
        }
    }

    public boolean isLocationMonitoring() {
        return locationAndroidService != null && locationAndroidService.isMonitoring();
    }
}