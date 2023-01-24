package com.example.ocr;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterAPI {

    @POST("user/signUp")
    Call<RegisterRes> getRegisterRes(@Body RegisterReq registerReq);

}
