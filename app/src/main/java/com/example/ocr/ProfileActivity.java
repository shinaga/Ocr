package com.example.ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView recycler_rental,recycler_tendinous;
    private ArrayList<Equipment> equipmentList,equipmentList2;//리사이클러뷰에 넣어줄 기자재 리스트
    private EquipmentAdapter recyclerAdapter,recyclerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recyclerViewSet(3,4);//RecyclerView 세팅한다.
        loadEquipment();//서버에서 기자재 목록 불러오기
        loadEquipment2();//서버에서 기자재 목록 불러오기2
    }
    private void recyclerViewSet(int i, int j) {
        equipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recycler_rental = findViewById(R.id.recycler_rental);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new EquipmentAdapter(i);//어댑터 대입
        /* initiate recyclerview */
        recycler_rental.setAdapter(recyclerAdapter);//어댑터 연결
        recycler_rental.setLayoutManager(new LinearLayoutManager(this));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //recyclerAdapter.setEquipmentList(myEquipmentList);//RecyclerView에 noticeList를 연결한다.

        //두번째 리사이클러뷰
        equipmentList2 = new ArrayList<Equipment>();//ArrayList 생성
        recycler_tendinous = findViewById(R.id.recycler_tendinous);//layout에서 찾기

        recyclerAdapter2 = new EquipmentAdapter(j);//어댑터 대입
        /* initiate recyclerview */
        recycler_tendinous.setAdapter(recyclerAdapter2);//어댑터 연결
        recycler_tendinous.setLayoutManager(new LinearLayoutManager(this));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        recyclerAdapter.setEquipmentList(equipmentList2);//RecyclerView에 noticeList를 연결한다.
    }
    private void loadEquipment() {
        new Thread(){
            @Override
            public void run() {
                equipmentList.clear();
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                    URL url = new URL("http://120.142.105.189:5080/tool/viewToolList/1/1");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestMethod("GET");         // 통신방식
                    connection.setDoInput(true);                // 읽기모드 지정
                    connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                    connection.setConnectTimeout(15000);        // 통신 타임아웃
                    connection.setRequestProperty("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoic3NzIiwidXNlcl9saWNlbnNlIjozLCJleHAiOjE2NzMyNTg5OTksImlhdCI6MTY3MzIzNzM5OSwiaXNzIjoiYWVsaW1pIn0.AGa5ugl5Z2z4Fweh0hbOffRdXwgr2qO1SjP20PTRa6M");
                    Log.e("TAG", "11");
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);

                            JSONObject obj = new JSONObject(response.toString());// String을 먼저 JSONObject 형태로 바꾼다.
                            JSONArray result = obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool_state = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Log.d("TAG",tool_state.getString("tool_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
                    JSONArray result = obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                    runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                        @Override
                        public void run() {
                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Equipment equipment = new Equipment();
                                    equipment.name = tool.getString("tool_name");
                                    equipment.rental=tool.getString("tool_state");
                                    equipment.code="품목 코드 :   "+tool.getString("tool_use_division");
                                    equipment.number="자산 번호 :   "+tool.getString("tool_id");
                                    equipmentList.add(equipment);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
                            recyclerAdapter.notifyDataSetChanged();
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
    private void loadEquipment2() {
        new Thread(){
            @Override
            public void run() {
                equipmentList.clear();
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                    URL url = new URL("http://120.142.105.189:5080/tool/viewToolList/1/1");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestMethod("GET");         // 통신방식
                    connection.setDoInput(true);                // 읽기모드 지정
                    connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                    connection.setConnectTimeout(15000);        // 통신 타임아웃
                    connection.setRequestProperty("token", "loadEquipment");
                    Log.e("TAG", "11");
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);

                            JSONObject obj = new JSONObject(response.toString());// String을 먼저 JSONObject 형태로 바꾼다.
                            JSONArray result = obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool_state = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Log.d("TAG",tool_state.getString("tool_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
                    JSONArray result = obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                    runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                        @Override
                        public void run() {
                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Equipment equipment = new Equipment();
                                    equipment.name = tool.getString("tool_name");
                                    equipment.rental=tool.getString("tool_state");
                                    equipment.code="품목 코드 :   "+tool.getString("tool_use_division");
                                    equipment.number="자산 번호 :   "+tool.getString("tool_id");
                                    equipmentList2.add(equipment);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            recyclerAdapter2.setEquipmentList(equipmentList2);//RecyclerView에 noticeList를 연결한다.
                            recyclerAdapter2.notifyDataSetChanged();
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
}