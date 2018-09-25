package edu.hsb.proto.test.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.service.ILocationService;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.IMapService;
import edu.hsb.proto.test.service.impl.FakeLoginService;
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
    public SharedPreferences provideSharedPreferences(Application application) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    public ILoginService provideLoginSerive() {
        return new FakeLoginService();
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