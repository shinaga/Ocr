package com.example.ocr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserEmailCerti {

    @POST("authEmail")
    Call<ResponseBody> getEmail(@Body  EmailDTO emailDTO);
}
