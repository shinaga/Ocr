package com.example.ocr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAPI {

    @POST("user/login")
    Call<LoginResponse> getLogin(@Body LoginDTO loginDTO);

}
