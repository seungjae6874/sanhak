package com.example.chm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import java.io.File;
import java.io.IOException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.text.SimpleDateFormat;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddDietActivity extends AppCompatActivity
{
    TextView TextV1,TextV2;
    EditText editTextInput;
    Button textInputButton,voiceInputButton,resetButton ;
    ImageButton voiceRecordButton;
    String text;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    File foodfilepath;
    TransferUtility transferUtilitytext;
    TransferUtility transferUtilityaudio;

    MediaRecorder recorder;
    String fileName;
    MediaPlayer mediaPlayer;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    int position = 0;
    @Override



    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfood);

        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "ap-northeast-2:0736fa12-bb2d-489c-90e5-460af3a31e38",
                Regions.AP_NORTHEAST_2
        );
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");




        TextV1 = findViewById(R.id.textView1);
        TextV2 = findViewById(R.id.textView2);
        editTextInput = findViewById(R.id.editText);  // Text입력 받는 곳
        textInputButton = findViewById(R.id.TextInputButton); // 텍스트 입력완료
        voiceInputButton = findViewById(R.id.VoiceInputButton); // 녹음 버튼 누르면 입력
        voiceRecordButton = findViewById(R.id.VoiceRecordButton); // 입력 완료 버튼튼



        final TransferUtility transferUtilitytext = new TransferUtility(s3, getApplicationContext());
        final TransferUtility transferUtilityaudio = new TransferUtility(s3, getApplicationContext());

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/My AudioFiles/");
        dir.mkdirs();

        final File file = new File(dir, "foodname.mp4");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴
        Toast.makeText(getApplicationContext(), "파일 위치:"+fileName, Toast.LENGTH_SHORT).show();

        Intent Intent = getIntent();// 인덴트 받아오고 시작

        // 여기는 이제 음식텍스트로 입력
        textInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = editTextInput.getText().toString().trim();
                if (text.isEmpty()) { //비어있다면
                    Toast.makeText(AddDietActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                } else { //음식이 입력된다면
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            //show popup for runtime permission
                            requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
                        } else {//음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자.
                            //permission already granted
                            saveToTxtFile(text);
                            //저장하고나면 upload 시키자자
                            TransferObserver Observer = transferUtilitytext.upload(
                                    "food-anlaysis-text",
                                    "foodname.txt",
                                    foodfilepath
                                    //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                            );
                            Toast.makeText(AddDietActivity.this, "Success to upload", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        saveToTxtFile(text);


                        //저장하고나면 upload 시키자자
                        TransferObserver Observer = transferUtilitytext.upload(
                                "food-anlaysis-text",
                                "textfile",
                                foodfilepath
                        );
                        Toast.makeText(AddDietActivity.this, "Success to upload", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });



        // 음성입력

        //녹음 시작
        voiceRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 시작
                if (recorder == null) {
                    recorder = new MediaRecorder(); // 미디어리코더 객체 생성
                }
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 입력 지정(마이크)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);    // 출력 형식 지정
                //마이크로 들어오는 음성데이터는 용량이 크기 때문에 압축이 필요
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   // 인코딩
                recorder.setOutputFile(fileName);  // 음성 데이터를 저장할 파일 지정
                try {
                    recorder.prepare();
                    recorder.start();
                    Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        // 녹음 종료
        findViewById(R.id.ResetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 종료
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }





                Toast.makeText(getApplicationContext(), "녹음종료", Toast.LENGTH_SHORT).show();
            }
        });


        // AWS에 음성전송
        findViewById(R.id.VoiceInputButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else{
                        //저장하고나면 upload 시키자자
                        TransferObserver Observer = transferUtilityaudio.upload(
                                "food-analysis/audioFile",
                                "foodname.mp4",
                                file
                                //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                        );
                        Toast.makeText(AddDietActivity.this, "Success to upload",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    save();


                    //저장하고나면 upload 시키자자
                    TransferObserver Observer = transferUtilityaudio.upload(
                            "food-analysis/audioFile",
                            "foodname.mp4",
                            file
                    );
                    Toast.makeText(AddDietActivity.this, "Success to upload",Toast.LENGTH_LONG).show();

                }

            }

        });


    }
// 텍스트 입력 관련 함수들
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                //if request if cancelled,
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(text);
                }
                else{
                    Toast.makeText(AddDietActivity.this,"Storage permissions ",Toast.LENGTH_SHORT).show();
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

            Toast.makeText(AddDietActivity.this,fileName+" is saved"+dir,Toast.LENGTH_SHORT).show();

        }catch (Exception e){

            Toast.makeText(AddDietActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    //음성입력 관련 함수 SAVE

    public Uri save(){
        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "Recorded");
        values.put(MediaStore.Audio.Media.ALBUM, "Audio_Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Ton");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4"); // 미디어 파일의 포맷
        values.put(MediaStore.Audio.Media.DATA, fileName); // 저장된 녹음 파일

        // ContentValues 객체를 추가할 때, 음성 파일에 대한 내용 제공자 URI 사용
        return getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    }















    //---------------  음성입력 구현 (위에는 텍스트입력)----------








}

