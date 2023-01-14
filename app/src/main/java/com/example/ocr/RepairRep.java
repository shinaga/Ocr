package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RepairRep {

    @SerializedName("suc")
    public Object suc;

    @SerializedName("result")
    public Object result;

    public String toRes(){
        return "SUC=" + " " + suc + "Result=" + result;
    }
}


