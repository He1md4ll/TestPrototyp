package edu.hsb.proto.test;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.RealWithMocks;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.connection.ReqresTestApi;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.service.impl.DefaultLoginService;
import retrofit2.Call;
import retrofit2.Response;

public class SecurityTest extends BaseUnitTest {

    @RealWithMocks
    @Inject
    ILoginService loginService;

    @Inject
    Application context;

    @Inject
    ConnectionManager connectionManager;

    private String username;
    private String password;
    private int rounds;
    private boolean encryption;

    @Before
    public void init() {
        username = "testUser";
        password = "testPassword";
        rounds = 1000;
        encryption = Boolean.TRUE;
    }

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testSharedPreferencesLoginFailed() {
        // Given
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // When
        final boolean result = loginService.loginOffline(username, password, rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();
        Truth.assertThat(preferences.getAll()).isEmpty();

        Truth.assertThat(context.getCacheDir().exists()).isTrue();
        Truth.assertThat(context.getCacheDir().listFiles()).isEmpty();
    }

    @Test
    public void testSharedPreferencesLoginSuccessful() {
        // Given
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // When
        String hashedPassword = loginService.hash(DefaultLoginService.RIGHT_PASSWORD, rounds, encryption);
        boolean result = loginService.loginOffline(DefaultLoginService.RIGHT_USERNAME,
                hashedPassword, rounds, encryption);

        // Then
        Truth.assertThat(result).isTrue();
        Truth.assertThat(preferences.getAll()).isEmpty();

        Truth.assertThat(context.getCacheDir().exists()).isTrue();
        Truth.assertThat(context.getCacheDir().listFiles()).isEmpty();
    }

    @Test
    public void testSharedPreferencesLoginOnlineMock() throws IOException {
        // Given
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Mock Retrofit connection
        ReqresTestApi reqresTestApi = Mockito.mock(ReqresTestApi.class);
        Call call = Mockito.mock(Call.class);
        Mockito.when(connectionManager.getReqresTestApi()).thenReturn(reqresTestApi);
        Mockito.when(reqresTestApi.login(Mockito.any())).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(Response.success("testResponse"));

        // When
        final boolean result = loginService.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isTrue();
        Truth.assertThat(preferences.getAll()).isEmpty();

        Truth.assertThat(context.getCacheDir().exists()).isTrue();
        Truth.assertThat(context.getCacheDir().listFiles()).isEmpty();
    }

    @Test
    public void testPermissions() throws PackageManager.NameNotFoundException{
        // Given - List of all required permissions
        final String[] requiredPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,};

        // When
        PackageManager packageManager = context.getPackageManager();
        final String[] requestedPermissions = packageManager.getPackageInfo(context.getPackageName(),
                PackageManager.GET_PERMISSIONS).requestedPermissions;

        // Then
        Truth.assertThat(requestedPermissions).asList()
                .containsExactlyElementsIn(requiredPermissions);
    }
}