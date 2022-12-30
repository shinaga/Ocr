package com.example.ocr;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReturnFragment extends Fragment {
    Context context;
    View view;

    private RecyclerView recyclerView;
    private ArrayList<Equipment> myEquipmentList;//리사이클러뷰에 넣어줄 기자재 리스트
    private LoanEquipmentAdapter recyclerAdapter;

    public ReturnFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_return, container, false);
        recyclerViewSet();

        return view;
    }

    private void recyclerViewSet() {
        myEquipmentList = new ArrayList<Equipment>();//ArrayList 생성

        recyclerView = view.findViewById(R.id.recyclerView);//layout에서 찾기
        /* initiate adapter */
        recyclerAdapter = new LoanEquipmentAdapter();//어댑터 대입

        /* initiate recyclerview */
        recyclerView.setAdapter(recyclerAdapter);//어댑터 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(context));//layout 형식 지정
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //recyclerAdapter.setEquipmentList(myEquipmentList);//RecyclerView에 noticeList를 연결한다.
    }
}
