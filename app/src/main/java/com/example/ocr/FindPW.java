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

public class FindPW extends AppCompatActivity {

    private EditText userEmail, Emailnum;
    private Button findbtn;

    private com.example.ocr.UserEmailAPI userEmailAPI;
    private Button btn_emailCertified;

    private String baseUrl = "http://120.142.105.189:5080/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        userEmail = findViewById(R.id.useremail);
        Emailnum = findViewById(R.id.emailnum);


        findbtn = findViewById(R.id.btn_idfind);

        btn_emailCertified = findViewById(R.id.emailcertifiedbtn);

        Emailnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                findbtn.setBackgroundColor(Color.parseColor("#9785CB"));
            }
        });

        btn_emailCertified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Log.e("bibibic", v.toString());
                String useremai = userEmail.getText().toString();

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

        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = userEmail.getText().toString();
                String emailnum = Emailnum.getText().toString();

                if(useremail.trim().length() == 0 || useremail == null || emailnum.trim().length() == 0 || emailnum == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPW.this);
                    builder.setTitle("알림").setMessage("정보 입력란을 다시 확인해주세요").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    Intent intent = new Intent(FindPW.this, com.example.ocr.FindPWnext.class);
                    intent.putExtra("useremail", useremail);
                    startActivity(intent);
                }



            }
        });

    }
}