package edu.hsb.proto.test.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import edu.hsb.proto.test.InjectionTest;
import edu.hsb.proto.test.injection.ComponentBuilder;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class, ComponentBuilder.class, UnitTestModule.class})
public interface UnitTestComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        UnitTestComponent build();
    }

    void inject(UnitTestApp app);

    /*
     * Inject declarations for Unit Tests
     */
    void inject(InjectionTest injectionTest);
}