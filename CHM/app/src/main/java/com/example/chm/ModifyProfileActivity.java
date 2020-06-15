package com.example.chm;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

//프로필 수정 버튼 누르면 이 곳으로 이동 인텐트를 넘겨 받고 여기서 다시 프로필 화면으로 intent를 다시 넘겨주어서 수정하면 된다.
public class ModifyProfileActivity extends AppCompatActivity {
    EditText name,age,height,weight,periodnumber;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras(); //Main에서 보낸 값을 담는 번들
        //이 값이 프로필 초기 설정 후에 저장된 정보
        final String username = bundle.getString("username");
        final String userage = bundle.getString("userage");
        final String userheight = bundle.getString("userheight");
        final String userweight = bundle.getString("userweight");
        final String userperiod = bundle.getString("userperiod");
        final String userperiodnumber = bundle.getString("userperiodnumber");
        final boolean modifycheck2 = bundle.getBoolean("modifycheck");
        final String useractive = bundle.getString("useractive");

        Resources res = getResources();
        String changename = String.format(res.getString(R.string.username),username);
        String changeage = String.format(res.getString(R.string.username),userage);
        String changeheight = String.format(res.getString(R.string.username),userheight);
        String changeweight = String.format(res.getString(R.string.username),userweight);
        String changeperiodnumber = String.format(res.getString(R.string.userperiodfull),userperiodnumber);
        String changeperiod = String.format(res.getString(R.string.username),userperiod);
        //---------------------------먼저 intent 넘겨 받음-------------------------------

        periodnumber = findViewById(R.id.periodnumber);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        button = findViewById(R.id.login);
        name.setText(changename);
        age.setText(changeage);
        height.setText(changeheight);
        weight.setText(changeweight);
        periodnumber.setText(changeperiodnumber);
        //여기까지는 이전에 기록했던 프로필을 먼저 hint로 보여주는것
        final Spinner spinner = findViewById(R.id.period);
        final String[] term = new String[1]; //개월인지 주 인지 일인지
        final Spinner spinner2 = findViewById(R.id.active); //활동지수

        final String[] active = new String[1]; //활동지수를 받는 배열

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                term[0] = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                active[0] = spinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //여기서 String으로 수정된 정보들을 getText로 받아서 다시 선언
                String username1 = name.getText().toString();
                String userage1 = age.getText().toString();
                String userheight1 = height.getText().toString();
                String userweight1 = weight.getText().toString();
                String userperiod1 = periodnumber.getText().toString()+term[0];
                String userperiodnumber1 = periodnumber.getText().toString();
                String useractivenum = active[0];
                boolean modifycheckfinal = true;
                //다시 프로필 창으로 정보 INTENT로 보내기, 프로필 창 다시 수정해줘야함.

                //------------------------------------------------------------
                //이 값이 최신으로 바뀌는 프로필 정보값이다.
                Intent intent2 = new Intent(ModifyProfileActivity.this, LoginResultActivity.class);
                intent2.putExtra("musername", username1);
                intent2.putExtra("muserage", userage1);
                intent2.putExtra("muserheight", userheight1);
                intent2.putExtra("muserweight", userweight1);
                intent2.putExtra("muserperiod",userperiod1);
                intent2.putExtra("muserperiodnumber",userperiodnumber1);//기간에서 문자 뺴고 숫자만 보내기
                intent2.putExtra("responsecheck",modifycheckfinal); //수정 확인 응답
                intent2.putExtra("useractive",useractivenum);
                startActivity(intent2);

            }
        });

    }

}
