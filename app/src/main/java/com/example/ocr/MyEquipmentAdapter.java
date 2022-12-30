package com.example.ocr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyEquipmentAdapter extends RecyclerView.Adapter<MyEquipmentAdapter.ViewHolder>{
    private ArrayList<Equipment> equipmentList;

    public void setEquipmentList(ArrayList<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    @NonNull
    @Override
    public MyEquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_equipment_recyclerview, parent, false);
        return new MyEquipmentAdapter.ViewHolder(view);
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text_name = itemView.findViewById(R.id.text_name);
        }
        void onBind(Equipment item) {
            image.setClipToOutline(true);//이게 없으면 둥글게 안나옴
            text_name.setText(item.code+"");
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(equipmentList.get(position));
    }
}