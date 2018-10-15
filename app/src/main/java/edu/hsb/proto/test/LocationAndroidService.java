package edu.hsb.proto.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

public class LocationAndroidService extends Service {

    public static final String INTENT_START_SERVICE = "start_service";
    private static final String TAG = LocationAndroidService.class.getSimpleName();

    @Inject
    ILocationService locationServices;

    private boolean monitoring;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationAndroidServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }

    @Nullable
    public Task<LocationSettingsResponse> startMonitoring(ILocationListener locationListener) {
        if (!monitoring) {
            final Task<LocationSettingsResponse> task = locationServices.checkSettings();
            task.addOnSuccessListener(locationSettingsResponse -> {
                locationServices.start(locationListener);
                monitoring = Boolean.TRUE;
            });
            return task;
        } else {
            return null;
        }
    }

    public void stopMonitoring() {
        locationServices.stop();
        monitoring = Boolean.FALSE;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public class LocationAndroidServiceBinder extends Binder {
        public LocationAndroidService getService() {
            return LocationAndroidService.this;
        }
    }

    @VisibleForTesting
    public void setLocationServices(ILocationService locationServices) {
        this.locationServices = locationServices;
    }
}