package com.example.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PWfind extends AppCompatActivity {

    private TextView Userid;
    private Button mainpagebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwfind);

        Intent intent = getIntent();
        Userid = findViewById(R.id.userid);
        mainpagebtn = findViewById(R.id.btn_idfind);

        Userid.setText(intent.getStringExtra("id"));
        mainpagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PWfind.this, MainActivity.class);
                startActivity(intent1);
                PWfind.this.finish();
            }
        });


    }
}