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
import edu.hsb.proto.test.service.impl.DefaultLoginService;
import edu.hsb.proto.test.service.impl.GoogleLocationService;
import edu.hsb.proto.test.service.impl.GoogleMapService;

@Module
public class PrototypeModule {

    @Provides
    @Singleton
    public Context provideApplication(PrototypeApp application) {
        return application;
    }

    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager(Application application, SharedPreferences sharedPreferences) {
        return new PreferenceManager(application, sharedPreferences);
    }

    @Provides
    @Singleton
    public ConnectionManager provideConnectionManager(ConnectionInterceptor interceptor, ConnectivityManager connectivityManager) {
        return new ConnectionManager(interceptor, connectivityManager);
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Application application) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    public ConnectionInterceptor provideConnectionInterceptor(Application application) {
        return new ConnectionInterceptor(application);
    }

    @Provides
    public ConnectivityManager provideConnectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    public ILoginService provideLoginService(ConnectionManager connectionManager) {
        return new DefaultLoginService(connectionManager);
    }

    @Provides
    public IMapService provideMapService() {
        return new GoogleMapService();
    }

    @Provides
    public ILocationService provideLocationService(Application application, PreferenceManager preferenceManager) {
        return new GoogleLocationService(application, preferenceManager);
    }
}