package edu.hsb.proto.test;

import android.Manifest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.location.SettingsClient;
import com.google.common.truth.Truth;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.service.ILocationService;

public class InjectionInstTest extends BaseUITest {

    @Inject
    ILocationService locationService;

    @Inject
    SettingsClient settingsClientMock;

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Override
    protected void inject(UITestComponent component) {
        component.inject(this);
    }

    @Test
    public void injectionTest() {
        Truth.assertThat(locationService).isNotNull();
        Truth.assertThat(settingsClientMock).isNotNull();
        Mockito.mockingDetails(settingsClientMock).isMock();
    }

    @Test
    public void espressoTest() {
        activityRule.getActivity();
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }
}