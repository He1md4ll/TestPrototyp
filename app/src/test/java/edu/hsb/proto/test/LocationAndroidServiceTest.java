package edu.hsb.proto.test;

import android.os.RemoteException;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.shadows.ShadowServiceManager;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILocationListener;
import edu.hsb.proto.test.service.ILocationService;

public class LocationAndroidServiceTest extends BaseUnitTest {

    @Inject
    ILocationService locationService;

    // No injection for android interfaces --> Robolectric
    private LocationAndroidService classUnderTest;
    private ServiceController<LocationAndroidService> serviceController;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        serviceController = Robolectric.buildService(LocationAndroidService.class);
        classUnderTest = serviceController.create().bind().get();
    }

    @Test
    public void testStartMonitoringSuccess() {
        // Given
        ILocationListener locationListener = Mockito.mock(ILocationListener.class);
        Mockito.when(locationService.checkSettings()).thenReturn(Tasks.forResult(null));

        // When
        final Task<LocationSettingsResponse> result = classUnderTest.startMonitoring(locationListener);

        // Then
        result.addOnCompleteListener(task -> {
            Truth.assertThat(result.isSuccessful()).isTrue();
            Truth.assertThat(classUnderTest.isMonitoring()).isTrue();
            Mockito.verify(locationService).start(locationListener);
            Mockito.verify(locationService).checkSettings();
        });
    }

    @Test
    public void testStartMonitoringError() {
        // Given
        ILocationListener locationListener = Mockito.mock(ILocationListener.class);
        Mockito.when(locationService.checkSettings())
                .thenReturn(Tasks.forException(new RuntimeException("Test Exception")));

        // When
        final Task<LocationSettingsResponse> result = classUnderTest.startMonitoring(locationListener);

        // Then
        result.addOnCompleteListener(task -> {
            Truth.assertThat(result.isSuccessful()).isFalse();
            Truth.assertThat(classUnderTest.isMonitoring()).isFalse();
            Mockito.verify(locationService, Mockito.never()).start(locationListener);
            Mockito.verify(locationService).checkSettings();
        });
    }

    @Test
    public void testStopMonitoring() {
        // When
        classUnderTest.stopMonitoring();

        // Then
        Truth.assertThat(classUnderTest.isMonitoring()).isFalse();
        Mockito.verify(locationService).stop();
    }

    @Test
    public void testUnbind() throws RemoteException {
        // When
        serviceController.unbind();

        // Then
        Truth.assertThat(ShadowServiceManager.listServices()).isNull();
    }
}