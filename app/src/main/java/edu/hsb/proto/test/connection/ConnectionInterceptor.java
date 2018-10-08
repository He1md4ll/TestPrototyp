package edu.hsb.proto.test.connection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionInterceptor implements Interceptor{

    private static final String TAG = ConnectionInterceptor.class.getSimpleName();
    private static final int MAX_REQUEST_COUNT = 3;

    private Context context;

    public ConnectionInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        int tryCount = 0;
        final Request request = chain.request();
        Response response = chain.proceed(request);

        while (!response.isSuccessful() &&
                response.code() >= HttpURLConnection.HTTP_INTERNAL_ERROR
                && tryCount++ < MAX_REQUEST_COUNT) {
            Log.i(TAG, "Request not successful - " + tryCount);
            Toast.makeText(context, "Request not successful", Toast.LENGTH_LONG).show();
            response = chain.proceed(request);
        }
        return response;
    }
}