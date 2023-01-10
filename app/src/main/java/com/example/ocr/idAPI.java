package com.example.ocr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface idAPI {

    @GET("user/findId")
    Call<ResponseBody> getfindid(@Query("user_email") String user_email);
}
