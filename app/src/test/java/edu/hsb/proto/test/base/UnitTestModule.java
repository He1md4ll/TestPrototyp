package edu.hsb.proto.test.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.SettingsClient;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.connection.ConnectionInterceptor;
import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.injection.BaseModule;
import edu.hsb.proto.test.service.ILocationService;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.IMapService;

import static org.mockito.Mockito.mock;

@Module
public class UnitTestModule extends BaseModule {

    @Provides
    public Context provideApplication(UnitTestApp application) {
        return application;
    }

    @Reusable
    @Provides
    public PreferenceManager providePreferenceManager() {
        return mock(PreferenceManager.class);
    }

    @Real
    @Provides
    public PreferenceManager provideRealPreferenceManager(Application application, @Real SharedPreferences sharedPreferences) {
        return preferenceManager(application, sharedPreferences);
    }

    @Reusable
    @Provides
    public ConnectionManager provideConnectionManager() {
        return mock(ConnectionManager.class);
    }

    @RealWithMocks
    @Provides
    public ConnectionManager provideRealWithMocksConnectionManager(ConnectionInterceptor connectionInterceptor, ConnectivityManager connectivityManager) {
        return connectionManager(connectionInterceptor, connectivityManager);
    }

    @Real
    @Provides
    public SharedPreferences provideSharedPreferences(Application application) {
        return sharedPreferences(application);
    }

    @Reusable
    @Provides
    public ConnectionInterceptor provideConnectionInterceptor() {
        return mock(ConnectionInterceptor.class);
    }

    @Real
    @Provides
    public ConnectionInterceptor provideRealConnectionInterceptor(Application application) {
        return connectionInterceptor(application);
    }

    @Reusable
    @Provides
    public ConnectivityManager provideConnectivityManager() {
        return mock(ConnectivityManager.class);
    }

    @Reusable
    @Provides
    public ILoginService provideLoginService() {
        return mock(ILoginService.class);
    }

    @RealWithMocks
    @Provides
    public ILoginService provideRealWithMocksLoginService(ConnectionManager connectionManager) {
        return loginService(connectionManager);
    }

    @Reusable
    @Provides
    public IMapService provideMapService() {
        return mock(IMapService.class);
    }

    @Reusable
    @Provides
    protected ILocationService provideLocationService() {
        return mock(ILocationService.class);
    }

    @RealWithMocks
    @Provides
    public ILocationService provideRealWithMocksLocationService(PreferenceManager preferenceManager, FusedLocationProviderClient fusedLocationProviderClient, SettingsClient settingsClient) {
        return locationService(preferenceManager, fusedLocationProviderClient, settingsClient);
    }

    @Reusable
    @Provides
    public FusedLocationProviderClient provideFusedLocationProviderClient() {
        return mock(FusedLocationProviderClient.class);
    }

    @Reusable
    @Provides
    public SettingsClient provideSettingsClient() {
        return mock(SettingsClient.class);
    }
}