package edu.hsb.proto.test.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PrototypeModule {

    @Provides
    @Singleton
    public Context provideApplication(PrototypeApp application) {
        return application;
    }
}