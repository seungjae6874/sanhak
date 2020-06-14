package com.example.chm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectInputActivity extends AppCompatActivity {
    Button text,record,feed;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_main);
        text = findViewById(R.id.textinput);
        record = findViewById(R.id.record);


        //식단 추가 후 입력 버튼 눌렀을 때
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectInputActivity.this, "음식 입력 완료", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SelectInputActivity.this, TextActivity.class);
                startActivity(intent);

            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectInputActivity.this, "음식 입력 완료", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(SelectInputActivity.this, RecordActivity.class);
                startActivity(intent2);
            }
        });




    }
}
