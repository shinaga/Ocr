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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tv_register;
    static EditText userid, userpw;
    private Button login;
    private TextView idfind, pwfind;
    static String token;

    RetrofitClient retrofitClient;
    LoginAPI loginAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_register = findViewById(R.id.tv_loginpage_register);
        idfind = findViewById(R.id.tv_loginpage_findid);
        pwfind = findViewById(R.id.tv_loginpage_findpw);

        userid = findViewById(R.id.userid);
        userpw = findViewById(R.id.userpw);

        pwfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindPW.class);
                startActivity(intent);
            }
        });

        userpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    login.setBackgroundResource(R.drawable.loginbackgrounddrawablebuttonemail);
                }else {
                    login.setBackgroundResource(R.drawable.loginbackgrounddrawablebutton);
                }
            }
        });

        idfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IDfind.class);
                startActivity(intent);
            }
        });

        login = findViewById(R.id.btn_loginpage_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Userid = userid.getText().toString();
                String Userpw = userpw.getText().toString();

                LoginDTO loginDTO = new LoginDTO(Userid,Userpw);
                retrofitClient = RetrofitClient.getInstance();
                loginAPI = RetrofitClient.getLoginAPI();
                if(Userid.trim().length() == 0 || Userid == null || Userpw.trim().length() ==0 || Userpw == null){
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 한번 확인해주세요!", Toast.LENGTH_LONG).show();

                }else {
                    loginAPI.getLogin(loginDTO).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if(response.isSuccessful()){
                                String result = response.body().toToken();
                                token=result.substring(13,result.length()-1);

                                Log.e("test32",  token);
                                if(token.length()>5){
                                    token=result.substring(21,result.length()-1);
                                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }else
                                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(), "통신오류", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d("test31fa", t.getMessage());
                            Toast.makeText(getApplicationContext(), "통신실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });



        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }
}