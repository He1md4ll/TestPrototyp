package edu.hsb.proto.test.connection;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowToast;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectionInterceptorTest extends BaseUnitTest {

    @Real
    @Inject
    ConnectionInterceptor classUnderTest;

    private Request request;
    private Interceptor.Chain chain;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() {
        chain = Mockito.mock(Interceptor.Chain.class);
        request = new Request.Builder().url("https://test.org").build();
        when(chain.request()).thenReturn(request);
    }

    /*
        Rest of ConnectionInterceptor tests are located in ToughnessTest
     */
    @Test
    public void testInterceptorInternalError() throws IOException {
        // Given
        Response responseError = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(HttpURLConnection.HTTP_INTERNAL_ERROR)
                .message("Test Response")
                .build();

        Response responseSucc = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(HttpURLConnection.HTTP_ACCEPTED)
                .message("Test Response")
                .build();

        // First return error then success
        when(chain.proceed(request))
                .thenReturn(responseError)
                .thenReturn(responseSucc);

        // When
        final Response result = classUnderTest.intercept(chain);

        // Then
        Truth.assertThat(result).isEqualTo(responseSucc);
        verify(chain, times(1)).request();
        verify(chain, times(2)).proceed(request);
        Truth.assertThat(ShadowToast.getTextOfLatestToast().equals("Request not successful")).isTrue();
    }
}