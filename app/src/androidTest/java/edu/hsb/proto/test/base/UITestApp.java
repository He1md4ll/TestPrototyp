package edu.hsb.proto.test.base;

import android.app.Application;

public class UITestApp extends Application {

    private UITestComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        component.inject(this);
    }

    protected UITestComponent buildComponent() {
        return DaggerUITestComponent.builder().application(this).build();
    }

    public UITestComponent getComponent() {
        return component;
    }
}