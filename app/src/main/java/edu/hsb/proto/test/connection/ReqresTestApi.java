package edu.hsb.proto.test.connection;

import edu.hsb.proto.test.domain.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReqresTestApi {

    @POST("api/login")
    Call<ResponseBody> login(@Body User user);
}