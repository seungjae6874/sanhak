package com.example.chm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class AddDietActivity extends AppCompatActivity
{
    TextView TextV1,TextV2;
    EditText editTextInput;
    Button buttonGetEditTextInput;
    public int yearSelected, monthSelected, daySelected;  //선택된 년/월/일 각각 저장


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddiet);
        TextV1 = findViewById(R.id.TextV1);
        TextV2 = findViewById(R.id.TextV2);
        editTextInput = findViewById(R.id.editTextInput);
        buttonGetEditTextInput = findViewById(R.id.buttonGetEditTextInput);

        //final Spinner spinner = findViewById(R.id.spinner);

        Intent Intent = getIntent();// 인덴트 받아오고 시작


        /*spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                term[0] = spinner.getSelectedItem().toString();

            }
        });*/ //일단 스피너 구현안할거임 text받는것이 우선이기도하고 날짜 눌러서 입력하는데 굳이 입력받게끔 할 필요가 있을까?? (편의성을 위한다면 추가하면 좋을듯)

        buttonGetEditTextInput.setOnClickListener(new View.OnClickListener() { // 맨밑 확인 버튼 눌렀을때 음식이름 인텐트에 담아 저장
            @Override
            public void onClick(View v) {
                //여기서 String으로 수정된 정보들을 getText로 받아서 다시 선언
                String FoodName = editTextInput.getText().toString(); //음식이름을 받는 Edit TextIneput

                if ((editTextInput.length() <= 0)) {
                    Toast.makeText(AddDietActivity.this, "음식이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    //Intent로 이제 음식이름을 넘겨주자
                    Intent intentFoodName = new Intent(AddDietActivity.this, LoginResultActivity.class);
                    intentFoodName.putExtra("mFoodName", FoodName);

                    startActivity(intentFoodName);

                }
            }
        });




    }
}

