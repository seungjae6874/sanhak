package com.example.chm;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    MediaRecorder recorder;
    String fileName;
    MediaPlayer mediaPlayer;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);
        //AWS AudioFile 업로드 하기

        //aws에 접근하기위한 정보들
        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "ap-northeast-2:900a2186-f67e-463d-9644-868a044ffaf5",
                Regions.AP_NORTHEAST_2
        );
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        final TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());


        //
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/My AudioFiles/");
        dir.mkdirs();

        final File file = new File(dir, "foodname.mp4");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴
        Toast.makeText(getApplicationContext(), "파일 위치:"+fileName, Toast.LENGTH_SHORT).show();

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
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


        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 재생
                try {
                    if(mediaPlayer != null){    // 사용하기 전에
                        mediaPlayer.release();  // 리소스 해제
                        mediaPlayer = null;
                    }
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(fileName); // 음악 파일 위치 지정
                    mediaPlayer.prepare();  // 미리 준비
                    mediaPlayer.start();    // 재생
                    Toast.makeText(getApplicationContext(), "재생시작", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //업로드 버튼 누르면 mp3 파일을 aws로 전송한다.
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
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
                        TransferObserver Observer = transferUtility.upload(
                                "food-analysis/audioFile",
                                "foodname.mp4",
                                file
                                //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                        );
                        Toast.makeText(RecordActivity.this, "Success to upload",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    save();


                    //저장하고나면 upload 시키자자
                    TransferObserver Observer = transferUtility.upload(
                            "food-analysis/audioFile",
                            "foodname.mp4",
                            file
                    );
                    Toast.makeText(RecordActivity.this, "Success to upload",Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    // 녹음한 파일 저장
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
}

