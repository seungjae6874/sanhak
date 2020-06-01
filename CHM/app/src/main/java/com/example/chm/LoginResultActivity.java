package com.example.chm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
//로그인 후 넘어오게되는 프로필 화면
import androidx.appcompat.app.AppCompatActivity;

public class LoginResultActivity extends AppCompatActivity {

    //선언 사용자이름, 기간, 문구 조언
    TextView usernamed;
    TextView userprofile;
    TextView userperioded;
    TextView message;
    //다음 식단 수정창 or 프로필 수정창으로 이동
    Button modify;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //activity main xml에서 이제 사진, 이름 , 식단 설정 기간, 건강 식단 문구 조언 그리고
        //프로필 수정, 식단관리 시작 버튼 을 만든다.

        //userprofile = findViewById(R.id.userprofile);
        usernamed = findViewById(R.id.username);
        userperioded = findViewById(R.id.period);
        message = findViewById(R.id.startmessage);
        modify = findViewById(R.id.modify);
        start = findViewById(R.id.start);

        //-----------------이건 시작 화면에서 프로필 초기 생성 시 보내준 정보 --------------------
        // 메인에서 보내는 intent와 login에서 보내는 intent를 갖게 보내면?
        Intent intent = getIntent();

        //한번 모든 loginresult 액티비티가 받는 인텐드 메시지를 같게해보자 m붙여서
        Bundle bundle = intent.getExtras(); //Main에서 보낸 값을 담는 번들
        //이 값이 프로필 초기 설정 후에 저장된 정보
        final String username = bundle.getString("musername");
        final String userage = bundle.getString("muserage");
        final String userheight = bundle.getString("muserheight");
        final String userweight = bundle.getString("muserweight");
        final String userperiod = bundle.getString("muserperiod");
        final String userperiodnumber = bundle.getString("muserperiodnumber");
/*
*/
        //메인에서 넘어올 때
        //1. 사용자  이름을 띄워주기
        usernamed.setText(username+"님");
        //2. 식단 기간 띄워주기
        userperioded.setText("기간 : "+userperiod);
        //3. 건강한 식단 문구 띄워주기
        Animation anim = AnimationUtils.loadAnimation(LoginResultActivity.this, R.anim.alpha_anim);
        message.startAnimation(anim);

        //프로필 수정창으로 이동 할 경우
        final boolean modifycheck = false;
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = getIntent();
                Bundle bundle2 = intent2.getExtras();
                String username = bundle2.getString("musername");
                String username1 = username;//name을 다시 변환

                //여기서 다시 수정 버튼 눌렸을 때 값이 null 아니라 최근 값으로 들어가야한다.
                Intent intent = new Intent(LoginResultActivity.this, ModifyProfileActivity.class);
                intent.putExtra("username",username1);
                intent.putExtra("userage",userage);
                intent.putExtra("userheight",userheight);
                intent.putExtra("userweight",userweight);
                intent.putExtra("userperiod",userperiod);
                intent.putExtra("userperiodnumber",userperiodnumber);//기간에서 문자 뺴고 숫자만 보내기
                intent.putExtra("modifycheck",modifycheck);//프로필 수정했다고 확인
                startActivity(intent);



            }
        });


        //여기서 받고 돌아오면 새 값으로 정보를 수정하자
        //------------------------------------------------------------
        //여기가 프로필 수정 후에 받는 정보
        Intent intent3 = getIntent();
        Bundle bundle2 = intent3.getExtras();
        final String username2 = bundle2.getString("musername");
        final String userage2 = bundle2.getString("muserage");
        final String userheight2 = bundle2.getString("muserheight");
        final String userweight2 = bundle2.getString("muserweight");
        final String userperiod2 = bundle2.getString("muserperiod");
        final String userperiodnumber2 = bundle2.getString("muserperiodnumber");
        final Boolean getcheck = bundle2.getBoolean("responsecheck");
        //1. 사용자  이름을 띄워주기
        if(getcheck != modifycheck){
            usernamed.setText(username2+"님");
            //2. 식단 기간 띄워주기
            userperioded.setText("기간 : "+userperiod2);
            //3. 건강한 식단 문구 띄워주기
            Animation anim2 = AnimationUtils.loadAnimation(LoginResultActivity.this, R.anim.alpha_anim);
            message.startAnimation(anim);


        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // home으로 보내는 Intent
                Intent intentH = new Intent(LoginResultActivity.this, HomeActivity.class);
                startActivity(intentH);
                //finish(); // 현재 액티비티 없애고 다른 액티비티를 띄운다. 아래링크 대로
                //https://hashcode.co.kr/questions/3484/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%99%94%EB%A9%B4-%EC%A0%84%ED%99%98-%EC%A4%91-%EC%97%90%EB%9F%AC

            }
        });


        //한번 사용자 이름과 식단기간을 s3 버킷에 보내보자
    }
}
