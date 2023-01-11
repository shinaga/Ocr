package com.example.ocr;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class LoanFragment extends Fragment {
    Context context;
    View view;

    private RecyclerView recyclerView;
    private ArrayList<Equipment> equipmentList;//리사이클러뷰에 넣어줄 기자재 리스트
    private EquipmentAdapter recyclerAdapter;

    public LoanFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loan, container, false);

        recyclerViewSet(2);//RecyclerView 세팅한다.
        loadEquipment();//서버에서 기자재 목록 불러오기
        return view;
    }
    private void recyclerViewSet(int i) {
        equipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recyclerView = view.findViewById(R.id.recyclerView);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new EquipmentAdapter(i);//어댑터 대입

        /* initiate recyclerview */
        recyclerView.setAdapter(recyclerAdapter);//어댑터 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(context));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
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
                    connection.setRequestProperty("token", MainActivity.token);
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

                    getActivity().runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                        @Override
                        public void run() {
                            TextView text_count = view.findViewById(R.id.text_count);
                            text_count.setText("기자재 목록("+result.length()+"개)");
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
}
