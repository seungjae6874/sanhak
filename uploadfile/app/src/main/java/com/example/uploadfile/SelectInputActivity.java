package com.example.uploadfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectInputActivity extends AppCompatActivity {
    Button text,record;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textinput);
        record = findViewById(R.id.record);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectInputActivity.this, TextActivity.class);
                startActivity(intent);

            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SelectInputActivity.this, RecordActivity.class);
                startActivity(intent2);
            }
        });



    }
}
