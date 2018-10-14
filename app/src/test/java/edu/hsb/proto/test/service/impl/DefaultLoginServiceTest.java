package edu.hsb.proto.test.service.impl;


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
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DefaultLoginServiceTest extends BaseUnitTest {

    @RealWithMocks
    @Inject
    ILoginService classUnderTest;

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

    /*
        More of tests of login service can be found in SecurityTest and PerformanceTest
     */

    @Test
    public void testLoginOnlineSuccessful() throws IOException {
        // Given - Mock Retrofit connection
        ReqresTestApi reqresTestApi = Mockito.mock(ReqresTestApi.class);
        Call call = Mockito.mock(Call.class);
        Mockito.when(connectionManager.getReqresTestApi()).thenReturn(reqresTestApi);
        Mockito.when(reqresTestApi.login(Mockito.any())).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(Response.success("testResponse"));

        // When
        final boolean result = classUnderTest.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isTrue();
        Mockito.verify(connectionManager, Mockito.times(1)).getReqresTestApi();
    }

    @Test
    public void testLoginOnlineFailure() throws IOException {
        // Given - Mock Retrofit connection
        ReqresTestApi reqresTestApi = Mockito.mock(ReqresTestApi.class);
        Call call = Mockito.mock(Call.class);
        Mockito.when(connectionManager.getReqresTestApi()).thenReturn(reqresTestApi);
        Mockito.when(reqresTestApi.login(Mockito.any())).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(
                Response.error(500, ResponseBody.create(MediaType.parse(""), "")));

        // When
        final boolean result = classUnderTest.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isFalse();
        Mockito.verify(connectionManager, Mockito.times(1)).getReqresTestApi();
    }

    @Test
    public void testLoginOnlineException() throws IOException {
        // Given - Mock Retrofit connection
        Mockito.when(connectionManager.getReqresTestApi()).thenThrow(new IOException("Test Error"));

        // When
        final boolean result = classUnderTest.loginOnline(username, password);

        // Then
        Truth.assertThat(result).isFalse();
        Mockito.verify(connectionManager, Mockito.only()).getReqresTestApi();
    }

    @Test
    public void testLoginOnlineEmptyUsername() {
        // Given - Mock Retrofit connection
        String emptyUsername = "";

        // When
        final boolean result = classUnderTest.loginOnline(emptyUsername, password);

        // Then
        Truth.assertThat(result).isFalse();
        Mockito.verifyZeroInteractions(connectionManager);
    }

    @Test
    public void testLoginOnlineNullPassword() {
        // When
        final boolean result = classUnderTest.loginOnline(username, null);

        // Then
        Truth.assertThat(result).isFalse();
        Mockito.verifyZeroInteractions(connectionManager);
    }

    @Test
    public void testLoginOfflineNullOrEmpty() {
        // When
        boolean result = classUnderTest.loginOffline("", null, rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();

        // When
        result = classUnderTest.loginOffline(null, "", rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void testLoginOfflineWrongUsernameAndOrPassword() {
        // When
        boolean result = classUnderTest.loginOffline(username, password, rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();

        // When
        result = classUnderTest.loginOffline(DefaultLoginService.RIGHT_USERNAME, password, rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();

        // Given
        String rightPassword = classUnderTest.hash(DefaultLoginService.RIGHT_PASSWORD, rounds, encryption);

        // When
        result = classUnderTest.loginOffline(username, rightPassword, rounds, encryption);

        // Then
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void testLoginOfflineRight() {
        // Given
        String rightPassword = classUnderTest.hash(DefaultLoginService.RIGHT_PASSWORD, rounds, encryption);

        // When
        boolean result = classUnderTest.loginOffline(DefaultLoginService.RIGHT_USERNAME, rightPassword, rounds, encryption);

        // Then
        Truth.assertThat(result).isTrue();
    }

    @Test
    public void testHashEncryption() {
        // Given
        boolean enc = Boolean.TRUE;

        // When
        String hash = classUnderTest.hash(password, rounds, enc);

        // Then
        Truth.assertThat(hash).isNotEmpty();

        // Given
        enc = Boolean.FALSE;

        // When
        hash = classUnderTest.hash(password, rounds, enc);

        // Then
        Truth.assertThat(hash).isNotEmpty();
    }

    @Test
    public void testHashPasswordNullOrEmpty() {
        // When
        String hash = classUnderTest.hash(null, rounds, encryption);

        // Then
        Truth.assertThat(hash).isEmpty();

        // When
        hash = classUnderTest.hash("", rounds, encryption);

        // Then
        Truth.assertThat(hash).isEmpty();
    }
}