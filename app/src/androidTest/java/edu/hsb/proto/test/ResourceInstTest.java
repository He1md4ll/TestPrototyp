package edu.hsb.proto.test;

import android.Manifest;
import android.os.BatteryManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.util.Log;

import com.google.common.truth.Truth;

import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.rules.BatteryStats;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class ResourceInstTest extends BaseUITest {

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public BatteryStats batteryStats = new BatteryStats();

    @Inject
    BatteryManager batteryManager;

    @Override
    protected void inject(UITestComponent component) {
        component.inject(this);
    }

    @Test
    public void testLocationMonitoring5s() throws InterruptedException {
        // Given
        boolean charging = batteryManager.isCharging();
        double capacityBefore = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        onView(withId(R.id.fab_monitoring)).perform(click());

        // When --> Wait 5 seconds
        Thread.sleep(5000);

        // Then
        double capacityAfter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Truth.assertThat(capacityAfter / capacityBefore).isAtLeast(0.999);
        if (charging) {
            Log.w("ResourceInstTest", "Device is charging --> Battery capacity test useless");
        }
    }

    @Test
    public void testLocationMonitoring60s() throws InterruptedException {
        // Given
        boolean charging = batteryManager.isCharging();
        double capacityBefore = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        onView(withId(R.id.fab_monitoring)).perform(click());

        // When --> Wait 60 seconds
        Thread.sleep(60000);

        // Then
        double capacityAfter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Truth.assertThat(capacityAfter / capacityBefore).isAtLeast(0.99);
        if (charging) {
            Log.w("ResourceInstTest", "Device is charging --> Battery capacity test useless");
        }
    }
}