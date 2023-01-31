package com.example.ocr;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class enterpriseRst3 extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private RegisterAPI registerAPI;

    private Button btn_register4;
    private EditText et_userid, et_userpw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        btn_register4 = findViewById(R.id.btn_register_next);
        et_userid = findViewById(R.id.userid);
        et_userpw = findViewById(R.id.userpw);





        et_userpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    btn_register4.setBackgroundResource(R.drawable.loginbackgrounddrawablebuttonemail);
                }else {
                    btn_register4.setBackgroundResource(R.drawable.loginbackgrounddrawablebutton);
                }
            }
        });



        btn_register4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = et_userid.getText().toString();
                String userpw = et_userpw.getText().toString();


                if (userid.trim().length() == 0 || userid == null || userpw.trim().length() == 0 || userpw == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(enterpriseRst3.this);
                    builder.setTitle("알림").setMessage("정보를 바르게 입력하시오").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    RegisterResponse();
                }

            }
        });
    }

    private void RegisterResponse() {
        Intent intent = getIntent();
        String userid = et_userid.getText().toString();
        String userpw = et_userpw.getText().toString();
        String usereamil = intent.getStringExtra("useremail");
        String userstudentid = intent.getStringExtra("userstudentid");
        String username = intent.getStringExtra("username");
        String userphonenum = intent.getStringExtra("userphonenum");
         Integer userlicense = 3;
         Integer depatment = 1;

        RegisterReq registerReq = new RegisterReq(userid, userpw, usereamil, userstudentid, username, userphonenum, userlicense, depatment);

        retrofitClient =  RetrofitClient.getInstance();
        registerAPI = RetrofitClient.getRegisterAPI();
        registerAPI.getRegisterRes(registerReq).enqueue(new Callback<RegisterRes>() {
            @Override
            public void onResponse(Call<RegisterRes> call, Response<RegisterRes> response) {

                if(response.isSuccessful() && response.body() != null){
                        Log.d("test11", String.valueOf(response));
                        Toast.makeText(enterpriseRst3.this, "통신성공!", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(enterpriseRst3.this, MainActivity.class);
                        startActivity(intent1);
                        finish();

                }else {
                    Toast.makeText(enterpriseRst3.this, "통신오류..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterRes> call, Throwable t) {
                Toast.makeText(enterpriseRst3.this, "통신실패..", Toast.LENGTH_LONG).show();
                Log.d("test33333", t.getMessage());
            }
        });
    }
}