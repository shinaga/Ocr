package com.example.ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView recycler_rental,recycler_tendinous;
    private ArrayList<Equipment> equipmentList,equipmentList2;//리사이클러뷰에 넣어줄 기자재 리스트
    private EquipmentAdapter recyclerAdapter,recyclerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recyclerViewSet();
    }
    private void recyclerViewSet() {
        equipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recycler_rental = findViewById(R.id.recycler_rental);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new EquipmentAdapter();//어댑터 대입
        /* initiate recyclerview */
        recycler_rental.setAdapter(recyclerAdapter);//어댑터 연결
        recycler_rental.setLayoutManager(new LinearLayoutManager(this));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //recyclerAdapter.setEquipmentList(myEquipmentList);//RecyclerView에 noticeList를 연결한다.
        //두번째 리사이클러뷰
        equipmentList2 = new ArrayList<Equipment>();//ArrayList 생성
        recycler_tendinous = findViewById(R.id.recycler_tendinous);//layout에서 찾기

        recyclerAdapter2 = new EquipmentAdapter();//어댑터 대입
        /* initiate recyclerview */
        recycler_tendinous.setAdapter(recyclerAdapter2);//어댑터 연결
        recycler_tendinous.setLayoutManager(new LinearLayoutManager(this));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //recyclerAdapter.setEquipmentList(myEquipmentList);//RecyclerView에 noticeList를 연결한다.
    }
}