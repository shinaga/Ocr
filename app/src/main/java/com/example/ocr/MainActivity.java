package com.example.ocr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    final static int TAKE_PICTURE = 1;
    final static int CROP_PICTURE = 2;
    private Uri pictureUri;

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ocrSetting();//ocr관련 세팅
    }
    //Process an Image

    private void ocrSetting(){
        imageView = findViewById(R.id.imageView);

        //언어파일 경로
        datapath = getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }

    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
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
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setText(OCRresult);
    }

    public void runCamera(View v){
        if(hasPermission()){//권한 허용 받았는가
            takePhoto();

        }else{
            requestPermission();
        }
    }
    private void takePhoto() {

        ImagePicker.Companion.with(this)
                .crop(16f, 9f)   //이미지 자르기 (16:9)
                .compress(1024)			//최종 이미지 크기는 1MB 미만
                .maxResultSize(1080, 1080)	//최종 이미지 해상도는 1080 x 1080 미만

                .start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){//이미지 Uri는 RESULT_OK에 대해 null이 아닐시
            Uri uri = data.getData();
            imageView.setImageURI(uri);

            processImage();
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "오류", Toast.LENGTH_SHORT).show();
        }

    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    private boolean hasPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED;
    }
}