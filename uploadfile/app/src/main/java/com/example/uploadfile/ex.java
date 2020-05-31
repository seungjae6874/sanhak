package com.example.uploadfile;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.amazonaws.mobileconnectors.s3.transferutility.*;
public class ex extends AppCompatActivity {
    private String TAG = "MainActivity";
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    File f;
    TransferUtility transferUtility;
    File path = Environment.getExternalStorageDirectory();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
    String fileName = "MyFile_"+timeStamp+".txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "ap-northeast-2:0736fa12-bb2d-489c-90e5-460af3a31e38",
                Regions.AP_NORTHEAST_2
        );
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

    }
    //@Override
    //public void onClick(View view) {
     //   switch (view.getId()) {
      //      case R.id.uploadBtn ;
                TransferObserver Observer = transferUtility.upload(
                        "aws-anlaysis-text",
                        "",
                        path
                );
       //         break;

        //}


}

