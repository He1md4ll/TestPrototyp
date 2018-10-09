package edu.hsb.proto.test.base;

import android.app.Application;

public class UnitTestApp extends Application {

    private UnitTestComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
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