package edu.hsb.proto.test.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import edu.hsb.proto.test.LocationAndroidServiceTest;
import edu.hsb.proto.test.PreferenceManagerTest;
import edu.hsb.proto.test.connection.ConnectionInterceptorTest;
import edu.hsb.proto.test.connection.ConnectionManagerTest;
import edu.hsb.proto.test.domain.LocationAccuracyTest;
import edu.hsb.proto.test.injection.ComponentBuilder;
import edu.hsb.proto.test.mobile.ConnectivityTest;
import edu.hsb.proto.test.mobile.InjectionTest;
import edu.hsb.proto.test.mobile.PerformanceTest;
import edu.hsb.proto.test.mobile.SecurityTest;
import edu.hsb.proto.test.service.impl.DefaultLoginServiceTest;
import edu.hsb.proto.test.service.impl.GoogleLocationServiceTest;

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
    void inject(PerformanceTest performanceTest);
    void inject(SecurityTest securityTest);
    void inject(ConnectivityTest connectivityTest);

    void inject(ConnectionInterceptorTest connectionInterceptorTest);
    void inject(ConnectionManagerTest connectionManagerTest);
    void inject(LocationAccuracyTest locationAccuracyTest);
    void inject(DefaultLoginServiceTest defaultLoginServiceTest);
    void inject(GoogleLocationServiceTest googleLocationServiceTest);
    void inject(LocationAndroidServiceTest locationAndroidServiceTest);
    void inject(PreferenceManagerTest preferenceManagerTest);
}