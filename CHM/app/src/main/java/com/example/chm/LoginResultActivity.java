package com.example.chm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
//로그인 후 넘어오게되는 프로필 화면
import androidx.appcompat.app.AppCompatActivity;

public class LoginResultActivity extends AppCompatActivity {

    TextView userprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userprofile = findViewById(R.id.userprofile);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras(); //Main에서 보낸 값을 담는 번들
        String username = bundle.getString("user");
        String userage = bundle.getString("userage");
        String userheight = bundle.getString("userheight");
        String userweight = bundle.getString("userweight");
        String userperiod = bundle.getString("userperiod");

        userprofile.setText("사용자 : "+username+'\n'+ "나이 : "+userage+'\n'+
                "키 : "+userheight+"\n"+"체중 : "+userweight+'\n'+
                "식단 조절 기간 : "+userperiod);

    }
}
