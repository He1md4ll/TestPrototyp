package edu.hsb.proto.test;

import android.Manifest;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.rules.Tracing;
import edu.hsb.proto.test.service.impl.DefaultLoginService;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static edu.hsb.proto.test.LoginInstTest.setProgress;
import static org.hamcrest.Matchers.not;

@LargeTest
public class PerformanceInstTest extends BaseUITest {

    private static final int BASE_LOGIN_SPEED = 2; // seconds
    private static final int MAX_HASH_TIME = 1; // seconds

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public Tracing methodTracing = new Tracing();

    @Rule
    public Timeout timeout = new Timeout(BASE_LOGIN_SPEED + MAX_HASH_TIME, TimeUnit.SECONDS);

    @Override
    protected void inject(UITestComponent component) {}

    @Before
    public void init() {
        // Change displayed fragment to login fragment
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.menu_activity_login)).perform(click());
    }

    @Test
    public void testHashPerformance1000() {
        // Given
        int rounds = 1000;
        boolean enc = Boolean.FALSE;

        // When
        onView(withId(R.id.login_username)).perform(replaceText(DefaultLoginService.RIGHT_USERNAME));
        onView(withId(R.id.login_password)).perform(replaceText(DefaultLoginService.RIGHT_PASSWORD));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        if (enc) {
            onView(withId(R.id.login_encryption)).perform(click());
        }
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testHashPerformance50000() {
        // Given
        int rounds = 50000;
        boolean enc = Boolean.FALSE;

        // When
        onView(withId(R.id.login_username)).perform(replaceText(DefaultLoginService.RIGHT_USERNAME));
        onView(withId(R.id.login_password)).perform(replaceText(DefaultLoginService.RIGHT_PASSWORD));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        if (enc) {
            onView(withId(R.id.login_encryption)).perform(click());
        }
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public void testHashPerformance100000() {
        // Given
        int rounds = 100000;
        boolean enc = Boolean.FALSE;

        // When
        onView(withId(R.id.login_username)).perform(replaceText(DefaultLoginService.RIGHT_USERNAME));
        onView(withId(R.id.login_password)).perform(replaceText(DefaultLoginService.RIGHT_PASSWORD));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        if (enc) {
            onView(withId(R.id.login_encryption)).perform(click());
        }
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
}