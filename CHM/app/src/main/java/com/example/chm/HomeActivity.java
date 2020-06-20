package com.example.chm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

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

    TextView food1,food2,food3;
    TextView morning, lunch, dinner,snack,food4;


    Button AddDiet,update;
    Button EditDiet;
    Button feed;
    String f1,f2,f3,f4,meal1,meal2,meal3,meal4;
    String checkdate,querydate;
    String ksum;
    String addfoodname,addmeal; //식단 추가를 통해 받아온 음식 이름이다.
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
        feed = findViewById(R.id.CheckFeed);
        check = findViewById(R.id.checkd);
        check2 = findViewById(R.id.checkd2);
 //       ksum = bundle.getString("rkcal");//권장 칼로리
    //    addfoodname = bundle.getString("Foodname");
        update = findViewById(R.id.update); //식단 정보 갱신



        //오늘 먹은 식단을 diettable에 넣어줘야 한다.
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:18f29d04-451e-4051-a2c4-8c7fdc8c960f", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        //음식 정보 받아오기 시간, 음식이름

        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);
        final MyInterface2 myInterface2 = factory.build(MyInterface2.class);

        //




        //1. xml과 연결
        food1 = findViewById(R.id.st_foodname);
        morning = findViewById(R.id.st_meal);
        food2 = findViewById(R.id.nd_foodname);
        lunch = findViewById(R.id.nd_meal);
        food3 = findViewById(R.id.rd_foodname);
        dinner = findViewById(R.id.rd_meal);
        food4 = findViewById(R.id.ld_foodname);
        snack = findViewById(R.id.ld_meal);
        //2. aws에서 먹은 음식과 끼니시간을 aws에서 받아서 setText해주기




        //



        date = findViewById(R.id.date); //날짜가 표시될 텍스트
        SimpleDateFormat initdate = new SimpleDateFormat( "yyyy년 MM월 dd일의 식단"); //TextView의 date현재날짜로 초기화
        SimpleDateFormat initdate2 = new SimpleDateFormat("yyyyMMdd");
        String format_time1 = initdate.format (System.currentTimeMillis());
        //querydate = initdate2.format(System.currentTimeMillis());
       // querydate = querydate.substring(2,querydate.length());
        date.setText(format_time1);




        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView); //캘린더뷰 띄움
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { // 클릭시 반응하는 리스너 추가
            @Override
            public void onSelectedDayChange(/*@NonNull*/ CalendarView calendarView, int year, int month, int dayOfMonth) {
                String date2;
                String dates = year + "년 " + (month+1) + "월 " + dayOfMonth + "일의 식단"; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴
                //왜인지 모르겠지만 월이 안맞음 month+1로 수정
                if((month+1 <10)&&(dayOfMonth<10)){
                    date2 = (year-2000) + "0" + (month+1) + "0" + dayOfMonth; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴

                }
                else if((month+1 <10)&&(dayOfMonth>=10)){
                    date2 = (year-2000) + "0" + (month+1) + "" + dayOfMonth; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴
                }
                else if((month+1 >=10)&&(dayOfMonth<10)){
                    date2 = (year-2000) + "" + (month+1) + "0" + dayOfMonth; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴
                }
                else{
                    date2 = (year-2000) + "" + (month+1) + "" + dayOfMonth + ""; //캘린더에 날짜를 클릭하면 해당 날짜의 년,월,일 받아옴

                }
                querydate = date2;
                //애뮬레이터가 미국시간이라 미국시간으로 출력하는것 같다. .
                Log.d(TAG, "date: " + date2);
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aws 연결 시작
                Request2Class request = new Request2Class(querydate);
                //Log.d(TAG, "querydate: " + querydate);

                new AsyncTask<Request2Class, Void, Response2Class>() {
                    @Override
                    protected Response2Class doInBackground(Request2Class... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return myInterface2.getFoodinfo(params[0]);
                        } catch (LambdaFunctionException lfe) {
                            Log.e("Tag", "Failed to invoke echo", lfe);
                            Toast.makeText(HomeActivity.this, "선택한 날짜에 정보가 없습니다.",Toast.LENGTH_LONG).show();
                            return null;
                        }

                    }

                    @Override
                    protected void onPostExecute(Response2Class result) {
                        if (result == null) {
                            Toast.makeText(HomeActivity.this, "선택한 날짜에 정보가 없습니다.",Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Do a toast
                        f1 = result.getfood1();
                        meal1 = result.getMeal1();
                        f2 = result.getfood2();
                        meal2 = result.getMeal2();
                        f3 = result.getfood3();
                        meal3 = result.getMeal3();
                        f4 = result.getfood4();
                        meal4 = result.getMeal4();
                        food1.setText(f1);
                        morning.setText(meal1);
                        food2.setText(f2);
                        lunch.setText(meal2);
                        food3.setText(f3);
                        dinner.setText(meal3);
                        food4.setText(f4);
                        snack.setText(meal4);
                        //이제 이 list를 Home으로 넘겨주자

                        //Toast.makeText(MainActivity.this, result.getFeedback(), Toast.LENGTH_LONG).show();
                    }
                }.execute(request);


                //연결 끝
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