package edu.hsb.proto.test.integration;

import android.Manifest;
import android.app.Application;
import android.support.v4.app.FragmentManager;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import javax.inject.Inject;

import edu.hsb.proto.test.MainActivity;
import edu.hsb.proto.test.MapFragment;
import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.UnitTestComponent;

public class PermissionIntegrationTest extends BaseUnitTest {

    @Inject
    Application application;

    private ActivityController<MainActivity> activityController;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        activityController = Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void testPermissionGranted() {
        // Given
        final ShadowApplication shadowApplication = Shadows.shadowOf(application);
        shadowApplication.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        // When
        activityController.create();

        // Then
        final FragmentManager fragmentManager = activityController.get().getSupportFragmentManager();
        Truth.assertThat(fragmentManager.getFragments()).isNotEmpty();
        Truth.assertThat(fragmentManager.getFragments().get(0)).isInstanceOf(MapFragment.class);
    }

    @Test
    public void testPermissionDenied() {
        // Given
        final ShadowApplication shadowApplication = Shadows.shadowOf(application);
        shadowApplication.denyPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        // When
        activityController.create();

        // Then
        final FragmentManager fragmentManager = activityController.get().getSupportFragmentManager();
        Truth.assertThat(fragmentManager.getFragments()).isEmpty();
        Truth.assertThat(activityController.get().isDestroyed()).isFalse();
    }
}