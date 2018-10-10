package edu.hsb.proto.test.base;

import edu.hsb.proto.test.injection.PrototypeApp;

public class UITestApp extends PrototypeApp {

    private UITestComponent component;

    @Override
    public void onCreate() {
        component = buildComponent();
        component.inject(this);
        setupLeakCanary();
        enabledStrictMode();
    }

    protected UITestComponent buildComponent() {
        return DaggerUITestComponent.builder().application(this).build();
    }

    public UITestComponent getComponent() {
        return component;
    }
}