package com.example.ocr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserEmailAPI {

    @GET("email")
    Call<ResponseBody> getEmail(@Query("user_email") String user_email);
}
