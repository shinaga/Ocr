package com.example.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register2 extends AppCompatActivity {

    Button btn_register2;
    EditText  et_username;
    TextView tvzero, tvzero1;
    Spinner et_register2_schooll, et_register2_userdepartment;


    String[] item = {"명지전문대학교", "데이터없음", "데이터없음", "데이터없음"};
    String[] itemsub = {"소프트웨어콘텐츠과", "데이터없음", "데이터없음", "데이터없음"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        btn_register2 = findViewById(R.id.btn_register_next);
        et_register2_schooll = findViewById(R.id.usershooll);
        et_register2_userdepartment = findViewById(R.id.userdepartment);
        et_username = findViewById(R.id.username);
        tvzero = findViewById(R.id.tv_zero);
        tvzero1 = findViewById(R.id.tv_zero1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_register2_schooll.setAdapter(adapter);
        et_register2_schooll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tvzero.setText(item[position]);

                String tvw = item[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 tvzero.setText("선택");
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsub);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_register2_userdepartment.setAdapter(adapter1);
        et_register2_userdepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvzero1.setText(itemsub[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 tvzero1.setText("");
            }
        });








     et_username.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                   btn_register2.setBackgroundResource(R.drawable.loginbackgrounddrawablebutton);
         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             btn_register2.setBackgroundResource(R.drawable.loginbackgrounddrawablebuttonemail);
         }

         @Override
         public void afterTextChanged(Editable s) {

           if(s.length() != 0) {
               btn_register2.setBackgroundResource(R.drawable.loginbackgrounddrawablebuttonemail);
           }else {
               btn_register2.setBackgroundResource(R.drawable.loginbackgrounddrawablebutton);
           }
         }
     });


        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = et_username.getText().toString();



               if(username.trim().length() == 0 || username == null){
                   AlertDialog.Builder builder = new AlertDialog.Builder(Register2.this);
                   builder.setTitle("알림").setMessage("정보를 바르게 입력하시오.").setPositiveButton("확인", null).create().show();
                   AlertDialog alertDialog = builder.create();
                   alertDialog.show();
               }else {

                   Intent intent = new Intent(Register2.this, Register3.class);
                   intent.putExtra("username", username);
                   startActivity(intent);
               }

            }
        });
    }
}