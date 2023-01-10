package com.example.ocr;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

  private static RetrofitClient instance = null;
  private static  RegisterAPI registerAPI;
  private static UserEmailCerti userEmailCerti;
  private static com.example.ocr.LoginAPI loginAPI;
  private static String baseUrl = "http://120.142.105.189:5080/";


  private RetrofitClient (){
      Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
      registerAPI = retrofit.create(RegisterAPI.class);
      userEmailCerti = retrofit.create(UserEmailCerti.class);
      loginAPI = retrofit.create(com.example.ocr.LoginAPI.class );

  }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static RegisterAPI getRegisterAPI(){
      return registerAPI;
    }
    public static UserEmailCerti getUserEmailCerti(){
      return userEmailCerti;
    }
    public static com.example.ocr.LoginAPI getLoginAPI(){
      return loginAPI;

    }
}
