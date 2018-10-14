package edu.hsb.proto.test.connection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.truth.Truth;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.RealWithMocks;
import edu.hsb.proto.test.base.UnitTestComponent;
import retrofit2.Retrofit;

public class ConnectionManagerTest extends BaseUnitTest {

    @RealWithMocks
    @Inject
    ConnectionManager classUnderTest;

    @Inject
    ConnectivityManager connectivityManager;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testGetRetrofitConnection() {
        // When
        final Retrofit result = classUnderTest.getRetrofitConnection();

        // Then
        Truth.assertThat(result).isNotNull();
        Truth.assertThat(result.baseUrl().toString()).isEqualTo(ConnectionManager.BASE);
    }

    @Test
    public void testGetReqresTestApiConnected() throws IOException {
        // Given
        final NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(Boolean.TRUE);

        // When
        final ReqresTestApi result = classUnderTest.getReqresTestApi();

        // Then
        Truth.assertThat(result).isNotNull();
        Truth.assertThat(result).isInstanceOf(ReqresTestApi.class);
    }

    @Test(expected = IOException.class)
    public void testGetReqresTestApiDisconnected() throws IOException {
        // Given
        final NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(Boolean.FALSE);

        // When
        classUnderTest.getReqresTestApi();

        // Then --> IOException
    }
}