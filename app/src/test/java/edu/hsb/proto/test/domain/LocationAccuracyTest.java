package edu.hsb.proto.test.domain;

import com.google.android.gms.location.LocationRequest;
import com.google.common.truth.Truth;

import org.junit.Test;

public class LocationAccuracyTest {

    @Test
    public void getLocationRequest() {
        // When
        final LocationRequest result = LocationAccuracy.HIGH.getLocationRequest();

        // Then
        Truth.assertThat(result).isNotNull();
        Truth.assertThat(result.getInterval()).isEqualTo(LocationAccuracy.HIGH.interval);
        Truth.assertThat(result.getFastestInterval()).isEqualTo(LocationAccuracy.HIGH.passiveInterval);
        Truth.assertThat(result.getPriority()).isEqualTo(LocationAccuracy.HIGH.priority);
        Truth.assertThat(result.getSmallestDisplacement())
                .isEqualTo(Float.valueOf(LocationAccuracy.HIGH.smallestDisplacement));
    }
}