package edu.hsb.proto.test.base;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import edu.hsb.proto.test.service.ILocationService;

import static org.mockito.Mockito.mock;

@Module
public class UITestModule {

    @Provides
    @Named("mock")
    protected ILocationService provideLocationService() {
        return mock(ILocationService.class);
    }
}