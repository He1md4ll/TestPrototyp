package edu.hsb.proto.test.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;

import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.connection.ConnectionInterceptor;
import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.service.ILocationService;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.IMapService;
import edu.hsb.proto.test.service.impl.DefaultLoginService;
import edu.hsb.proto.test.service.impl.GoogleLocationService;
import edu.hsb.proto.test.service.impl.GoogleMapService;

public abstract class BaseModule {

    public PreferenceManager preferenceManager(Application application, SharedPreferences sharedPreferences) {
        return new PreferenceManager(application, sharedPreferences);
    }

    public ConnectionManager connectionManager(ConnectionInterceptor interceptor, ConnectivityManager connectivityManager) {
        return new ConnectionManager(interceptor, connectivityManager);
    }

    public SharedPreferences sharedPreferences(Application application) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(application);
    }

    public ConnectionInterceptor connectionInterceptor(Application application) {
        return new ConnectionInterceptor(application);
    }

    public ConnectivityManager connectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public ILoginService loginService(ConnectionManager connectionManager) {
        return new DefaultLoginService(connectionManager);
    }

    public IMapService mapService() {
        return new GoogleMapService();
    }

    public ILocationService locationService(PreferenceManager preferenceManager, FusedLocationProviderClient fusedLocationProviderClient, SettingsClient settingsClient) {
        return new GoogleLocationService(preferenceManager, fusedLocationProviderClient, settingsClient);
    }

    public FusedLocationProviderClient fusedLocationProviderClient(Application application) {
        return LocationServices.getFusedLocationProviderClient(application);
    }

    public SettingsClient settingsClient(Application application) {
        return LocationServices.getSettingsClient(application);
    }
}
