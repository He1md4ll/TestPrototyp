package edu.hsb.proto.test.connection;

import android.net.ConnectivityManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionManager {

    private static final String BASE = "https://reqres.in/";

    private Retrofit retrofit;
    private Interceptor interceptor;
    private ConnectivityManager connectivityManager;

    public ConnectionManager(Interceptor interceptor, ConnectivityManager connectivityManager) {
        this.interceptor = interceptor;
        this.connectivityManager = connectivityManager;
    }

    public Retrofit getRetrofitConnection() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE)
                    .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public ReqresTestApi getReqresTestApi() throws IOException {
        if (isNetworkAvailable()) {
            return getRetrofitConnection().create(ReqresTestApi.class);
        } else {
            throw new IOException("No network connection present!");
        }
    }

    private boolean isNetworkAvailable() {
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }
}