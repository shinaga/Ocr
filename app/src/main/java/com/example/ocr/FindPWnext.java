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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindPWnext extends AppCompatActivity {


    private TextView userEmail;
    private EditText newpw;
    private Button Mainbtn;

    private String baseUrl = "http://120.142.105.189:5080/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwnext);

        Intent intent = getIntent();

        userEmail = findViewById(R.id.useremail);
        userEmail.setText(intent.getStringExtra("useremail"));
        newpw = findViewById(R.id.newpw);

        Mainbtn = findViewById(R.id.btnmainpage);

        newpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Mainbtn.setBackgroundColor(Color.parseColor("#9785CB"));
            }
        });

        Mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = userEmail.getText().toString();
                String usernewpw = newpw.getText().toString();


                if(useremail.trim().length() == 0 || useremail == null || usernewpw.trim().length() == 0 || usernewpw == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPWnext.this);
                    builder.setTitle("알림").setMessage("정보 입력란을 다시 확인해주세요").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {

                    findpwDTO findpwDTO = new findpwDTO(useremail, usernewpw);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                    findpwAPI API = retrofit.create(findpwAPI.class);
                    Call<ResponseBody> call = API.getFINDpw(findpwDTO);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if(response.isSuccessful()){
                                       String result = response.body().string();
                                    Log.d("findpwsuc", response.body().string());
                                    Toast.makeText(getApplicationContext(), "통신성공", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(FindPWnext.this, MainActivity.class);
                                    startActivity(intent1);
                                    FindPWnext.this.finish();


                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("findpwfail", t.getMessage());
                            Toast.makeText(getApplicationContext(), "통신실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



    }
}