package com.example.chm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText name,age,height,weight,periodnumber;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        periodnumber = findViewById(R.id.periodnumber);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        button = findViewById(R.id.login);
        final Spinner spinner = findViewById(R.id.period);
        final String[] term = new String[1]; //개월인지 주 인지 일인지

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                term[0] = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭했을때
                String user = name.getText().toString(); //사용자 이름
                String userage = age.getText().toString();//사용자 나이
                String userheight = height.getText().toString();//사용자 키
                String userweight = weight.getText().toString();//사용자 몸무게
                String userperiod = periodnumber.getText().toString() + term[0]; //식단기간
                if(periodnumber.getText().toString() == String.valueOf('0')){
                    userperiod = "일일 관리";
                }

                //Intent로 이제 다음 화면에 넘겨주자 이 값들이 프로필로써 사용되므로
                Intent intent = new Intent(MainActivity.this, LoginResultActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("userage",userage);
                intent.putExtra("userheight",userheight);
                intent.putExtra("userweight",userweight);
                intent.putExtra("userperiod",userperiod);
                startActivity(intent);
            }
        });

    }
}
