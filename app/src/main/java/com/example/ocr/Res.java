package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class Res {

    @SerializedName("suc")
    public boolean suc;

    public boolean getsuc(){
        return suc;
    }
    public void setSuc(boolean suc){
        this.suc = suc;
    }
}

