package edu.hsb.proto.test.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.connection.ConnectionInterceptor;
import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.service.ILocationService;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.IMapService;

@Module
public class PrototypeModule extends BaseModule {

    @Provides
    @Singleton
    public Context provideApplication(PrototypeApp application) {
        return application;
    }

    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager(Application application, SharedPreferences sharedPreferences) {
        return preferenceManager(application, sharedPreferences);
    }

    @Provides
    @Singleton
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
    public ILocationService provideLocationService(Application application, PreferenceManager preferenceManager) {
        return locationService(application, preferenceManager);
    }
}