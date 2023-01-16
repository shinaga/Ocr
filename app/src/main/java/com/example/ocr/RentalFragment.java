package com.example.ocr;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalFragment extends Fragment {
    static FragmentActivity fragmentActivity;//getActivity()를 사용하기 위함
    static TextView text_name,text_posible,text_code,text_number,text_purchase,text_date,text_standard;
    static Button btn_repair,btn_rental;
    static String name;
    ImageView img_camera,imageView;
    static ImageView img;
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로
    static Context context;
    View view;
    Uri photoUri;
    String imageFilePath;
    static TextView ocr;
    Retrofiyclient retrofiyclient;
    Inter inter;
    static LinearLayout linearLayout;

    public RentalFragment(Context context) {
        this.context = context;
    }

     // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rental, container, false);
        HomeActivity.flag = 1;
        img = view.findViewById(R.id.image);

        textSet();
        btnSet();
        linearLayout = view.findViewById(R.id.linearLayout);
        fragmentActivity = getActivity();

        ocrSetting();//ocr관련 세팅
        ocr = view.findViewById(R.id.text);//ocr작동 시켜주는 버튼
        ocr.setOnClickListener(v -> {
            // 카메라 기능을 Intent
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 사진파일 변수 선언 및 경로세팅
            File photoFile = null;
            try { photoFile = createImageFile(); } catch (IOException ex) { }

            // 사진을 저장하고 이미지뷰에 출력
            if(photoFile != null) {
                photoUri = FileProvider.getUriForFile(context, getActivity().getPackageName() + ".fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(i, 1);
            }
        });

        authority();
        img_camera = view.findViewById(R.id.img_camera);
        img_camera.setOnClickListener(v -> {
            new IntentIntegrator(getActivity()).initiateScan();
        });
        return view;
    }
    private void textSet() {
        text_name = view.findViewById(R.id.text_name);
        text_posible = view.findViewById(R.id.text_posible);
        text_code = view.findViewById(R.id.text_code);
        text_number = view.findViewById(R.id.text_number);
        text_purchase = view.findViewById(R.id.text_purchase);
        text_date = view.findViewById(R.id.text_date);
        text_standard = view.findViewById(R.id.text_standard);
    }
    private void btnSet() {
        btn_repair = view.findViewById(R.id.btn_repair);
        btn_rental = view.findViewById(R.id.btn_rental);
    }
    static void loadEquipment(String tool_id) {
        new Thread(){
            @Override
            public void run() {
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임
                    URL url = new URL("http://120.142.105.189:5080/tool/viewTool?tool_id="+tool_id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("Accept", " application/json"); // api 리턴값을 json으로 받을 경우!
                    connection.setRequestMethod("GET");// 통신방식
                    connection.setRequestProperty("token", MainActivity.token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    }
                    JSONObject obj = new JSONObject(response.toString());// jsonData를 먼저 JSONObject 형태로 바꾼다.
                    boolean suc = obj.getBoolean("suc");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출

                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(suc != true)Toast.makeText(fragmentActivity,"대여할 수 없는 기자재 입니다.",Toast.LENGTH_SHORT).show();
                            else{
                                try {
                                    JSONObject tool = obj.getJSONObject("tool");// jsonData를 먼저 JSONObject 형태로 바꾼다.
                                    JSONObject result = tool.getJSONObject("result");// jsonData를 먼저 JSONObject 형태로 바꾼다.

                                    String tool_name = result.getString("tool_name");
                                    text_name.setText(tool_name);
                                    String tool_state = result.getString("tool_state");
                                    text_posible.setText(tool_state);
                                    String tool_code = result.getString("tool_code");
                                    text_code.setText(tool_code);
                                    String tool_id = result.getString("tool_id");
                                    text_number.setText(tool_id);
                                    String tool_purchase_division = result.getString("tool_purchase_division");
                                    text_purchase.setText(tool_purchase_division);
                                    String tool_purchase_date = result.getString("tool_purchase_date");
                                    tool_purchase_date = tool_purchase_date.substring(0,10);//문자열 자르기
                                    text_date.setText(tool_purchase_date);
                                    String tool_standard = result.getString("tool_standard");
                                    text_standard.setText(tool_standard);

                                    JSONObject image = tool.getJSONObject("image");// 이미지를 가져오기 위해
                                    if(image!=null){//image가 없으면 false임
                                        String img_url = "http://120.142.105.189:5080/tool/"+image.getString("img_url");
                                        Glide.with(context).load(img_url).into(img);
                                    }
                                    btn_repair.setVisibility(View.VISIBLE);
                                    btn_rental.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                linearLayout.setVisibility(View.GONE);
                                btn_rental.setOnClickListener(v -> {
                                    rental(tool_id);
                                });
                                btn_repair.setOnClickListener(v->{
                                    Intent intent = new Intent(v.getContext(),RepairActivity.class);
                                    RepairActivity.tool_id = tool_id;
                                    v.getContext().startActivity(intent);
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    static void rental(String tool_id){
        new Thread(){
            @Override
            public void run() {
                try {
                    StringBuffer response = new StringBuffer();//여기에 json을 문자열로 받아올것임
                    URL url = new URL("http://120.142.105.189:5080/rental/rentalTool");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestMethod("POST");         // 통신방식
                    connection.setDoInput(true);                // 읽기모드 지정
                    connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                    connection.setConnectTimeout(15000);        // 통신 타임아웃a

                    JSONObject body = new JSONObject();
                    body.put("tool_id", /*원래는 tool_id로 해야함*/tool_id);
                    body.put("user_id", MainActivity.userid.getText().toString());
                    body.put("department_id", "1");

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    bw.write(body.toString());
                    bw.flush();
                    bw.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    }

                    JSONObject obj = new JSONObject(response.toString());// jsonData를 먼저 JSONObject 형태로 바꾼다.
                    boolean suc = obj.getBoolean("suc");// boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(suc!=true)Toast.makeText(fragmentActivity,"기자재를 대여하지 못했습니다.",Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(fragmentActivity,"기자재를 대여했습니다.",Toast.LENGTH_SHORT).show();
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void authority(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            }
            else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // 이미지파일을 bitmap 변수에 초기화
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            }
            catch(IOException e){
                e.printStackTrace();
            }

            // 이미지를 회전각도를 구한다
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            // 이미지를 출력
            imageView.setImageBitmap(rotate(bitmap, exifDegree));
            processImage();//OCR로 숫자를 잘라냄
        }
    }
    // ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_"; File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File StorageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    public static void makeOcr(){
        ocr.setVisibility(View.VISIBLE);
    }
    private void ocrSetting(){
        imageView = view.findViewById(R.id.imageView);

        //언어파일 경로
        datapath = context.getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
    }
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = context.getAssets();
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void processImage() {
        Drawable img = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)img).getBitmap();

        String OCRresult = null;
        mTess.setImage(bitmap);
        OCRresult = mTess.getUTF8Text();

        String pattern = "^[0-9]*$"; // 숫자만 등장하는지 확인하는 정규표현식
                String result = "사진을 다시 촬영해 주세요.";//촬영이 안되면 이 문자열이 출력됨
                for(int i=0;i<OCRresult.length()-13;i++){
                    if(Pattern.matches(pattern, OCRresult.substring(i,i+13))){//문자열이 13연속으로 숫자로 이어지는지 판단
                        result = OCRresult.substring(i,i+13);
                break;//for문을 빠져나온다.
            }
        }
        loadEquipment(result);
    }
}
