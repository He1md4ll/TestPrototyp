package edu.hsb.proto.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;
import edu.hsb.proto.test.connection.ConnectionInterceptor;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class ToughnessTest extends BaseUnitTest {

    @Real
    @Inject
    ConnectionInterceptor connectionInterceptor;

    private Request request;
    private Response response;
    private Interceptor.Chain chain;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Before
    public void init() throws IOException {
        chain = Mockito.mock(Interceptor.Chain.class);
        request = new Request.Builder().url("https://test.org").build();
        Mockito.when(chain.request()).thenReturn(request);
    }

    @Test
    public void testInterceptorInternalError() throws IOException {
        // Given
        int httpStatusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(httpStatusCode)
                .message("Test Response")
                .build();
        Mockito.when(chain.proceed(request)).thenReturn(response);

        // When
        connectionInterceptor.intercept(chain);

        // Then
        Mockito.verify(chain, Mockito.times(4)).proceed(request);
    }

    @Test
    public void testInterceptorClientError() throws IOException {
        // Given
        int httpStatusCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
        response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(httpStatusCode)
                .message("Test Response")
                .build();
        Mockito.when(chain.proceed(request)).thenReturn(response);

        // When
        connectionInterceptor.intercept(chain);

        // Then
        Mockito.verify(chain, Mockito.times(1)).proceed(request);
    }
}