package com.example.ocr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IDfind extends AppCompatActivity {

    private EditText useremail, emailnum;

    private Button nextpage;

    private String baseUrl = "http://120.142.105.189:5080/";
    private com.example.ocr.UserEmailAPI userEmailAPI;
    private Button btn_emailCertified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idfind);

        useremail = findViewById(R.id.useremail);
        emailnum = findViewById(R.id.emailnum);

        nextpage = findViewById(R.id.btn_idfind);
        btn_emailCertified = findViewById(R.id.emailcertifiedbtn);

        emailnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextpage.setBackgroundColor(Color.parseColor("#9785CB"));
            }
        });


        btn_emailCertified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Log.e("bibibic", v.toString());
                String useremai = useremail.getText().toString();

//            emailDTO  = new EmailDTO(useremail);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                userEmailAPI = retrofit.create(com.example.ocr.UserEmailAPI.class);
                Call<ResponseBody> call = userEmailAPI.getEmail(useremai);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.isSuccessful()){
                                Log.e("popopo", response.body().string());
                                String result = response.body().string();
                                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("papapa", t.getMessage());
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail, emailNum;
                userEmail = useremail.getText().toString();
                emailNum = emailnum.getText().toString();

                if(userEmail.trim().length() == 0 || userEmail == null || emailNum.trim().length() == 0 || emailNum == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(IDfind.this);
                    builder.setTitle("??????").setMessage("?????? ???????????? ?????? ??????????????????").setPositiveButton("??????", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Suc();
                }

            }
        });
    }

    private void Suc(){


        String getemail = useremail.getText().toString();
        String getemailnum = emailnum.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        idAPI API = retrofit.create(idAPI.class);
        Call<ResponseBody> call = API.getfindid(getemail);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try {
                   if(response.isSuccessful()){
                       String result = response.body().string();
                       Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(IDfind.this, PWfind.class);
                       intent.putExtra("id", result);
                       startActivity(intent);

                       Log.d("idfind", response.body().string());
                   }
               }catch (IOException e){
                   e.printStackTrace();
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("idfindfailure", t.getMessage());
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
            }
        });

    }
}