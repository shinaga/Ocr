package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RentalReq {

    @SerializedName("rental_id")
    public String rentalid;

    public RentalReq(String rentalid) {
        this.rentalid = rentalid;
    }
}
