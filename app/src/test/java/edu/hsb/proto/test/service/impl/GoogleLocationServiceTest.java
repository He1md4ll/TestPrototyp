package edu.hsb.proto.test.service.impl;


import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Tasks;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;

import javax.inject.Inject;

import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.RealWithMocks;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class GoogleLocationServiceTest extends BaseUnitTest {

    @RealWithMocks
    @Inject
    ILocationService classUnderTest;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    SettingsClient settingsClient;

    private Location testLocation;

    @Before
    public void init() {
        testLocation = new Location("testLocation");
    }

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testStartSuccess() {
        // Given
        ILocationListener locationListener = new ILocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Then --> Asynchronous call of task from start method
                Truth.assertThat(location).isEqualTo(testLocation);
                Truth.assertThat(classUnderTest.getLastLocation()).isEqualTo(testLocation);
                verify(fusedLocationProviderClient, times(1)).getLastLocation();
            }
        };
        when(fusedLocationProviderClient.getLastLocation()).thenReturn(Tasks.forResult(testLocation));

        // When
        classUnderTest.start(locationListener);

        // Then
        verify(fusedLocationProviderClient, times(1)).getLastLocation();
        verify(fusedLocationProviderClient, times(1))
                .requestLocationUpdates(any(), any(LocationCallback.class), ArgumentMatchers.isNull());
    }

    @Test
    public void testStartSuccessNoLastLocation() {
        // Given
        ILocationListener locationListener = mock(ILocationListener.class);
        when(fusedLocationProviderClient.getLastLocation())
                .thenReturn(Tasks.forException(new IOException("Test Exception")));

        // When
        classUnderTest.start(locationListener);

        // Then
        Truth.assertThat(classUnderTest.getLastLocation()).isNull();
        verifyZeroInteractions(locationListener);
        verify(fusedLocationProviderClient, times(1)).getLastLocation();
        verify(fusedLocationProviderClient, times(1))
                .requestLocationUpdates(any(), any(LocationCallback.class), ArgumentMatchers.isNull());
    }

    @Test
    public void testStartNoPermission() {
        // Given
        ILocationListener locationListener = mock(ILocationListener.class);
        when(fusedLocationProviderClient.getLastLocation())
                .thenThrow(new SecurityException("Test Exception"));

        // When
        classUnderTest.start(locationListener);

        // Then
        Truth.assertThat(classUnderTest.getLastLocation()).isNull();
        verifyZeroInteractions(locationListener);
        verify(fusedLocationProviderClient, times(1)).getLastLocation();
        verify(fusedLocationProviderClient, never())
                .requestLocationUpdates(any(), any(LocationCallback.class), ArgumentMatchers.isNull());
    }

    @Test
    public void testStop() {
         // When
        classUnderTest.stop();

        // Then
        verify(fusedLocationProviderClient, times(1))
                .removeLocationUpdates(any(LocationCallback.class));
    }

    @Test
    public void testCheckSettings() {
        // When
        classUnderTest.checkSettings();

        // Then
        verify(settingsClient, times(1)).checkLocationSettings(any());
    }
}