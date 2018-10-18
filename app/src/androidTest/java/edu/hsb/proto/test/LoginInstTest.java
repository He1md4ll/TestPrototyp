package edu.hsb.proto.test;

import android.Manifest;
import android.app.Application;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.service.impl.DefaultLoginService;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@LargeTest
public class LoginInstTest extends BaseUITest {

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Inject
    Application application;

    @Override
    protected void inject(UITestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        // Change displayed fragment to login fragment
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.menu_activity_login)).perform(click());
    }

    @Test
    public void testViewPresence() {
        // Then
        onView(withId(R.id.login_username)).check(matches(isDisplayed()));
        onView(withId(R.id.login_password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_rounds_label)).check(matches(isDisplayed()));
        onView(withId(R.id.login_rounds)).check(matches(isDisplayed()));
        onView(withId(R.id.login_encryption)).check(matches(isDisplayed()));
        onView(withId(R.id.login_online_button)).check(matches(isDisplayed()));
        onView(withId(R.id.login_offline_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSeekBarMovement() {
        // Given
        int seekBarNewValue = 5000;
        String textAfterChange = application.getString(R.string.login_title_rounds, seekBarNewValue);

        // When
        onView(withId(R.id.login_rounds)).perform(setProgress(seekBarNewValue));

        // Then
        onView(withId(R.id.login_rounds_label)).check(matches(withSubstring(textAfterChange)));
    }

    @Test
    public void testOnlineLogin() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        int rounds = 5000;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        onView(withId(R.id.login_encryption)).perform(click());
        onView(withId(R.id.login_online_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnlineLoginNoPassword() {
        // Given
        String username = "testUser";
        int rounds = 5000;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        onView(withId(R.id.login_online_button)).perform(click());

        // Then
        String expectedResult = "Result: false";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnlineLoginNoUsername() {
        // Given
        String password = "testPassword";

        // When
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_online_button)).perform(click());

        // Then
        String expectedResult = "Result: false";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnlineLoginCriticalValueMax() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        int seekBarMaxValue = 100000;
        int rounds = Integer.MAX_VALUE;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        onView(withId(R.id.login_online_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withId(R.id.login_rounds_label)).check(matches(withSubstring(seekBarMaxValue + "")));
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnlineLoginCriticalValueMin() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        int seekBarMinValue = 0;
        int rounds = Integer.MIN_VALUE;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        onView(withId(R.id.login_online_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withId(R.id.login_rounds_label)).check(matches(withSubstring(seekBarMinValue + "")));
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnlineLoginRandom() {
        // Given
        int loops = 10;
        int randomStringMaxLength = 100;
        int randomIntMax = 10000;

        for(int i = 0; i < loops; i++) {
            // When
            onView(withId(R.id.login_username))
                    .perform(replaceText(TestUtils.randomString(randomStringMaxLength)));
            onView(withId(R.id.login_password))
                    .perform(replaceText(TestUtils.randomString(randomStringMaxLength)));
            onView(withId(R.id.login_rounds)).perform(setProgress(TestUtils.randomInt(randomIntMax)));
            onView(withId(R.id.login_online_button)).perform(click());

            // Then
            String expectedResult = "Result: true";
            onView(withText(expectedResult))
                    .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void testOffline() {
        // Given
        String username = DefaultLoginService.RIGHT_USERNAME;
        String password = DefaultLoginService.RIGHT_PASSWORD;
        int rounds = 5000;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_rounds)).perform(setProgress(rounds));
        onView(withId(R.id.login_encryption)).perform(click());
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: true";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOfflineWrongUsername() {
        // Given
        String username = "testUsername";
        String password = DefaultLoginService.RIGHT_PASSWORD;

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: false";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOfflineWrongPassword() {
        // Given
        String username = DefaultLoginService.RIGHT_USERNAME;
        String password = "testPassword";

        // When
        onView(withId(R.id.login_username)).perform(typeText(username));
        onView(withId(R.id.login_password)).perform(typeText(password));
        onView(withId(R.id.login_offline_button)).perform(click());

        // Then
        String expectedResult = "Result: false";
        onView(withText(expectedResult))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }
}