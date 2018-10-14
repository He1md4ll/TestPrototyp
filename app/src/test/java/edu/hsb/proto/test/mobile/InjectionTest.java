package edu.hsb.proto.test.mobile;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Tasks;
import com.google.common.truth.Truth;

import org.junit.Test;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.RealWithMocks;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InjectionTest extends BaseUnitTest {

    @Inject
    ILocationService locationService;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;

    @RealWithMocks
    @Inject
    ILocationService locationServiceReal;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testInjection() {
        // When
        // --> Injection

        // Then
        Truth.assertThat(locationService).isNotNull();
        Truth.assertThat(locationServiceReal).isNotNull();
    }

    @Test
    public void testDeepMockInjection() {
        // Given
        Location testLocation = new Location("testLocation");
        ILocationListener locationListener = new ILocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Then --> Asynchronous call of task from start method
                Truth.assertThat(location).isEqualTo(testLocation);
                verify(fusedLocationProviderClient, times(1)).getLastLocation();
            }
        };
        when(fusedLocationProviderClient.getLastLocation()).thenReturn(Tasks.forResult(testLocation));

        // When
        locationServiceReal.start(locationListener);

        // Then
        verify(fusedLocationProviderClient, times(1)).requestLocationUpdates(any(), any(), any());
    }

    @Test
    public void testMocking() {
        // When
        // --> Injection

        // Then
        Truth.assertThat(mockingDetails(locationService).isMock()).isTrue();
        verifyZeroInteractions(locationService);
    }
}