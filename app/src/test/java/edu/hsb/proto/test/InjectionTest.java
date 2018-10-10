package edu.hsb.proto.test;

import com.google.common.truth.Truth;

import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILocationService;

public class InjectionTest extends BaseUnitTest {

    @Inject
    ILocationService locationService;

    @Real
    @Inject
    ILocationService locationServiceReal;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testInjection() {
        Truth.assertThat(locationService).isNotNull();
        Truth.assertThat(locationServiceReal).isNotNull();
        Mockito.verifyZeroInteractions(locationService);
    }
}