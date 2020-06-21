package com.example.chm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

public class FeedbackActivity extends AppCompatActivity {

    String diet;
    TextView show;
    TextView cdate;
    TextView feedresult;
    ImageView image;
    Button score;
    double eatkcal = 0;
    double criterion = 0;
    double minus = 0;


    String eatsum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbackcalendar);

        //홈에서 피드백 받고싶은 날짜를 인텐트로 받아옴
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras(); //홈에서 보낸 값을 담는 번들
        //이 값이 쿼리로 이용할 날짜값
        String querydate = bundle.getString("checkdate");
        String ksum = bundle.getString("rkcal");
        String weight = bundle.getString("rweight");

        //---------------------------

        show = findViewById(R.id.result);
        cdate = findViewById(R.id.cdate);
        image = findViewById(R.id.imgfeed);

        //feedback 결과
        score = findViewById(R.id.button2);
        feedresult = findViewById(R.id.feedresult);
        //cdate.setText(ksum);

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                this.getApplicationContext(),
                "ap-northeast-2:de25ebac-0c12-4991-9bb9-a95df04a6441",
                Regions.AP_NORTHEAST_2);

        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final MyInterface myInterface = factory.build(MyInterface.class);

        //나중에 checkdate는 날짜를 선택했을때의 피드백 날짜이다.
        RequestClass request = new RequestClass(querydate, ksum);
        //Log.d("date" ,querydate);
        Log.d("ksum",ksum);
        //RequestClass request2 = new RequestClass(querydate, ksum);
        new AsyncTask<RequestClass, Void, ResponseClass>() {
            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.diet_rd_n_cal(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    Toast.makeText(FeedbackActivity.this, "선택한 날짜에 정보가 없습니다.",Toast.LENGTH_LONG).show();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                if (result == null) {
                    Toast.makeText(FeedbackActivity.this, "선택한 날짜에 정보가 없습니다.",Toast.LENGTH_LONG).show();
                    return;
                }

                // Do a toast
                diet = result.getFeedback();
                eatsum = result.getEatsum();
                show.setText(diet);
                //Toast.makeText(MainActivity.this, result.getFeedback(), Toast.LENGTH_LONG).show();
            }
        }.execute(request);


        //점수보기 버튼 누르면 피드백 계산 후에 제공
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = getIntent();

                Bundle bundle2 = intent2.getExtras(); //홈에서 보낸 값을 담는 번들
                //이 값이 쿼리로 이용할 날짜값
                //String querydate = bundle2.getString("checkdate");

                String ksum2 = bundle2.getString("rkcal");

                criterion = Integer.parseInt(ksum2);
                eatkcal = Integer.parseInt(eatsum); //먹은 칼로리를 얻고 ksum과의 차이로 피드백 계산하자


                minus = eatkcal-criterion;
                double realm = Math.abs(minus);
                double result = (realm/criterion)*100;
                String minus1 = String.valueOf(result);
                Log.d("minus1 ", minus1);

                //feedresult.setText("A등급\n 균형 잡힌 식사를 잘 하고 있습니다!");
                //image.setImageResource(R.drawable.agrade);

                if(result <= 10){ //A등급
                    feedresult.setText("A등급\n 균형 잡힌 식사를 잘 하고 있습니다!");
                    image.setImageResource(R.drawable.agrade);
                }

                else if((result <= 30)&&(result > 10)){ //B등급
                    feedresult.setText("B등급\n 조금만 식단에 더 신경쓴다면 \n균형잡힌 식습관을 가질 수 있어요!");
                    image.setImageResource(R.drawable.bgrade);
                }

                else if(result >30){ //C등급
                    feedresult.setText("D등급\n 불균형한 식습관입니다.\n 조언에 맞는 식단 관리가 필요합니다!");
                    image.setImageResource(R.drawable.cgrade);
                }

            }
        });

    }
}
