package edu.hsb.proto.test;

import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILocationService;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest extends BaseUnitTest {

    @Inject @Named("mock")
    ILocationService locationService;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}