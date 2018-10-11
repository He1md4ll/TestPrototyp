package edu.hsb.proto.test.base;

import edu.hsb.proto.test.injection.PrototypeApp;

public class UnitTestApp extends PrototypeApp {

    private UnitTestComponent component;

    @Override
    public void onCreate() {
        component = buildComponent();
        component.inject(this);
    }

    protected UnitTestComponent buildComponent() {
        return DaggerUnitTestComponent.builder().application(this).build();
    }

    public UnitTestComponent getComponent() {
        return component;
    }
}