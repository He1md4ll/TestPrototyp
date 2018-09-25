package edu.hsb.proto.test.injection;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import edu.hsb.proto.test.LocationAndroidService;
import edu.hsb.proto.test.LoginFragment;
import edu.hsb.proto.test.MainActivity;
import edu.hsb.proto.test.MapFragment;

@Module
public abstract class ComponentBuilder {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract MapFragment bindMapFragment();

    @ContributesAndroidInjector
    abstract LoginFragment bindLoginFragment();

    @ContributesAndroidInjector
    abstract LocationAndroidService bindLocationAndroidService();
}