package com.example.ocr;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RepairAPI {

    @POST("repair/requestRepair")
    Call<RepairRep> getResponse(@Body RepairReq repairReq);


}
