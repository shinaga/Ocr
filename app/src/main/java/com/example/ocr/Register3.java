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

public class Register3 extends AppCompatActivity {


    private Button btn_register3 ;
    private EditText et_studentid, et_phone, et_useremail;
    private EditText textView;
    private Button btn_emailCertified;

    String baseUrl = "http://120.142.105.189:5080/";
    private com.example.ocr.UserEmailAPI userEmailAPI;

    private  com.example.ocr.RetrofitClient retrofitClient;
    private EmailDTO emailDTO;
    private UserEmailCerti userEmailCerti;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        btn_register3 = findViewById(R.id.btn_register_next);
        et_phone = findViewById(R.id.userphonenum);
        et_studentid = findViewById(R.id.userstudentid);
        et_useremail = findViewById(R.id.useremail);
        btn_emailCertified = findViewById(R.id.emailcertifiedbtn);
       textView =findViewById(R.id.emailCertified);

        Intent intent = getIntent();




        et_studentid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_register3.setBackgroundColor(Color.parseColor("#9785CB"));
            }



        });


        btn_emailCertified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("bibibic", v.toString());
                String useremail = et_useremail.getText().toString();

//            emailDTO  = new EmailDTO(useremail);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                userEmailAPI = retrofit.create( com.example.ocr.UserEmailAPI.class);
                Call<ResponseBody> call = userEmailAPI.getEmail(useremail);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.isSuccessful()){
                                Log.e("popopo", response.body().string());
                                String result = response.body().string();
                                Toast.makeText(getApplicationContext(), "통신성공", Toast.LENGTH_SHORT).show();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("papapa", t.getMessage());
                        Toast.makeText(getApplicationContext(), "통신실패", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        btn_register3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userstudentid = et_studentid.getText().toString();
                String userphonenum = et_phone.getText().toString();
                String usereamil = et_useremail.getText().toString();
                String username = intent.getStringExtra("username");
                String userset = textView.getText().toString();

                Log.d("test321", String.valueOf(v));

                if(userstudentid.trim().length() == 0 || userstudentid == null || userphonenum.trim().length() == 0 || userphonenum == null || usereamil.trim().length() == 0 || usereamil ==null
                || userset.trim().length() ==0 || userset == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register3.this);
                    builder.setTitle("알림").setMessage("정보를 바르게 입력하시오").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{

                    Intent intent = new Intent(Register3.this, com.example.ocr.Register4.class);
                    intent.putExtra("userstudentid", userstudentid);
                    intent.putExtra("userphonenum", userphonenum);
                    intent.putExtra("useremail", usereamil);
                    intent.putExtra("username", username);

                    startActivity(intent);
                }


            }
        });
    }
}