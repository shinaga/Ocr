package com.example.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    static int flag;
    HomeFragment homeFragment;
    RentalFragment rentalFragment;
    LoanFragment loanFragment;
    ReturnFragment returnFragment;

    Retrofiyclient retrofiyclient;
    Inter inter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment = new HomeFragment(this);
        rentalFragment = new RentalFragment(this);
        loanFragment = new LoanFragment(this);
        returnFragment = new ReturnFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
                    case R.id.rental:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, rentalFragment).commit();
                        return true;
                    case R.id.ret:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, loanFragment).commit();
                        return true;
                }
                return false;
            }
        });
        NavigationBarView navigationBarView2 = findViewById(R.id.bottom_navigationview2);
        navigationBarView2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
                    case R.id.rental:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, rentalFragment).commit();
                        return true;
                    case R.id.ret:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, loanFragment).commit();
                        return true;
                    case R.id.ret2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, returnFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    public void myProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//이 부분은 fragment에서 안됨
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                RentalFragment.makeOcr();//qr이 안되면 ocr로 한다.
            }else {
                String datas = "대여가능";
                if(flag==1) RentalFragment.loadEquipment(/*"20"+result.getContents()원래는 이걸로 해야함*/"123456");
                else if(flag==2) ReturnFragment.loadEquipment(/*"20"+result.getContents()원래는 이걸로 해야함*/"123456");
                Req req = new Req(result.getContents(),datas);
                retrofiyclient = Retrofiyclient.getInstance();
                inter = Retrofiyclient.getInter();

                inter.getRes(req).enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Toast.makeText(getApplicationContext(),"성공", Toast.LENGTH_LONG).show();
                            Log.d("respose", String.valueOf(response));

                        }

                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                        Log.d("fali", String.valueOf(t));
                    }
                });
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}