package com.example.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocr.manageRst1;
import com.example.ocr.enterpriseRst1;

public class Register extends AppCompatActivity {

    ImageView img_student, img_register_enterprise, img_register_manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img_student = findViewById(R.id.img_register_students);
        img_register_enterprise = findViewById(R.id.img_register_enterprise);
        img_register_manager = findViewById(R.id.img_register_manager);



        img_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, com.example.ocr.Register2.class);
                startActivity(intent);

            }
        });

        img_register_enterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, enterpriseRst1.class);
                startActivity(intent);
            }
        });

        img_register_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, manageRst1.class);
                startActivity(intent);
            }
        });



    }
}