package com.example.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    static int flag;
    Boolean master;
    HomeFragment homeFragment;
    RentalFragment rentalFragment;
    LoanFragment loanFragment;
    ReturnFragment returnFragment;

    Retrofiyclient retrofiyclient;
    Inter inter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getEmail();

        //ifMaster();//조교 또는 관리자, 필요 없어짐
        homeFragment = new HomeFragment(this);
        rentalFragment = new RentalFragment(this);
        loanFragment = new LoanFragment(this);
        returnFragment = new ReturnFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        //NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        //navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
        //    @Override
        //    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
        //        switch(item.getItemId()){
        //            case R.id.home:
        //                getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        //                return true;
        //            case R.id.rental:
        //                getSupportFragmentManager().beginTransaction().replace(R.id.containers, rentalFragment).commit();
        //                return true;
        //            case R.id.ret:
        //                getSupportFragmentManager().beginTransaction().replace(R.id.containers, loanFragment).commit();
        //                return true;
        //        }
        //        return false;
        //    }
        //});
        NavigationBarView navigationBarView2 = findViewById(R.id.bottom_navigationview2);
        navigationBarView2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
                    case R.id.rental:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, rentalFragment).commit();
                        return true;
                    case R.id.ret:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, loanFragment).commit();
                        return true;
                    case R.id.ret2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, returnFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void getEmail() {
        new Thread(){
            @Override
            public void run() {
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                    URL url = new URL("http://120.142.105.189:5080/user/inquireMyInfo/?user_id=");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestMethod("GET");         // 통신방식
                    connection.setDoInput(true);                // 읽기모드 지정
                    connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                    connection.setConnectTimeout(15000);        // 통신 타임아웃
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
                    JSONObject inquireMyInfo = obj.getJSONObject("inquireMyInfo");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출


                    runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                        @Override
                        public void run() {
                            try {
                                String email="";
                                email=inquireMyInfo.getString("user_email");
                                email = email.replace("@",".at.");
                                String finalEmail = email;
                                FirebaseMessaging.getInstance().subscribeToTopic(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String msg = "Subscribed";
                                                if (!task.isSuccessful()) {
                                                    msg = "Subscribe failed";
                                                }
                                                //Toast.makeText(HomeActivity.this, msg+ finalEmail, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void ifMaster() {
        new Thread(){
        @Override
        public void run() {
            try {
                StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임
                URL url = new URL("http://120.142.105.189:5080/user/inquireMyInfo/?user_id="+MainActivity.userid.getText().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("content-type", "application/json");
                connection.setRequestProperty("Accept", " application/json"); // api 리턴값을 json으로 받을 경우!
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }.start();
    }

    public void myProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//이 부분은 fragment에서 안됨
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                RentalFragment.makeOcr();//qr이 안되면 ocr로 한다.
            }else {
                String datas = "대여가능";
                if(flag==1) RentalFragment.loadEquipment("20"+result.getContents());
                else if(flag==2) ReturnFragment.loadEquipment("20"+result.getContents());
                Req req = new Req(result.getContents(),datas);
                retrofiyclient = Retrofiyclient.getInstance();
                inter = Retrofiyclient.getInter();

                inter.getRes(req).enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Toast.makeText(getApplicationContext(),"성공", Toast.LENGTH_LONG).show();
                            Log.d("respose", String.valueOf(response));

                        }

                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                        Log.d("fali", String.valueOf(t));
                    }
                });
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}