package edu.hsb.proto.test.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import edu.hsb.proto.test.InjectionInstTest;
import edu.hsb.proto.test.LoginInstTest;
import edu.hsb.proto.test.ResourceInstTest;
import edu.hsb.proto.test.injection.ComponentBuilder;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class, ComponentBuilder.class, UITestModule.class})
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
    void inject(InjectionInstTest injectionInstTest);
    void inject(LoginInstTest loginInstTest);
    void inject(ResourceInstTest resourceInstTest);
}
