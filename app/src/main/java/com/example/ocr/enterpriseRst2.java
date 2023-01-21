package com.example.ocr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr.EmailDTO;
import com.example.ocr.R;
import com.example.ocr.Register2;
import com.example.ocr.Register3;
import com.example.ocr.Register4;
import com.example.ocr.RetrofitClient;
import com.example.ocr.UserEmailAPI;
import com.example.ocr.UserEmailCerti;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class enterpriseRst2 extends AppCompatActivity {

    private Button btn_register3 ;
    private EditText et_studentid, et_phone, et_useremail;
    private EditText textView;
    private Button btn_emailCertified;
    private String baseUrl = "http://120.142.105.189:5080/";

   private UserEmailAPI userEmailAPI;

    Spinner emailspiner;
    String itemresult;
    String beng = "@";
    String[] emailreult = {"naver.com", "daum.net", "gmail.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_rst3);

        btn_register3 = findViewById(R.id.btn_register_next);
        et_phone = findViewById(R.id.userphonenum);
        et_studentid = findViewById(R.id.userstudentid);
        et_useremail = findViewById(R.id.useremail);
        btn_emailCertified = findViewById(R.id.emailcertifiedbtn);
        textView =findViewById(R.id.emailCertified);
        emailspiner = findViewById(R.id.emailsub1);

        Intent intent = getIntent();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emailreult);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emailspiner.setAdapter(adapter);
        emailspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemresult = emailreult[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    btn_register3.setBackgroundResource(R.drawable.loginbackgrounddrawablebuttonemail);
                }else {
                    btn_register3.setBackgroundResource(R.drawable.loginbackgrounddrawablebutton);
                }
            }



        });


        btn_emailCertified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                Log.e("bibibic", v.toString());
                String useremail = et_useremail.getText().toString() + beng + itemresult;

//            emailDTO  = new EmailDTO(useremail);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                userEmailAPI = retrofit.create(UserEmailAPI.class);
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
                String usereamil = et_useremail.getText().toString() + beng + itemresult;
                String username = intent.getStringExtra("username");

                Log.d("test321", String.valueOf(v));

                if(userstudentid.trim().length() == 0 || userstudentid == null || userphonenum.trim().length() == 0 || userphonenum == null || usereamil.trim().length() == 0 || usereamil ==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(enterpriseRst2.this);
                    builder.setTitle("알림").setMessage("정보를 바르게 입력하시오").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{

                    Intent intent = new Intent(enterpriseRst2.this, enterpriseRst3.class);
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