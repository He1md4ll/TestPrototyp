package edu.hsb.proto.test;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.RealWithMocks;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILoginService;

public class PerformanceTest extends BaseUnitTest {

    private static final Long PERFORMANCE_CAP = 1000L; //ms

    @RealWithMocks
    @Inject
    ILoginService loginService;

    private String stringToHash;
    private boolean encryption;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        stringToHash = "testString";
        encryption = Boolean.TRUE;
    }

    @Test
    public void testHashing1000Rounds() {
        // Given
        long startTime = System.currentTimeMillis();
        int hashRounds = 1000;

        // When
        final String result = loginService.hash(stringToHash, hashRounds, encryption);

        // Then
        Truth.assertThat(result).isNotEmpty();
        Truth.assertThat(System.currentTimeMillis() - startTime)
                .named("Performance 1000 Rounds")
                .isAtMost(PERFORMANCE_CAP);
    }

    @Test
    public void testHashing50000Rounds() {
        // Given
        long startTime = System.currentTimeMillis();
        int hashRounds = 50000;

        // When
        final String result = loginService.hash(stringToHash, hashRounds, encryption);

        // Then
        Truth.assertThat(result).isNotEmpty();
        Truth.assertThat(System.currentTimeMillis() - startTime)
                .named("Performance 50000 Rounds")
                .isAtMost(PERFORMANCE_CAP);
    }

    @Test(expected = AssertionError.class)
    public void testHashing500000Rounds() {
        // Given
        long startTime = System.currentTimeMillis();
        int hashRounds = 500000;

        // When
        final String result = loginService.hash(stringToHash, hashRounds, encryption);

        // Then
        Truth.assertThat(result).isNotEmpty();
        Truth.assertThat(System.currentTimeMillis() - startTime)
                .named("Performance 500000 Rounds")
                .isAtMost(PERFORMANCE_CAP);
    }
}