package edu.hsb.proto.test.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import dagger.Module;
import dagger.Provides;
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

    @Provides
    public PreferenceManager providePreferenceManager() {
        return mock(PreferenceManager.class);
    }

    @Real
    @Provides
    public PreferenceManager provideRealPreferenceManager(Application application, SharedPreferences sharedPreferences) {
        return preferenceManager(application, sharedPreferences);
    }

    @Provides
    public ConnectionManager provideConnectionManager() {
        return mock(ConnectionManager.class);
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Application application) {
        return sharedPreferences(application);
    }

    @Provides
    public ConnectionInterceptor provideConnectionInterceptor() {
        return mock(ConnectionInterceptor.class);
    }

    @Provides
    public ConnectivityManager provideConnectivityManager() {
        return mock(ConnectivityManager.class);
    }

    @Provides
    public ILoginService provideLoginService() {
        return mock(ILoginService.class);
    }

    @Provides
    public IMapService provideMapService() {
        return mock(IMapService.class);
    }

    @Provides
    protected ILocationService provideLocationService() {
        return mock(ILocationService.class);
    }

    @Real
    @Provides
    protected ILocationService provideRealLocationService(Application application, PreferenceManager preferenceManager) {
        return super.locationService(application, preferenceManager);
    }
}