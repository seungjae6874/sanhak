package com.example.chm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

//식단 삭제 액티비티이고 -> 식단 삭제 버튼을 홈에서 누르면 여기로 이동해서 삭제가 이루어진다.
// 식단 삭제 버튼 누르면 -> dialog 떠서
public class DeleteActivity extends AppCompatActivity {
    String foodd,meald,querydated,ksum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent(); //Intent수신
        Bundle bundle = intent.getExtras();

        foodd = bundle.getString("deletef");
        meald = bundle.getString("deletem");//권장 칼로리
        querydated = bundle.getString("querydate");//권장 칼로리

        ksum = bundle.getString("rkcal");

        Request4Class request = new Request4Class(querydated, meald, foodd);

        CognitoCachingCredentialsProvider cognitoProvider4 = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:b36f878a-998b-4e06-9aeb-de1a87f2ab3f", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        LambdaInvokerFactory factory4 = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider4);
        final MyInterface4 myInterface4 = factory4.build(MyInterface4.class);


        new AsyncTask<Request4Class, Void, Response4Class>() {
            @Override
            protected Response4Class doInBackground(Request4Class... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface4.DeleteFood2(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    //Toast.makeText(DeleteActivity.this, "선택한 날짜에 정보가 없습니다!",Toast.LENGTH_LONG).show();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Response4Class result) {
                if (result == null) {
                    Toast.makeText(DeleteActivity.this, "삭제 성공",Toast.LENGTH_LONG).show();
                    return;
                }



                //Toast.makeText(MainActivity.this, result.getFeedback(), Toast.LENGTH_LONG).show();
            }
        }.execute(request);

        Intent intentD = new Intent(DeleteActivity.this, HomeActivity.class);
        intentD.putExtra("rkcal",ksum);

        startActivity(intentD);




    }
}
