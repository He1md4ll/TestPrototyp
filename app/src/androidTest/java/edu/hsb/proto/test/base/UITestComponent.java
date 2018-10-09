package edu.hsb.proto.test.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import edu.hsb.proto.test.ExampleInstrumentedTest;
import edu.hsb.proto.test.injection.PrototypeModule;

@Singleton
@Component(modules = {PrototypeModule.class, UITestModule.class})
public interface UITestComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        UITestComponent build();
    }

    void inject(UITestApp app);

    /*
     * Inject declarations for UI Tests
     */
    void inject(ExampleInstrumentedTest exampleInstrumentedTest);
}
