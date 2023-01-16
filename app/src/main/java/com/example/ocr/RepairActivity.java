package com.example.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepairActivity extends AppCompatActivity {
    static String tool_id;
    TextView text_name,text_posible,text_code,text_number,text_purchase,text_date,text_standard;
    Button btn_repair;
    ImageView img;
    EditText edit_tendinous;
    String baseUrl = "http://120.142.105.189:5080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        img = findViewById(R.id.img);
        textSet();

        btn_repair = findViewById(R.id.btn_repair);
        edit_tendinous = findViewById(R.id.edit_tendinous);
        loadEquipment(tool_id);
    }
    private void textSet() {
        text_name = findViewById(R.id.text_name);
        text_posible = findViewById(R.id.text_posible);
        text_code = findViewById(R.id.text_code);
        text_number = findViewById(R.id.text_number);
        text_purchase = findViewById(R.id.text_purchase);
        text_date = findViewById(R.id.text_date);
        text_standard = findViewById(R.id.text_standard);
    }
    void loadEquipment(String tool_id) {
        new Thread(){
            @Override
            public void run() {
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임
                    URL url = new URL("http://120.142.105.189:5080/tool/viewTool?tool_id="+tool_id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestMethod("GET");// 통신방식
                    connection.setRequestProperty("token", MainActivity.token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    }
                    JSONObject obj = new JSONObject(response.toString());// jsonData를 먼저 JSONObject 형태로 바꾼다.
                    boolean suc = obj.getBoolean("suc");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(suc != true) Toast.makeText(getApplicationContext(),"대여할 수 없는 기자재 입니다.",Toast.LENGTH_SHORT).show();
                            else{
                                try {
                                    text_name.setText(tool_id);
                                    JSONObject tool = obj.getJSONObject("tool");// jsonData를 먼저 JSONObject 형태로 바꾼다.
                                    JSONObject result = tool.getJSONObject("result");// jsonData를 먼저 JSONObject 형태로 바꾼다.

                                    String tool_name = result.getString("tool_name");
                                    text_name.setText(tool_name);
                                    String tool_state = result.getString("tool_state");
                                    text_posible.setText(tool_state);
                                    String tool_code = result.getString("tool_code");
                                    text_code.setText(tool_code);
                                    String tool_id = result.getString("tool_id");
                                    text_number.setText(tool_id);
                                    String tool_purchase_division = result.getString("tool_purchase_division");
                                    text_purchase.setText(tool_purchase_division);
                                    String tool_purchase_date = result.getString("tool_purchase_date");
                                    tool_purchase_date = tool_purchase_date.substring(0,10);//문자열 자르기
                                    text_date.setText(tool_purchase_date);
                                    String tool_standard = result.getString("tool_standard");
                                    text_standard.setText(tool_standard);

                                    JSONObject image = tool.getJSONObject("image");// 이미지를 가져오기 위해
                                    if(image!=null){//image가 없으면 false임
                                        String img_url = "http://120.142.105.189:5080/tool/"+image.getString("img_url");
                                        Glide.with(getApplicationContext()).load(img_url).into(img);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                btn_repair.setOnClickListener(v -> {
                                    String data = edit_tendinous.getText().toString();
                                    if(data == null || data.trim().length() == 0){//아무것도 입력하지 않았을때
                                        Toast.makeText(getApplicationContext(), "건의 내용을 입력해 주세요.", Toast.LENGTH_LONG).show();
                                    }else{
                                        tendinous();
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void tendinous(){

        String code = tool_id;
        String userID = MainActivity.userid.getText().toString();
        String data = edit_tendinous.getText().toString();

        RepairReq repairReq = new RepairReq(data, code, userID);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        RepairAPI repairAPI = retrofit.create(RepairAPI.class);
        Call<RepairRep> call = repairAPI.getResponse(repairReq);
        call.enqueue(new Callback<RepairRep>() {
            @Override
            public void onResponse(Call<RepairRep> call, Response<RepairRep> response) {


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "건의를 완료했습니다.", Toast.LENGTH_LONG).show();
//                        Log.d("test1", response.body().toRes());
                    Log.e("test1", response.toString());
                    String result = response.body().toRes();
                }else{
                    Toast.makeText(getApplicationContext(), "건의를 실패했습니다.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RepairRep> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "건의를 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        });


    }
}