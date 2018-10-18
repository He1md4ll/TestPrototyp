package edu.hsb.proto.test;

import android.Manifest;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static org.hamcrest.Matchers.not;

@LargeTest
public class LocationInstTest extends BaseUITest {

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Override
    protected void inject(UITestComponent component) {}

    @Test
    public void testLocationMonitoringStart() {
        // Given
        String expectedResultSubstring = "My Location:";

        // When
        onView(withId(R.id.fab_monitoring)).perform(click());

        // Then
        onView(withId(R.id.fab_monitoring)).check(matches(new FABIconMatcher(R.drawable.baseline_stop_24)));
        onView(withSubstring(expectedResultSubstring))
                .inRoot(withDecorView(not(getMainActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLocationMonitoringStop() {
        // When
        onView(withId(R.id.fab_monitoring)).perform(click(), click());

        // Then
        onView(withId(R.id.fab_monitoring)).check(matches(new FABIconMatcher(R.drawable.baseline_play_arrow_24)));
    }

    private class FABIconMatcher extends TypeSafeMatcher<View> {

        @IdRes
        int resourceId;

        FABIconMatcher(int resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        protected boolean matchesSafely(View target) {
            return target instanceof FloatingActionButton && target.getTag().equals(resourceId);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with drawable from resource id: ");
            description.appendValue(resourceId);
        }
    }
}
