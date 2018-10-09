package edu.hsb.proto.test.base;

import android.content.Context;
import android.support.annotation.StringRes;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import edu.hsb.proto.test.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, application = UnitTestApp.class)
public abstract class BaseUnitTest {

    private UnitTestApp application;

    @Before
    public void setUp() {
        application = (UnitTestApp) RuntimeEnvironment.application;

        // Inject objects from graph into test
        // @Inject --> Real Class || @Inject @Named("mock") --> Mocked Class
        inject(application.getComponent());
    }

    protected abstract void inject(UnitTestComponent component);

    public Context getContext() {
        return application;
    }

    public String getString(@StringRes int stringResId) {
        return application.getString(stringResId);
    }
}