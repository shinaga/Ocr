package com.example.ocr;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder>{
    private ArrayList<Equipment> equipmentList = new ArrayList<>();
    int i;//어디서 부른 리사이클러뷰인지 식별하기 위한 숫자
    EquipmentAdapter(int i){
        this.i = i;
    }
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
        TextView text_name,text_posible,text_code,text_number,text_purchase,text_day,text_standard;
        Button btn_repair;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

            if(i==1) linear = itemView.findViewById(R.id.linear1);
            else if(i==2) linear = itemView.findViewById(R.id.linear2);
            itemView.setOnClickListener(v -> {//안에 내용물이 열려있는지 아닌지 확인
                if(linear!=null) {//linear가 지정이 안되어 있을 수도 있음
                    if(linear.getVisibility() == View.GONE) {
                        linear.setVisibility(View.VISIBLE);
                    } else{
                        linear.setVisibility(View.GONE);
                    }
                }
            });
            text_name = itemView.findViewById(R.id.text_name);
            text_posible = itemView.findViewById(R.id.text_posible);
            text_code = itemView.findViewById(R.id.text_code);
            text_number = itemView.findViewById(R.id.text_number);
            text_purchase = itemView.findViewById(R.id.text_purchase);
            text_day = itemView.findViewById(R.id.text_date);
            text_standard = itemView.findViewById(R.id.text_standard);

            btn_repair = itemView.findViewById(R.id.btn_repair);
        }
        void onBind(Equipment item) {
            image.setClipToOutline(true);//이게 없으면 둥글게 안나옴

            if(item.name!=null){
                text_name.setText(item.name);
                text_name.setVisibility(View.VISIBLE);
            }
            if(item.rental!=null){
                text_posible.setText(item.rental);
                text_posible.setVisibility(View.VISIBLE);
            }
            if(item.code!=null){
                text_code.setText("품목 코드 :   "+item.code);
                text_code.setVisibility(View.VISIBLE);
            }
            if(item.number!=null){
                text_number.setText("자산 번호 :   "+item.number);
                text_number.setVisibility(View.VISIBLE);
            }
            if(item.purchase_division!=null){
                text_purchase.setText("구입 구분 :   "+item.purchase_division);
                text_purchase.setVisibility(View.VISIBLE);
            }
            if(item.day!=null){
                text_day.setText("구입 일자 :   "+item.day);
                text_day.setVisibility(View.VISIBLE);
            }
            if(item.standard!=null){
                text_standard.setText("물품 규격 :   "+item.standard);
                text_standard.setVisibility(View.VISIBLE);
            }

            btn_repair.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(),RepairActivity.class);
                RepairActivity.tool_id = item.number;
                v.getContext().startActivity(intent);
            });
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(equipmentList.get(position));
    }

}
