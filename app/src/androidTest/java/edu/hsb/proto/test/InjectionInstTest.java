package edu.hsb.proto.test;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.google.common.truth.Truth;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.service.ILocationService;

public class InjectionInstTest extends BaseUITest {

    @Inject
    ILocationService locationService;

    @Real
    @Inject
    ILocationService locationServiceReal;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Override
    protected void inject(UITestComponent component) {
        component.inject(this);
    }

    @Test
    public void injectionTest() {
        Truth.assertThat(locationService).isNotNull();
        Truth.assertThat(locationServiceReal).isNotNull();
        Mockito.verifyZeroInteractions(locationService);
    }

    @Test
    public void espressoTest() {
        activityRule.getActivity();
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }
}