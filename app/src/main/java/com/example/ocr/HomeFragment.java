package com.example.ocr;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    Context context;
    View view;

    private RecyclerView recyclerView;
    private ArrayList<Equipment> equipmentList;//리사이클러뷰에 넣어줄 기자재 리스트
    //private EquipmentListAdapter recyclerAdapter;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //recyclerViewSet();//RecyclerView 세팅한다.
        return view;
    }

    private void recyclerViewSet() {
        //equipmentList = new ArrayList<Equipment>();
//
        //recyclerView = view.findViewById(R.id.recyclerView);
        ///* initiate adapter */
        //recyclerAdapter = new EquipmentListAdapter();
//
        ///* initiate recyclerview */
        //recyclerView.setAdapter(recyclerAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ////mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
//
        //recyclerAdapter.setEquipmentList(equipmentList);//RecyclerView에 noticeList를 연결한다.
    }
}