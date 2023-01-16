package com.example.ocr;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RentalAPI {

    @POST("rental/extension")
    Call<RenatalRes> getRental(@Body RentalReq rentalReq);



}
