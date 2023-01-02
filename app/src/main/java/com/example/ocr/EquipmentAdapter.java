package com.example.ocr;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder>{
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
    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_recyclerview, parent, false);
        return new EquipmentAdapter.ViewHolder(view);
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout linear;
        TextView text_name,text_posible,text_code,text_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

            linear = itemView.findViewById(R.id.linear);
            itemView.setOnClickListener(v -> {//안에 내용물이 열려있는지 아닌지 확인
                if(linear.getVisibility()==View.GONE){
                    linear.setVisibility(View.VISIBLE);
                }else {
                    linear.setVisibility(View.GONE);
                }
            });
            text_name = itemView.findViewById(R.id.text_name);
            text_posible = itemView.findViewById(R.id.text_posible);
            text_code = itemView.findViewById(R.id.text_code);
            text_number = itemView.findViewById(R.id.text_number);
        }
        void onBind(Equipment item) {
            image.setClipToOutline(true);//이게 없으면 둥글게 안나옴
            text_name.setText(item.name+"");
            text_posible.setText(item.rental+"");
            text_code.setText(item.code+"");
            text_number.setText(item.number+"");
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(equipmentList.get(position));
    }

}
