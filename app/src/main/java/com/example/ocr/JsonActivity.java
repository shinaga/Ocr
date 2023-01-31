package com.example.ocr;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class JsonActivity extends AppCompatActivity {

    private String address = "http://120.142.105.189:5080/tool/viewToolList/1/1";
    private ListView listView;
    private Button btnData;
    ArrayAdapter adapter;

    // 영화 제목을 담을 ArrayList 변수(items) 선언
    ArrayList<String> items = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        listView = (ListView)findViewById(R.id.listView1);
        // adapter 스타일 선언 및 items 적용
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        // listView에 adapter 적용
        listView.setAdapter(adapter);
        btnData = (Button)findViewById(R.id.btnData);

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        items.clear();
                        try {
                            URL url = new URL(address);

                            InputStream is = url.openStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader reader = new BufferedReader(isr);

                            StringBuffer buffer = new StringBuffer();
                            String line = reader.readLine();
                            while (line != null) {
                                buffer.append(line + "\n");
                                line = reader.readLine();
                            }

                            String jsonData = buffer.toString();

                            JSONObject obj = new JSONObject(jsonData);// jsonData를 먼저 JSONObject 형태로 바꾼다.
                            //JSONObject result = (JSONObject)obj.get("result");// obj의 "result"의 JSONObject를 추출
                            JSONArray result = (JSONArray)obj.getJSONArray("result");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                            items.add(result.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        // 리스트뷰의 아이템 클릭 이벤트 > 토스트 메시지 띄우기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                Toast.makeText(JsonActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

    }




}