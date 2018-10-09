package edu.hsb.proto.test.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import edu.hsb.proto.test.ExampleUnitTest;
import edu.hsb.proto.test.injection.PrototypeModule;

@Singleton
@Component(modules = {PrototypeModule.class, UnitTestModule.class})
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
    void inject(ExampleUnitTest app);
}