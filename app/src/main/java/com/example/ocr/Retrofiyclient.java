package com.example.ocr;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofiyclient {

    private static Retrofiyclient instance = null;
    private static Inter inter;
    private static String baseUrl = "http://120.142.105.189:5080/";

    private Retrofiyclient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        inter = retrofit.create(Inter.class);
    }
    public static Retrofiyclient getInstance(){
        if(instance == null){
            instance = new Retrofiyclient();
        }
        return instance;
    }

    public static Inter getInter(){
        return inter;
    }


}
