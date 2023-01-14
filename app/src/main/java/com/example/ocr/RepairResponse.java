package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RepairResponse {


    @SerializedName("suc")
    public boolean suc;


    public String  getsuc(){
        return "suc=" + " " + suc;
    }
}
