package com.example.uploadfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText Filename,Content;
    Button btn;
    String text;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    File foodfilepath;
    TransferUtility transferUtility;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //aws에 접근하기위한 정보들
        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "ap-northeast-2:0736fa12-bb2d-489c-90e5-460af3a31e38",
                Regions.AP_NORTHEAST_2
        );
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        final TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

        // 여기는 이제 음식 입력
        Content = findViewById(R.id.content);
        btn = findViewById(R.id.btn);

        //이걸 그냥 음식 입력 버튼이라고 나중에 생각
        //현재는 저장 및 aws에 업로드 버튼
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               text = Content.getText().toString().trim();
               if(text.isEmpty()){ //비어있다면
                   Toast.makeText(MainActivity.this,"Please enter something",Toast.LENGTH_SHORT).show();
               }
               else{ //음식이 입력된다면
                   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                           String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                           //show popup for runtime permission
                           requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                       }
                       else{//음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자.
                           //permission already granted
                           saveToTxtFile(text);
                           //저장하고나면 upload 시키자자
                           TransferObserver Observer = transferUtility.upload(
                                   "food-anlaysis-text",
                                   "foodname.txt",
                                   foodfilepath
                                   //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                           );
                           Toast.makeText(MainActivity.this, "Success to upload",Toast.LENGTH_LONG).show();
                       }
                   }
                   else{
                       saveToTxtFile(text);


                       //저장하고나면 upload 시키자자
                       TransferObserver Observer = transferUtility.upload(
                               "food-anlaysis-text",
                               "textfile",
                               foodfilepath
                       );
                       Toast.makeText(MainActivity.this, "Success to upload",Toast.LENGTH_LONG).show();

                   }

               }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                //if request if cancelled,
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(text);
                }
                else{
                    Toast.makeText(MainActivity.this,"Storage permissions ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveToTxtFile(String text){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        try{
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path+"/My Files/");

            dir.mkdirs();
            String fileName = "MyFile_"+timeStamp+".txt";

            File file = new File(dir,fileName);
            foodfilepath = file;

            //FileWriter class is use to store file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();

            Toast.makeText(MainActivity.this,fileName+" is saved"+dir,Toast.LENGTH_SHORT).show();

        }catch (Exception e){

            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }


}
