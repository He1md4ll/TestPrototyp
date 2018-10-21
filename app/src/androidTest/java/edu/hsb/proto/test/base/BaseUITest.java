package edu.hsb.proto.test.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.autofill.AutofillManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import edu.hsb.proto.test.MainActivity;

@RunWith(AndroidJUnit4.class)
public abstract class BaseUITest {

    private UITestApp application;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        application = (UITestApp) InstrumentationRegistry.getTargetContext().getApplicationContext();

        // Inject objects from graph into test
        // @Inject --> Real Class || @Inject @Named("mock") --> Mocked Class
        inject(application.getComponent());
        disableAutoFill();
    }

    protected abstract void inject(UITestComponent component);

    public Context getContext() {
        return application;
    }

    public ActivityTestRule<MainActivity> getActivityRule() {
        return activityRule;
    }

    public MainActivity getMainActivity() {
        return activityRule.getActivity();
    }

    public String getString(@StringRes int stringResId) {
        return application.getString(stringResId);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                final AutofillManager autofillManager = application.getSystemService(AutofillManager.class);
                autofillManager.disableAutofillServices();
            } catch (Exception e) {
                Log.w(UITestApp.class.getSimpleName(), "Could not disable auto fill services");
            }
        }
    }
}