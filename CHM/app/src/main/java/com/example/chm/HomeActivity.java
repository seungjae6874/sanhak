package com.example.chm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActvity";
    private CalendarView calendarView;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    TextView diettable;
    TextView date;
    TextView check,check2;

    Button AddDiet;
    Button EditDiet;
    Button feed;
    String checkdate;
    String ksum;
    int feedy,feedm,feedd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homecalendar);
        //----------------------------------LoginResultActivity에서 보낸 Intent---------------------//

        this.setCheckdateView();
        this.InitializeListener();

        Intent intent = getIntent(); //Intent수신
        Bundle bundle = intent.getExtras();

        AddDiet = findViewById(R.id.AddDiet); // 버튼 추가
        EditDiet = findViewById(R.id.EditDiet);
        feed = findViewById(R.id.CheckFeed);
        check = findViewById(R.id.checkd);
        check2 = findViewById(R.id.checkd2);
        ksum = bundle.getString("rkcal");//권장 칼로리

        date = findViewById(R.id.date); //날짜가 표시될 텍스트
        SimpleDateFormat initdate = new SimpleDateFormat( "yyyy년 MM월 dd일의 식단"); //TextView의 date현재날짜로 초기화
        String format_time1 = initdate.format (System.currentTimeMillis());
        date.setText(format_time1);


        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView); //캘린더뷰 띄움
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { // 클릭시 반응하는 리스너 추가
            @Override
            public void onSelectedDayChange(/*@NonNull*/ CalendarView calendarView, int year, int month, int dayOfMonth) {
                String dates = year + "년 " + (month+1) + "월 " + dayOfMonth + "일의 식단"; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴
                //왜인지 모르겠지만 월이 안맞음 month+1로 수정
                //애뮬레이터가 미국시간이라 미국시간으로 출력하는것 같다. .
                Log.d(TAG, "onSelectedDayChange: date: " + dates);
                date.setText(dates);// 해당 날짜를 TextView에 표시


            }
        });

        AddDiet.setOnClickListener(new View.OnClickListener() { // 식단추가 액티비티 연결
            @Override
            public void onClick(View view) {
                // home으로 보내는 Intent
                Intent intentA = new Intent(HomeActivity.this, AddDietActivity.class);
                startActivity(intentA);
                //finish(); // 현재 액티비티 없애고 다른 액티비티를 띄운다. 아래링크 대로
                //https://hashcode.co.kr/questions/3484/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%99%94%EB%A9%B4-%EC%A0%84%ED%99%98-%EC%A4%91-%EC%97%90%EB%9F%AC

            }
        });
        //----------------------------------------------------------------------
        //피드백 체크 버튼 눌렸을 때 -> 달력 뜨고 날짜 선택 -> 해당 날짜를 가지고 aws로 feed 요청 checkdate라는 값으로
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(HomeActivity.this, callbackMethod,2020,5,15);
                dialog.show();


                Handler handler = new Handler();
                //check2.setText(checkdate);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 체크데이트를 aws로 보내주자
                        //checkdate가 null이 아니면

                        Intent intentC = new Intent(HomeActivity.this, FeedbackActivity.class);
                        checkdate = check.getText().toString();
                        intentC.putExtra("rkcal",ksum);
                        intentC.putExtra("checkdate",checkdate);
                        //우선 이 값을 intent로 새로운 feedresultactivity로 보내자
                        startActivity(intentC);
                    }
                },7000);



            }
        });


    }

    public void setCheckdateView(){
        check = findViewById(R.id.checkd);

    }

    public void InitializeListener(){
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //check.setText(String.valueOf(year+month+dayOfMonth));
                check.setText(String.valueOf(year-2000)+'0'+String.valueOf(month+1)+String.valueOf(dayOfMonth));




            }
        };
    }


}