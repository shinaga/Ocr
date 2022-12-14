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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

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
    static TextView text_name;
    static String name;
    ImageView img_camera, imageView;
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //?????????????????? ?????? ??????
    Context context;
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

     // ?????? ??????
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
        linearLayout = view.findViewById(R.id.linearLayout);

        ocrSetting();//ocr?????? ??????
        ocr = view.findViewById(R.id.text);//ocr?????? ???????????? ??????
        ocr.setOnClickListener(v -> {
            // ????????? ????????? Intent
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // ???????????? ?????? ?????? ??? ????????????
            File photoFile = null;
            try { photoFile = createImageFile(); } catch (IOException ex) { }

            // ????????? ???????????? ??????????????? ??????
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
    static void loadEquipment(String tool_id) {
       linearLayout.setVisibility(View.GONE);
       Toast.makeText(linearLayout.getContext(), "asdf",Toast.LENGTH_SHORT).show();

        new Thread(){
            @Override
            public void run() {
                try {

                    StringBuffer response = new StringBuffer();//????????? json??? ???????????? ???????????????
                    URL url = new URL("http://120.142.105.189:5080/rental/rentalTool");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("Accept", " application/json"); // api ???????????? json?????? ?????? ??????!
                    connection.setRequestMethod("POST");         // ????????????
                    connection.setDoInput(true);                // ???????????? ??????
                    connection.setUseCaches(false);             // ?????????????????? ????????? ????????????
                    connection.setConnectTimeout(15000);        // ?????? ????????????a
                    //connection.setRequestProperty("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoic3NzIiwidXNlcl9saWNlbnNlIjozLCJleHAiOjE2NzMyMTk5NDUsImlhdCI6MTY3MzE5ODM0NSwiaXNzIjoiYWVsaW1pIn0.o24KC-1fTwGPMF0vMW-XojXAoyu1cBs6kb2Ea6woceQ");

                    JSONObject body = new JSONObject();
                    body.put("tool_id", /*????????? tool_id??? ?????????*/"test1");
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

                    JSONObject obj = new JSONObject(response.toString());// jsonData??? ?????? JSONObject ????????? ?????????.
                    boolean suc = obj.getBoolean("suc");// boxOfficeResult??? JSONObject?????? "dailyBoxOfficeList"??? JSONArray ??????

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        /*if ?????? ????????? ?????????*/rental();//??????
    }
    static void rental(){
        new Thread(){
            @Override
            public void run() {
                try {

                    StringBuffer response = new StringBuffer();//????????? json??? ???????????? ???????????????
                    URL url = new URL("http://120.142.105.189:5080/rental/rentalTool");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("Accept", " application/json"); // api ???????????? json?????? ?????? ??????!
                    connection.setRequestMethod("POST");         // ????????????
                    connection.setDoInput(true);                // ???????????? ??????
                    connection.setUseCaches(false);             // ?????????????????? ????????? ????????????
                    connection.setConnectTimeout(15000);        // ?????? ????????????a
                    //connection.setRequestProperty("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoic3NzIiwidXNlcl9saWNlbnNlIjozLCJleHAiOjE2NzMyMTk5NDUsImlhdCI6MTY3MzE5ODM0NSwiaXNzIjoiYWVsaW1pIn0.o24KC-1fTwGPMF0vMW-XojXAoyu1cBs6kb2Ea6woceQ");

                    JSONObject body = new JSONObject();
                    body.put("tool_id", /*????????? tool_id??? ?????????*/"test1");
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

                    JSONObject obj = new JSONObject(response.toString());// jsonData??? ?????? JSONObject ????????? ?????????.
                    boolean suc = obj.getBoolean("suc");// boxOfficeResult??? JSONObject?????? "dailyBoxOfficeList"??? JSONArray ??????

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void authority(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "?????? ?????? ??????");
            }
            else {
                Log.d(TAG, "?????? ?????? ??????");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ????????? ????????? ?????? ??????????????? ?????? ??????
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // ?????????????????? bitmap ????????? ?????????
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            }
            catch(IOException e){
                e.printStackTrace();
            }

            // ???????????? ??????????????? ?????????
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            // ???????????? ??????
            imageView.setImageBitmap(rotate(bitmap, exifDegree));
            processImage();//OCR??? ????????? ?????????
        }
    }
    // ImageFile??? ????????? ????????? ????????? ??????
    private File createImageFile() throws IOException {
        // ??????????????? ?????? ??? ???????????? ??????
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

        //???????????? ??????
        datapath = context.getFilesDir()+ "/tesseract/";

        //???????????????????????? ???????????? ????????? ??????
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
    }
    private void checkFile(File dir) {
        //??????????????? ????????? ??????????????? ????????? ????????? ????????? ??????
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //??????????????? ????????? ????????? ????????? ???????????? ??????
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

        String pattern = "^[0-9]*$"; // ????????? ??????????????? ???????????? ???????????????
                String result = "????????? ?????? ????????? ?????????.";//????????? ????????? ??? ???????????? ?????????
                for(int i=0;i<OCRresult.length()-13;i++){
                    if(Pattern.matches(pattern, OCRresult.substring(i,i+13))){//???????????? 13???????????? ????????? ??????????????? ??????
                        result = OCRresult.substring(i,i+13);
                break;//for?????? ???????????????.
            }
        }
        loadEquipment(result);
    }
}
