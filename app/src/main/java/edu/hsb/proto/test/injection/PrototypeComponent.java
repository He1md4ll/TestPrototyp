package edu.hsb.proto.test.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class, PrototypeModule.class})
public interface PrototypeComponent {

    void inject(PrototypeApp app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        PrototypeComponent build();
    }
}