package edu.hsb.proto.test.integration;

import android.location.Location;
import android.os.RemoteException;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.shadows.ShadowServiceManager;

import javax.inject.Inject;

import edu.hsb.proto.test.LocationAndroidService;
import edu.hsb.proto.test.PreferenceManager;
import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.domain.LocationAccuracy;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

public class LocationIntegrationTest extends BaseUnitTest {

    @Real
    @Inject
    ILocationService locationService;

    @Real
    @Inject
    PreferenceManager preferenceManager;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    SettingsClient settingsClient;

    private ServiceController<LocationAndroidService> serviceController;
    private LocationAndroidService service;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        serviceController = Robolectric.buildService(LocationAndroidService.class).create();
        service = serviceController.create().bind().get();
        service.setLocationServices(locationService);
    }

    @Test
    public void testLocationMonitoring() {
        // Given
        Location testLocation =  new Location("testLocation");
        Mockito.when(fusedLocationProviderClient.getLastLocation()).thenReturn(Tasks.forResult(testLocation));
        Mockito.when(settingsClient.checkLocationSettings(Mockito.any())).thenReturn(Tasks.forResult(null));

        ILocationListener locationListener = location -> {
            // Then --> listener
            Truth.assertThat(location).isEqualTo(testLocation);
            Mockito.verify(fusedLocationProviderClient).getLastLocation();
            Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(Mockito.any(), Mockito.any(), Mockito.any());
        };

        // When
        final Task<LocationSettingsResponse> result = service.startMonitoring(locationListener);

        // Then --> async task
        result.addOnCompleteListener(task -> {
            Truth.assertThat(task.isSuccessful()).isTrue();
            Truth.assertThat(service.isMonitoring()).isTrue();
        });
    }

    @Test
    public void testLocationAccuracyPreference() {
        // Given
        preferenceManager.setLocationAccuracy(LocationAccuracy.LOW);
        Location testLocation =  new Location("testLocation");
        Mockito.when(fusedLocationProviderClient.getLastLocation()).thenReturn(Tasks.forResult(testLocation));
        Mockito.when(settingsClient.checkLocationSettings(Mockito.any())).thenReturn(Tasks.forResult(null));

        ILocationListener locationListener = location -> {
            // Then --> listener
            Truth.assertThat(location).isEqualTo(testLocation);
            Mockito.verify(fusedLocationProviderClient)
                    .requestLocationUpdates(ArgumentMatchers.eq(
                            LocationAccuracy.LOW.getLocationRequest()),
                            Mockito.any(), Mockito.any());
        };

        // When
        service.startMonitoring(locationListener);
    }

    @Test
    public void testLocationAlreadyMonitoring() {
        // Given
        Location testLocation =  new Location("testLocation");
        ILocationListener locationListener = location -> {};
        Mockito.when(fusedLocationProviderClient.getLastLocation()).thenReturn(Tasks.forResult(testLocation));
        Mockito.when(settingsClient.checkLocationSettings(Mockito.any())).thenReturn(Tasks.forResult(null));

        // When
        final Task<LocationSettingsResponse> result = service.startMonitoring(locationListener);
        result.addOnCompleteListener(task -> {
            final Task<LocationSettingsResponse> result2 = service.startMonitoring(locationListener);

            // Then
            Truth.assertThat(service.isMonitoring()).isTrue();
            Truth.assertThat(result2).isNull();
        });
    }

    @Test
    public void testLocationWrongSettings() {
        // Given
        ILocationListener locationListener = location -> {};
        Mockito.when(settingsClient.checkLocationSettings(Mockito.any()))
                .thenReturn(Tasks.forException(new RuntimeException("Wrong Settings Test")));

        // When
        final Task<LocationSettingsResponse> result = service.startMonitoring(locationListener);
        result.addOnCompleteListener(task -> {

            // Then
            Truth.assertThat(task.isSuccessful()).isFalse();
            Truth.assertThat(service.isMonitoring()).isFalse();
            Mockito.verify(fusedLocationProviderClient, Mockito.never()).getLastLocation();
            Mockito.verify(fusedLocationProviderClient, Mockito.never())
                    .requestLocationUpdates(Mockito.any(), Mockito.any(), Mockito.any());
        });
    }

    @Test
    public void testLocationStopMonitoring() {
        // When
        service.stopMonitoring();

        // Then
        Truth.assertThat(service.isMonitoring()).isFalse();
        Mockito.verify(fusedLocationProviderClient)
                .removeLocationUpdates(Mockito.any(LocationCallback.class));
    }

    @Test
    public void testUnbind() throws RemoteException {
        // When
        serviceController.unbind();

        // Then
        Truth.assertThat(ShadowServiceManager.listServices()).isNull();
    }
}