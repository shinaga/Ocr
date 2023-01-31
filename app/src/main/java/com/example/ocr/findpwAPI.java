package com.example.ocr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface findpwAPI {

    @POST("user/changePw")
    Call<ResponseBody> getFINDpw(@Body findpwDTO findpwDTO);

}
