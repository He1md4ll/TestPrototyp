package edu.hsb.proto.test.integration;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.impl.DefaultLoginService;

public class LoginIntegrationTest extends BaseUnitTest {

    @Real
    @Inject
    ILoginService loginService;

    @Real
    @Inject
    ConnectivityManager connectivityManager;

    private String username;
    private String password;
    private ShadowConnectivityManager shadowConnectivityManager;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        username = "testUsername";
        password = "testPassword";
        shadowConnectivityManager = Shadows.shadowOf(connectivityManager);
    }

    @Test
    public void testLoginOnlineConnected() {
        // When
        final boolean result = loginService.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isTrue();
    }

    @Test
    public void testLoginOnlineNotConnected() {
        // Given
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI, 0, true, NetworkInfo.State.DISCONNECTED);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        // When
        final boolean result = loginService.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void testLoginOnlineIsConnecting() {
        // Given
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI, 0, true, NetworkInfo.State.CONNECTING);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        // When
        final boolean result = loginService.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void testLoginOnlineNoActiveNetwork() {
        // Given
        shadowConnectivityManager.setActiveNetworkInfo(null);

        // When
        final boolean result = loginService.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void testLoginOffline() {
        // Given
        int rounds = 10000;
        boolean enc = Boolean.FALSE;

        // When
        final String encPassword = loginService.hash(DefaultLoginService.RIGHT_PASSWORD, rounds, enc);
        final boolean result = loginService.loginOffline(DefaultLoginService.RIGHT_USERNAME,
                encPassword, rounds, enc);

        // Then
        Truth.assertThat(result).isTrue();
    }
}