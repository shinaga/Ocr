package com.example.ocr;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Inter {

    @POST("test/rentalTool")
    Call<Res> getRes(@Body Req req);

}
