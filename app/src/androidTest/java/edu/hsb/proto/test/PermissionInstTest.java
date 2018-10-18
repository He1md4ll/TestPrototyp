package edu.hsb.proto.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.google.common.truth.Truth;

import org.junit.Test;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public class PermissionInstTest extends BaseUITest {

    @Override
    protected void inject(UITestComponent component) {}

    @Test
    public void testPermissionRequested() throws UiObjectNotFoundException {
        // Given
        String expectedResult = getString(R.string.app_name) + " erlauben, den Gerätestandort abzurufen?";

        // When
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject text = device.findObject(new UiSelector().text(expectedResult));
        UiObject positiveButton = device.findObject(new UiSelector().text("ZULASSEN"));
        UiObject negativeButton = device.findObject(new UiSelector().text("ABLEHNEN"));

        // Then
        Truth.assertThat(text.exists()).isTrue();
        Truth.assertThat(positiveButton.exists()).isTrue();
        Truth.assertThat(negativeButton.exists()).isTrue();

        // After
        positiveButton.click();
    }


    @Test
    public void testPermissionGranted() throws UiObjectNotFoundException {
        // Given
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject positiveButton = device.findObject(new UiSelector().text("ZULASSEN"));

        // When
        positiveButton.click();

        // Then
        onView(withId(R.id.fab_monitoring)).check(matches(isDisplayed()));
    }

    @Test
    public void testPermissionDenied() throws UiObjectNotFoundException {
        // Given
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject negativeButton = device.findObject(new UiSelector().text("ABLEHNEN"));

        // When
        negativeButton.click();

        // Then
        onView(withText(R.string.rejected)).check(matches(isDisplayed()));
        onView(withText(R.string.ok)).perform(click());
        Truth.assertThat(getMainActivity().isFinishing()).isFalse();
        Truth.assertThat(getMainActivity().isDestroyed()).isFalse();
    }
}