package com.example.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    RentalFragment rentalFragment;
    ReturnFragment returnFragment;

    Retrofiyclient retrofiyclient;
    Inter inter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment(this);
        rentalFragment = new RentalFragment(this);
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, returnFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    public void myProfile(View v){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//이 부분은 fragment에서 안됨
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getApplicationContext(), "안됨", Toast.LENGTH_LONG).show();
            }else {

                Toast.makeText(getApplicationContext(), "스캔~" + result.getContents(), Toast.LENGTH_LONG).show();
                String datas = "대여가능";

                Req req = new Req(result.getContents(),datas);
                retrofiyclient = Retrofiyclient.getInstance();
                inter = Retrofiyclient.getInter();

                inter.getRes(req).enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG).show();
                            Log.d("respose", String.valueOf(response));

                        }

                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                        Log.d("fali", String.valueOf(t));
                    }
                });

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
//                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}