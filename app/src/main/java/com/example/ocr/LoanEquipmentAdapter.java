package com.example.ocr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoanEquipmentAdapter extends RecyclerView.Adapter<LoanEquipmentAdapter.ViewHolder>{

    @Override
    public int getItemCount() {
        return 10;
    }

    @NonNull
    @Override
    public LoanEquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_equipment_recyclerview, parent, false);
        return new LoanEquipmentAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout linear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

            linear = itemView.findViewById(R.id.linear);
            itemView.setOnClickListener(v -> {
                if(linear.getVisibility()==View.GONE){
                    linear.setVisibility(View.VISIBLE);
                }else {
                    linear.setVisibility(View.GONE);
                }
            });

        }
        void onBind(Equipment item) {
            image.setClipToOutline(true);//이게 없으면 둥글게 안나옴
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(new Equipment());
    }
}