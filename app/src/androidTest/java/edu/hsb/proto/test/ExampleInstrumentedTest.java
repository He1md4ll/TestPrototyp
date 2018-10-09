package edu.hsb.proto.test;

import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import edu.hsb.proto.test.base.BaseUITest;
import edu.hsb.proto.test.base.UITestComponent;
import edu.hsb.proto.test.service.ILocationService;

import static org.junit.Assert.assertEquals;

public class ExampleInstrumentedTest extends BaseUITest {

    @Inject @Named("mock")
    ILocationService locationService;

    @Override
    protected void inject(UITestComponent component) {
        component.inject(this);
    }

    @Test
    public void useAppContext() {
        assertEquals("edu.hsb.proto.test", getContext().getPackageName());
    }
}