package com.example.ocr;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment{
    boolean flag = false;
    Context context;
    View view;
    EditText editText;
    TextView text_count;
    private RecyclerView recyclerView;
    private ArrayList<Equipment> equipmentList;//리사이클러뷰에 넣어줄 기자재 리스트
    private EquipmentAdapter recyclerAdapter;
    FragmentActivity fragmentActivity;
    public HomeFragment(Context context) {
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentActivity = getActivity();
        text_count = view.findViewById(R.id.text_count);

        recyclerViewSet(1);//RecyclerView 세팅한다.
        loadEquipment();//서버에서 기자재 목록 불러오기

        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
            flag=true;//프래그먼트 전환시 editText가 입력되는 오류가 있음
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0&&flag==true){
                    loadEquipment();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search();//검색 버튼 클릭
        authority();//권한 설정
        return view;
    }
    private void authority(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) fragmentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            }
            else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    private void search() {
        ImageView img_search = view.findViewById(R.id.img_search);
        img_search.setOnClickListener(v -> {
            new Thread(){
                @Override
                public void run() {
                    text_count.setText("기자재 목록 (0개)");
                    equipmentList.clear();
                    try {
                        StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                        URL url = new URL("http://120.142.105.189:5080/tool/viewToolList/1");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("content-type", "application/json");
                        connection.setRequestMethod("GET");         // 통신방식
                        connection.setDoInput(true);                // 읽기모드 지정
                        connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                        connection.setConnectTimeout(15000);        // 통신 타임아웃a
                        connection.setRequestProperty("token", MainActivity.token);
                        Log.e("TAG", "11");
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

                        getActivity().runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                            @Override
                            public void run() {
                                for(int i=0;i<result.length();i++){
                                    try {
                                        JSONObject tool = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                        Equipment equipment = new Equipment();
                                        equipment.name = tool.getString("tool_name");
                                        equipment.rental = tool.getString("tool_state");
                                        equipment.code = tool.getString("tool_use_division");
                                        equipment.number = tool.getString("tool_id");
                                        equipment.order = i;

                                        if(equipment.name.indexOf(editText.getText().toString())==-1){//특정 문자열이 포함되지 않을때 그냥 넘어감
                                            continue;
                                        }
                                        Thread th = new Thread(new ThreadSee(equipment, equipment.number));//리사이클러뷰 아이템을 클릭했을때 표시할 정보를 가져오기 위한 클래스
                                        th.start();
                                        th.join();
                                    } catch (JSONException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    private void recyclerViewSet(int i) {
        equipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recyclerView = view.findViewById(R.id.recyclerView);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new EquipmentAdapter(i,context);//어댑터 대입

        /* initiate recyclerview */
        recyclerView.setAdapter(recyclerAdapter);//어댑터 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(context));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
    }
    private void loadEquipment() {
        text_count.setText("기자재 목록 (0개)");
        new Thread(){
            @Override
            public void run() {
                equipmentList.clear();
                try {

                     StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임

                     URL url = new URL("http://120.142.105.189:5080/tool/viewToolList/1");
                     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                     connection.setRequestProperty("content-type", "application/json");
                     connection.setRequestMethod("GET");         // 통신방식
                     connection.setDoInput(true);                // 읽기모드 지정
                     connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                     connection.setConnectTimeout(15000);        // 통신 타임아웃a
                     connection.setRequestProperty("token", MainActivity.token);
                     Log.e("TAG", "11");
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

                     getActivity().runOnUiThread(new Runnable() {//getActivity().을 붙여야 fragment에서 runOnUiThread가 작동함
                         @Override
                         public void run() {
                            for(int i=0;i<result.length();i++){
                                try {
                                    JSONObject tool = result.getJSONObject(i);// result의 "i 번째"의 JSONObject를 추출
                                    Equipment equipment = new Equipment();
                                    equipment.name = tool.getString("tool_name");
                                    equipment.rental = tool.getString("tool_state");
                                    equipment.code = tool.getString("tool_use_division");
                                    equipment.number = tool.getString("tool_id");
                                    equipment.order = i;

                                    Thread th = new Thread(new ThreadSee(equipment, equipment.number));//리사이클러뷰 아이템을 클릭했을때 표시할 정보를 가져오기 위한 클래스
                                    th.start();
                                    th.join();
                                } catch (JSONException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                     });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        recyclerAdapter.notifyDataSetChanged();
    }
    public class ThreadSee extends Thread{
        Equipment equipment;
        String tool_id;
        ThreadSee(Equipment equipment, String tool_id){
            this.equipment = equipment;
            this.tool_id = tool_id;
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

                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(suc != true)Toast.makeText(fragmentActivity,"대여할 수 없는 기자재 입니다.",Toast.LENGTH_SHORT).show();
                        else{
                            try {
                                JSONObject tool = obj.getJSONObject("tool");// jsonData를 먼저 JSONObject 형태로 바꾼다.
                                JSONObject result = tool.getJSONObject("result");// jsonData를 먼저 JSONObject 형태로 바꾼다.

                                equipment.purchase_division = result.getString("tool_purchase_division");
                                equipment.purchase_date = result.getString("tool_purchase_date").substring(0,10);//문자열 자르기
                                equipment.standard = result.getString("tool_standard");

                                JSONObject image = tool.getJSONObject("image");// 이미지를 가져오기 위해
                                if(image!=null){//image가 없으면 false임
                                    equipment.url = image.getString("img_url");
                                }
                                equipmentList.add(equipment);

                                Collections.sort(equipmentList);
                                recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
                                recyclerAdapter.notifyDataSetChanged();

                                text_count.setText("기자재 목록 ("+equipmentList.size()+"개)");
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
}