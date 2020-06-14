package com.example.androideventgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.lambdainvoker.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
public class FeedbackActivity extends AppCompatActivity {

    String diet;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //홈에서 피드백 받고싶은 날짜를 인텐트로 받아옴
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras(); //홈에서 보낸 값을 담는 번들
        //이 값이 쿼리로 이용할 날짜값
        final String checkdate = bundle.getString("checkdate");

        //---------------------------

        show = findViewById(R.id.result);


        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                this.getApplicationContext(),
                "ap-northeast-2:de25ebac-0c12-4991-9bb9-a95df04a6441",
                Regions.AP_NORTHEAST_2);

        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final MyInterface myInterface = factory.build(MyInterface.class);

        //나중에 checkdate는 날짜를 선택했을때의 피드백 날짜이다.
        RequestClass request = new RequestClass(checkdate);

        new AsyncTask<RequestClass, Void, ResponseClass>() {
            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.diet_rd_n_cal(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                if (result == null) {
                    return;
                }

                // Do a toast
                diet = result.getFeedback();
                show.setText(diet);
                //Toast.makeText(MainActivity.this, result.getFeedback(), Toast.LENGTH_LONG).show();
            }
        }.execute(request);


    }
}
