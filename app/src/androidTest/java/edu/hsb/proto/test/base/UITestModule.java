package edu.hsb.proto.test.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.BatteryManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.SettingsClient;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.connection.ConnectionInterceptor;
import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.injection.BaseModule;
import edu.hsb.proto.test.injection.PrototypeApp;
import edu.hsb.proto.test.service.ILocationService;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.IMapService;

@Module
public class UITestModule extends BaseModule {

    @Provides
    public Context provideApplication(PrototypeApp application) {
        return application;
    }

    @Provides
    public PreferenceManager providePreferenceManager(Application application, SharedPreferences sharedPreferences) {
        return preferenceManager(application, sharedPreferences);
    }

    @Provides
    public ConnectionManager provideConnectionManager(ConnectionInterceptor interceptor, ConnectivityManager connectivityManager) {
        return connectionManager(interceptor, connectivityManager);
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Application application) {
        return sharedPreferences(application);
    }

    @Provides
    public ConnectionInterceptor provideConnectionInterceptor(Application application) {
        return connectionInterceptor(application);
    }

    @Provides
    public ConnectivityManager provideConnectivityManager(Application application) {
        return connectivityManager(application);
    }

    @Provides
    public ILoginService provideLoginService(ConnectionManager connectionManager) {
        return loginService(connectionManager);
    }

    @Provides
    public IMapService provideMapService() {
        return mapService();
    }

    @Provides
    public ILocationService provideLocationService(PreferenceManager preferenceManager, FusedLocationProviderClient fusedLocationProviderClient, @Real SettingsClient settingsClient) {
        return locationService(preferenceManager, fusedLocationProviderClient, settingsClient);
    }

    @Provides
    public FusedLocationProviderClient provideFusedLocationProviderClient(Application application) {
        return fusedLocationProviderClient(application);
    }

    @Real
    @Provides
    public SettingsClient provideRealSettingsClient(Application application) {
        return settingsClient(application);
    }

    @Provides
    public SettingsClient provideSettingsClient() {
        return Mockito.mock(SettingsClient.class);
    }

    @Provides
    public BatteryManager provideBatteryManager(Application application) {
        return (BatteryManager) application.getSystemService(Context.BATTERY_SERVICE);
    }
}