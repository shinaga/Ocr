package com.example.ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class TendinousActivity extends AppCompatActivity {
    private RecyclerView recycler_rental;
    private ArrayList<Equipment> equipmentList;//리사이클러뷰에 넣어줄 기자재 리스트
    private EquipmentAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tendinous);

        recyclerViewSet(1);//RecyclerView 세팅한다.
        loadEquipment();//서버에서 기자재 목록 불러오기
    }
    private void recyclerViewSet(int i) {
        equipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recycler_rental = findViewById(R.id.recycler_rental);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new EquipmentAdapter(i,getApplicationContext());//어댑터 대입
        /* initiate recyclerview */
        recycler_rental.setAdapter(recyclerAdapter);//어댑터 연결
        recycler_rental.setLayoutManager(new LinearLayoutManager(this));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //recyclerAdapter.setEquipmentList(myEquipmentList);//RecyclerView에 noticeList를 연결한다.
    }
    public class ThreadSee extends Thread{
        Equipment equipment;
        String tool_id;
        int type;
        ThreadSee(Equipment equipment, String tool_id, int type){
            this.equipment = equipment;
            this.tool_id = tool_id;
            this.type = type;
        }
        public void run()
        {
            try {

                StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임
                URL url = new URL("http://120.142.105.189:5080/tool/viewTool?tool_id="+tool_id);
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(suc != true) Toast.makeText(getApplicationContext(),"대여할 수 없는 기자재 입니다.",Toast.LENGTH_SHORT).show();
                        else{
                            try {
                                JSONObject tool = obj.getJSONObject("tool");// jsonData를 먼저 JSONObject 형태로 바꾼다.
                                JSONObject result = tool.getJSONObject("result");// jsonData를 먼저 JSONObject 형태로 바꾼다.

                                equipment.name = result.getString("tool_name");
                                equipment.rental = result.getString("tool_state");
                                equipment.number = tool_id;
                                equipment.code = result.getString("tool_use_division");
                                equipment.purchase_date = result.getString("tool_purchase_date").substring(0,10);//문자열 자르기
                                equipment.update_at = result.getString("tool_update_at").substring(0,10);//문자열 자르기;
                                equipment.purchase_division = result.getString("tool_purchase_division");
                                equipment.standard = result.getString("tool_standard");
                                JSONObject image = tool.getJSONObject("image");// 이미지를 가져오기 위해
                                if(image!=null){//image가 없으면 false임
                                    equipment.url = image.getString("img_url");
                                }

                                if(type==1){
                                    equipmentList.add(equipment);

                                    Collections.sort(equipmentList);
                                    recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (ProtocolException protocolException) {
                protocolException.printStackTrace();
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    private void loadEquipment() {
        new Thread(){
            @Override
            public void run() {
                equipmentList.clear();
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                    URL url = new URL("http://120.142.105.189:5080/repair/myRepairList/1");
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
                    JSONArray result = obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                    runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                        @Override
                        public void run() {
                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Equipment equipment = new Equipment();

                                    Thread th = new Thread(new ThreadSee(equipment, tool.getJSONObject("tool").getString("tool_id"),1));//리사이클러뷰 아이템을 클릭했을때 표시할 정보를 가져오기 위한 클래스
                                    th.start();
                                    th.join();
                                } catch (JSONException | InterruptedException e) {
                                    e.printStackTrace();
                                }
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
}