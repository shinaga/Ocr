package com.example.ocr;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.POST;

public class LoginResponse {

    @SerializedName("suc")
    public boolean suc;

    @SerializedName("login")
    public Object login;

    @SerializedName("token")
    public Object token;

    public String toToken(){
        return "SUC" + " " + suc + "TOKEN=" + token;
    }

}