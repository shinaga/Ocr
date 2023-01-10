package com.example.ocr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class manageRst1 extends AppCompatActivity {

    Button btn_register2;
    EditText usermanage, et_register2_userdepartment, et_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rst1);


        btn_register2 = findViewById(R.id.btn_register_next);
        usermanage = findViewById(R.id.usermanage);
        et_register2_userdepartment = findViewById(R.id.userdepartment);
        et_username = findViewById(R.id.username);

        et_register2_userdepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_register2.setBackgroundColor(Color.parseColor("#9785CB"));
            }
        });


        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userManage = usermanage.getText().toString();
                String department = et_register2_userdepartment.getText().toString();
                String username = et_username.getText().toString();



                if(userManage.trim().length() == 0 || department.trim().length() == 0 || userManage == null || department == null || username.trim().length() == 0 || username == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(manageRst1.this);
                    builder.setTitle("알림").setMessage("정보를 바르게 입력하시오.").setPositiveButton("확인", null).create().show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {


                    Intent intent = new Intent(manageRst1.this, manageRst2.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }

            }
        });
    }
}