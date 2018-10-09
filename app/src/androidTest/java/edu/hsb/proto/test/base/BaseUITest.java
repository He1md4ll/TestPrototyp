package edu.hsb.proto.test.base;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class BaseUITest {

    private UITestApp application;

    @Before
    public void setUp() {
        application = (UITestApp) InstrumentationRegistry.getTargetContext().getApplicationContext();

        // Inject objects from graph into test
        // @Inject --> Real Class || @Inject @Named("mock") --> Mocked Class
        inject(application.getComponent());
    }

    protected abstract void inject(UITestComponent component);

    public Context getContext() {
        return application;
    }

    public String getString(@StringRes int stringResId) {
        return application.getString(stringResId);
    }
}