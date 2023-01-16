package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RenatalRes {

    @SerializedName("suc")
    public Object suc;

    @SerializedName("extension")
    public String extentsion;


    public String tosuc(){
        return "SUC=" + suc + " " + extentsion;
    }
}
