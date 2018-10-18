package edu.hsb.proto.test;

import android.Manifest;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.widget.DrawerLayout;

import org.junit.Rule;
import org.junit.Test;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
public class GrayVSBlackInstTest extends BaseUITest {

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Override
    protected void inject(UITestComponent component) {}

    @Test
    public void testMainNavigationGreyBox() {
        // When
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Then
        onView(withText(R.string.menu_activity_map)).check(matches(withParent(isClickable())));
        onView(withText(R.string.menu_activity_map)).check(matches(
                allOf(isChecked(), hasTextColor(R.color.colorPrimary), isDisplayed())));
        onView(withText(R.string.menu_activity_login)).check(matches(withParent(isClickable())));
        onView(withText(R.string.menu_activity_login)).check(matches(allOf(not(isChecked()), isDisplayed())));
    }

    @Test
    public void testMainNavigationBlackBox() {
        // When
        onView(allOf(hasFocus(), isAssignableFrom(DrawerLayout.class))).perform(DrawerActions.open());

        // Then
        onView(withText("Map")).check(matches(withParent(isClickable())));
        onView(withText("Map")).check(matches(allOf(isChecked(), isDisplayed())));
        onView(withText("Login")).check(matches(withParent(isClickable())));
        onView(withText("Login")).check(matches(allOf(not(isChecked()), isDisplayed())));
    }

    @Test
    public void testMainNavigationClickMix() {
        // When
        onView(allOf(hasFocus(), isAssignableFrom(DrawerLayout.class))).perform(DrawerActions.open());
        onView(allOf(isClickable(), withChild(withText("Login")))).perform(click());

        // Then
        onView(withId(R.id.login_username)).check(matches(isDisplayed()));
        onView(withId(R.id.login_password)).check(matches(isDisplayed()));
        onView(withText("LOGIN ONLINE")).check(matches(allOf(isClickable(), isDisplayed())));
        onView(withText("LOGIN OFFLINE")).check(matches(allOf(isClickable(), isDisplayed())));
    }
}
