package com.example.chm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import java.io.File;
import java.io.IOException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.text.SimpleDateFormat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddDietActivity extends AppCompatActivity
{
    TextView TextV1,TextV2;
    EditText editTextInput;
    Button textInputButton,voiceInputButton,resetButton ;
    ImageButton voiceRecordButton;
    String text;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    File foodfilepath;
    TransferUtility transferUtilitytext;
    TransferUtility transferUtilityaudio;

    MediaRecorder recorder;
    String fileName;
    MediaPlayer mediaPlayer;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    int position = 0;
    @Override



    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfood);

        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "ap-northeast-2:0736fa12-bb2d-489c-90e5-460af3a31e38",
                Regions.AP_NORTHEAST_2
        );
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");




        TextV1 = findViewById(R.id.textView1);
        TextV2 = findViewById(R.id.textView2);
        final AutoCompleteTextView editTextInput = (AutoCompleteTextView) findViewById(R.id.editText);  // Text입력 받는 곳  // Text입력 받는 곳
        textInputButton = findViewById(R.id.TextInputButton); // 텍스트 입력완료
        voiceInputButton = findViewById(R.id.VoiceInputButton); // 녹음 버튼 누르면 입력
        voiceRecordButton = findViewById(R.id.VoiceRecordButton); // 입력 완료 버튼튼

        editTextInput.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,items));


        final TransferUtility transferUtilitytext = new TransferUtility(s3, getApplicationContext());
        final TransferUtility transferUtilityaudio = new TransferUtility(s3, getApplicationContext());

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/My AudioFiles/");
        dir.mkdirs();

        final File file = new File(dir, "foodname.mp4");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴
        Toast.makeText(getApplicationContext(), "파일 위치:"+fileName, Toast.LENGTH_SHORT).show();

        Intent Intent = getIntent();// 인덴트 받아오고 시작

        // 여기는 이제 음식텍스트로 입력
        textInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDietActivity.this);
                String TAG = "이(가) 맞습니까";
                builder.setTitle("확인창");

                text = editTextInput.getText().toString().trim();
                builder.setMessage(text + TAG);

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "해당음식을 기록합니다", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                //show popup for runtime permission
                                requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
                            } else {//음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자.
                                //permission already granted
                                saveToTxtFile(text);
                                //저장하고나면 upload 시키자자
                                TransferObserver Observer = transferUtilitytext.upload(
                                        "food-anlaysis-text",
                                        "foodname.txt",
                                        foodfilepath
                                        //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                                );
                                Toast.makeText(AddDietActivity.this, "Success to upload", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            saveToTxtFile(text);


                            //저장하고나면 upload 시키자자
                            TransferObserver Observer = transferUtilitytext.upload(
                                    "food-anlaysis-text",
                                    "textfile",
                                    foodfilepath
                            );
                            Toast.makeText(AddDietActivity.this, "Success to upload", Toast.LENGTH_LONG).show();

                            Intent intentH2 = new Intent(AddDietActivity.this, HomeActivity.class);
                            intentH2.putExtra("FoodName",text);
                            startActivity(intentH2);


                        }

                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();




                    }
                });




                if (text.isEmpty()) { //비어있다면
                    Toast.makeText(AddDietActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                } else { //음식이 입력된다면
                    builder.show();


                }
            }
        });



        // 음성입력

        //녹음 시작
        voiceRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 시작
                if (recorder == null) {
                    recorder = new MediaRecorder(); // 미디어리코더 객체 생성
                }
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 입력 지정(마이크)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);    // 출력 형식 지정
                //마이크로 들어오는 음성데이터는 용량이 크기 때문에 압축이 필요
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   // 인코딩
                recorder.setOutputFile(fileName);  // 음성 데이터를 저장할 파일 지정
                try {
                    recorder.prepare();
                    recorder.start();
                    Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        // 녹음 종료
        findViewById(R.id.ResetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 종료
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }





                Toast.makeText(getApplicationContext(), "녹음종료", Toast.LENGTH_SHORT).show();
            }
        });


        // AWS에 음성전송
        findViewById(R.id.VoiceInputButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else{
                        //저장하고나면 upload 시키자자
                        TransferObserver Observer = transferUtilityaudio.upload(
                                "food-analysis/audioFile",
                                "foodname.mp4",
                                file
                                //음식이름을 입력한 txt를 저장하고 나서 aws에 업로드하자
                        );
                        Toast.makeText(AddDietActivity.this, "Success to upload",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    save();


                    //저장하고나면 upload 시키자자
                    TransferObserver Observer = transferUtilityaudio.upload(
                            "food-analysis/audioFile",
                            "foodname.mp4",
                            file
                    );
                    Toast.makeText(AddDietActivity.this, "Success to upload",Toast.LENGTH_LONG).show();

                }

            }

        });


    }
// 텍스트 입력 관련 함수들
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                //if request if cancelled,
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(text);
                }
                else{
                    Toast.makeText(AddDietActivity.this,"Storage permissions ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveToTxtFile(String text){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        try{
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path+"/My Files/");

            dir.mkdirs();
            String fileName = "MyFile_"+timeStamp+".txt";

            File file = new File(dir,fileName);
            foodfilepath = file;

            //FileWriter class is use to store file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();

            Toast.makeText(AddDietActivity.this,fileName+" is saved"+dir,Toast.LENGTH_SHORT).show();

        }catch (Exception e){

            Toast.makeText(AddDietActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    //음성입력 관련 함수 SAVE

    public Uri save(){
        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "Recorded");
        values.put(MediaStore.Audio.Media.ALBUM, "Audio_Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Ton");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4"); // 미디어 파일의 포맷
        values.put(MediaStore.Audio.Media.DATA, fileName); // 저장된 녹음 파일

        // ContentValues 객체를 추가할 때, 음성 파일에 대한 내용 제공자 URI 사용
        return getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    }















    //---------------  음성입력 구현 (위에는 텍스트입력)----------

    String[] items = { "감자 주먹 밥",  "고추장불고기 삼각김밥",  "곤드레나물밥(정선)",  "국밥(안성)",  "굴",  "김밥",  "김치 고구마 밥",
            "김치김밥",  "김치볶음밥",  "김치쌈밥",  "농어초밥",  "돼지국밥(밀양)",  "두부 채소 볶음밥",  "케밥",  "유산슬덮밥",  "멍게비빔밥(거제)",
            "멸치주먹밥",  "묵밥(영주)",  "문어초밥",  "볶음밥",  "불고기덮밥",  "비빔밥",  "삼선볶음밥",  "새싹 비빔밥",  "새우볶음밥",  "새우초밥",
            "새우튀김롤",  "샐러드김밥",  "생선초밥(광어)",  "생선초밥(모듬)",  "소고기국밥(밥포함)",  "소고기김밥",  "소머리국밥",  "송이덮밥",
            "쇠고기 말이 쌈밥",  "숯불갈비 삼각김밥",  "쌀밥",  "쌈밥",  "알밥",  "약초비빔밥(산청)",  "연어롤",  "연어초밥",  "영양밥",  "오곡밥",
            "오므라이스",  "오징어 덮밥",  "유부초밥",  "육회비빔밥",  "자장밥",  "잡채밥",  "잡탕밥",  "장어덮밥",  "장어초밥",  "제육덮밥",  "짬뽕밥",
            "참치김밥",  "참치덮밥",  "참치마요네즈 삼각김밥",  "충무김밥",  "치즈김밥",  "카레라이스",  "캘리포니아롤",  "콩나물국밥(전주)",  "콩나물해장국",
            "한치초밥",  "해물덮밥",  "해물볶음밥",  "회덮밥",  "고등어 강정",  "곰보빵",  "과일파이",  "과자",  "꽃게강정",  "꽈배기",  "녹차스콘",  "녹차크레페",
            "누룽지 과자",  "다시마칩",  "단호박 경단",  "두부샌드위치",  "라이스페이퍼 새우 롤",  "롤 피자",  "마늘빵",  "만주",  "머핀",  "멸치누룽지과자",  "모닝빵",
            "미니 버거",  "미니타르트",  "바질 고구마 옥수수빵",  "버터크림빵",  "베이글",  "블루베리 치즈무스케익",  "사과와 양파를 곁들인 돼지등심 요리",
            "사과와 크림치즈를 곁들인 토스트",  "생크림케이크",  "센베이",  "수퍼 브리또",  "식빵",  "아보카도 달걀 샌드위치",  "야채빵",  "야채칩",  "양송이버섯, 달걀크레페",
            "잡곡강정",  "잣호두강정",  "찹쌀도우넛",  "채소 커리를 곁들인 팬케익",  "채소 팬케익",  "채소고로케",  "초콜릿케이크",  "치즈케이크",  "카스텔라",  "카스텔라",
            "커리향이 들어간 치킨 샐러드 샌드위치",  "크로와상 샌드위치",  "클럽 샌드위치",  "티라미수",  "페이스트리빵",  "피스타치오 생크림과 산딸기 컵케이크",  "한과, 매작과 ",
            "한과, 산자",  "한과, 송화다식",  "한과, 약과",  "한과, 유과   ",  "한국식 오코노미야끼",  "햄치즈샌드위치",  "현미크레이프",  "호두과자",  "간자장",  "고기만두",
            "고기완자를 곁들인 스웨덴식 국수",  "구슬 파스타",  "군만두",  "굴짬뽕",  "기스면",  "김치라면",  "김치만두",  "김치우동",  "낙지볶음면",  "냉 쌀국수",  "냉김치말이국수",
            "니고랭",  "닭고기가 들어간 땅콩소스 스파게티",  "들깨칼국수",  "떡국",  "떡라면",  "떡만둣국",  "라면",  "짬뽕라면",  "라비올리 크림소스",  "라비올리 토마토소스",  "막국수",
            "만둣국",  "메밀국수",  "물냉면",  "물만두",  "미니그라탕",  "바지락 볶음 면",  "버섯 리조또",  "버섯을 넣은 가지 라자냐",  "비빔국수",  "비빔냉면",  "삼선우동",  "삼선자장면",
            "삼선짬뽕",  "서리태 잣국수",  "수제비",  "스파게티",  "시저 샐러드",  "쌀국수",  "쌀국수",  "알록달록 시금치 당근 수제비",  "열무냉면",  "오일소스스파게티",  "올챙이국수(정선)",
            "우동(일식)",  "우동(중식)",  "울면",  "인도식 만두구이(사모사)",  "자장면",  "잔치국수",  "짬뽕",  "쫄면",  "치즈라면",  "콩국수",  "크림소스스파게티",  "토마토소스스파게티",
            "해물칼국수",  "해물크림소스스파게티",  "해물토마토소스스파게티",  "호박 프리타타",  "호박만두",  "회냉면",  "감자를 곁들인 야채스튜",  "검보스프",  "검은콩 스프",  "게살죽",
            "고구마 타락죽",  "깨죽",  "단호박 두부 포타주",  "단호박 스프",  "닭죽(닭도가니)(성남)",  "대구 크림 스튜",  "대추죽",  "백합죽(부안)",  "새우살을 채운 두부 스프(미네스트로네)",
            "소고기버섯죽",  "송이 미역죽",  "쇠고기 스튜",  "어죽(영동)",  "완두콩 스프",  "잣죽",  "전복죽",  "차가운 당근스프",  "참치죽",  "팥죽",  "해산물을 곁들인 매콤한 토마토 스튜",
            "호박죽",  "흑임자 두부 죽",  "갈낙탕(영암)",  "갈비탕",  "게국지(태안)",  "고등어찌개",  "곰치국(삼척)",  "곰탕",  "굴 두부 국",  "김치국",  "꼬리곰탕",  "꽃게탕",  "낙지탕",
            "미역오이냉국",  "오이냉국",  "달걀국",  "닭곰탕(닭국)",  "대구지리탕",  "도가니탕",  "도토리묵 콩국",  "도토리묵말이(대전)",  "된장국",  "된장국, 근대",  "된장국, 무",
            "된장국, 시금치",  "된장국, 시래기",  "된장국, 쑥",  "된장국, 아욱",  "된장국, 왜된장국, 미소된장국",  "매생이국(장흥)",  "물회(포항)",  "미역 조랭이 떡국",  "미역국, 쇠고기",
            "미역국, 홍합",  "바지락조개국",  "배추 맑은 국",  "버섯 들깨탕",  "복지리탕",  "북어국",  "삼계탕",  "새우 완자탕",  "생선물회",  "선짓국",  "설렁탕",  "소고기무국",  "소고기육개장",
            "소탕        ",  "순대국",  "아구탕",  "어묵국",  "어탕        ",  "연두부 무순 냉국",  "연포탕",  "오리탕",  "오징어국",  "오징어물회(동해)",  "올갱이국(괴산)",  "우거지국",
            "우거지해장국",  "우렁된장국",  "육개장, 닭고기",  "육탕        ",  "전복탕",  "조기매운탕",  "짱뚱어탕(순천)",  "추어탕(남원)",  "탕국         ",  "토란국",  "해물탕",  "황태해장국",
            "감자탕",  "광어매운탕",  "김치찌개",  "내장탕",  "닭볶음탕",  "대구매운탕",  "동태찌개",  "된장찌개",  "두부전골(찌개)",  "버섯찌개",  "부대찌개",  "소고기샤브샤브",  "순두부찌개",
            "알탕",  "우럭매운탕",  "장어탕",  "장어탕",  "전골, 곱창전골",  "청국장찌개",  "추어탕",  "콩비지찌개",  "해산물 샤브샤브",  "금태찜",  "단호박 두부찜",  "단호박 새우 찜",  "달걀찜",
            "닭갈비",  "닭고기 편육",  "대구찜",  "대하찜",  "도미찜",  "동태찜",  "돼지고기 수육",  "등갈비찜(돼지)",  "매운갈비찜(대구중구)",  "문어숙회     ",  "민어찜       ",  "병어찜       ",
            "북어찜",  "붕어찜(경기광주)",  "사태떡찜",  "생선살 완자 찹쌀 찜",  "생선찜 채소말이",  "서양식 달걀찜(시금치 퀴시)",  "소갈비찜",  "쇠고기 감자찜",  "순대 ",  "아구찜",  "아귀찜(마산)",
            "안동찜닭(안동)",  "영양 달걀 찜",  "오징어보쌈과 저나트륨 된장소스",  "오징어순대(속초)",  "전어찜",  "조기찜",  "족발",  "참꼬막       ",  "한방오리백숙",  "해물찜",  "홍어찜",  "가자미구이",
            "갈치구이",  "고등어구이",  "굴비구이",  "꽁치 양념구이",  "꿩불고기(충주)",  "닭고기 바비큐",  "닭꼬치",  "도미구이",  "돼지갈비구이",  "돼지주물럭(돼지갈비)(서울마포)",  "등심 배구이",  "로스트비프",
            "미니 떡갈비",  "밀라노 스타일 포크 커틀렛",  "버섯소스를 곁들인 돼지 등심 스테이크",  "병어구이",  "불고기(광양)",  "오리불고기",  "붕장어구이(소금)(고흥)",  "비프 스트로가노프",  "살사를 곁들인 참치 오븐구이",
            "소곱창구이",  "소불고기",  "소양념갈비구이",  "양념장어구이",  "양송이버섯과 포도주스를 가미한 등심구이",  "왕갈비(양념)(수원)",  "요거트 파슬리소스를 곁들인 닭고기구이",  "임연수구이",  "조기구이, 양념, 조기조림",
            "종이에 싸서 구운 도미(파필로테)",  "중국식 바비큐 폭 립",  "지중해소스를 곁들인 도미스테이크",  "짚불구이곰장어(기장)",  "참깨 오렌지 쇠고기",  "치킨 버거 스테이크",  "카레소스를 얹은 두부 스테이크",
            "케이준 스타일 닭고기 요리",  "해산물 옥수수 오븐구이 프리타타",  "햄버그스테이크",  "황태구이",  "훈제오리",  "가자미전     ",  "감자전",  "계적        ",  "고추전",  "곶감샐러드",  "김치전",  "깻잎전",
            "낙지꼬지     ",  "녹두빈대떡",  "다시마전",  "단호박전",  "달걀말이",  "돔베기적     ",  "동그랑땡",  "동태전",  "돼지고기 찹쌀부침",  "두부 달걀전",  "두부 해산물 꼬치구이",  "두부전",  "떡갈비",
            "마 두부 오징어전",  "미나리전     ",  "배추전",  "버섯전",  "부추전",  "소라산적     ",  "쇠고기산적",  "오징어산적   ",  "치킨 쇠고기 땅콩소스 꼬치",  "케첩완자",  "파전",  "해물파전",  "햄부침",
            "호박전",  "홍어적       ",  "홍합산적     ",  "화양적",  "가지볶음",  "감자볶음",  "건새우볶음",  "고추잡채",  "궁중 떡볶음",  "김자반",  "김치볶음",  "깻잎볶음",  "꽃게볶음",  "낙지볶음",
            "닭갈비(춘천)",  "돼지갈비 볶음",  "돼지고기볶음",  "돼지고기볶음, 오징어",  "돼지고기토마토두부볶음",  "돼지껍데기볶음",  "두부김치",  "떡볶이",  "라볶이",  "라볶이",  "마파두부",  "멸치풋고추볶음",
            "미역줄기볶음",  "버섯볶음, 느타리",  "버섯볶음, 양송이",  "브로콜리쇠고기볶음",  "섭산삼",  "소고기 채소볶음",  "소세지볶음",  "순대볶음",  "시금치돼지고기볶음",  "쌀국수 볶음면",  "아시아식 채소 두부 볶음",
            "야채볶음",  "야채오믈렛",  "어묵볶음",  "오징어볶음",  "오징어채볶음",  "잔멸치볶음",  "잡채",  "조랭이 떡 궁중 떡볶이",  "주꾸미볶음",  "칠리새우",  "토마토소스달걀볶음",  "팔보채",  "팽이버섯야채볶음",
            "표고버섯볶음",  "해물볶음",  "해산물볶음",  "헝가리안 폭찹",  "화지타",  "가자미조림(간장+고춧가루+기타양념)",  "갈치조림",  "고기완두조림",  "고등어조림",  "곤약 콩조림",  "다시마돼지고기조림",
            "돼지고기메추리알장조림",  "두부 튀김 조림",  "두부조림",  "땅콩조림",  "부추조갯살 콩비지조림",  "북어조림",  "소고기메추리알장조림",  "연근조림",  "오징어말이 케첩조림",  "우엉조림",  "콩조림",  "감자튀김",
            "고구마 빠스",  "고구마맛탕",  "고구마튀김",  "김말이튀김",  "깐풍기",  "난자완스",  "닭강정",  "닭튀김, 양념",  "도라지 검은깨 튀김",  "등심돈가스",  "똥집튀김(대구동구)",  "라조기",  "레몬, 파슬리 빵가루를 입힌 도미",
            "멸치찹쌀 양념튀김",  "모듬탕수",  "미꾸라지튀김",  "삼색 콩튀김",  "새우튀김",  "생강향의 고구마 크로켓",  "생선까스",  "수삼당근정과",  "안심돈가스",  "오징어튀김",  "쥐포튀김",  "채소튀김",  "치즈돈가스",  "치즈스틱",
            "치킨까스",  "탕수육",  "파마산치즈를 가미한 커틀렛",  "호두 치킨",  "황태강정",  "가지나물",  "고구마줄기나물",  "고사리나물",  "도라지나물",  "머위나물",  "무나물",  "미나리나물",  "숙주나물",  "시래기나물",  "취나물",
            "콩나물",  "감자냉채",  "겉절이, 배추",  "겉절이, 상추",  "골뱅이무침",  "과일 요구르트 샐러드",  "구운 닭고기 샐러드와 저나트륨 과일드레싱",  "구운 닭고기를 곁들인 두부 샐러드",  "김무침",  "낙지 잣소스 냉채",  "노각무침",
            "니스와 샐러드",  "단무지무침",  "달래무침",  "닭고기 샐러드",  "도라지생채",  "도토리묵",  "돼지고기 수육 부추무침",  "두부 다시마말이",  "두부선과 저나트륨 겨자 간장소스",  "마늘쫑무침",  "무말랭이무침",  "무생채",
            "묵무침, 청포묵",  "미역초무침",  "벤댕이무침(인천)",  "부추무침",  "북어채무침",  "산마드레싱 실곤약 샐러드",  "상추 겉절이",  "서대회무침(여수)",  "수삼 냉채",  "스테이크 샐러드",  "쑥갓무침",  "아보카도 두부 샐러드",
            "아시아식 국수 샐러드",  "양상추와 닭고기 요리",  "양장피",  "얼갈이 무침",  "연어냉채",  "오이 초절임",  "오이생채",  "오이선",  "오이지무침",  "이태리식 해산물 샐러드",  "쥐치채",  "참기름 드레싱의 즉석 샐러드",
            "카프라제",  "탕평채",  "태국식 불고기 샐러드",  "파래무침",  "파무침",  "펜네 파스타 샐러드",  "허브 마리네이드 참치 샐러드",  "호두 사과 샐러드",  "홍어회무침",  "갓김치",  "고들빼기",  "깍두기",  "깻잎김치",
            "나박김치",  "동치미",  "배추김치",  "백김치",  "부추김치",  "열무김치",  "열무얼갈이김치",  "오이소박이",  "총각김치",  "파김치",  "간장게장",  "양념게장",  "고추장아찌",  "깻잎장아찌",  "마늘장아찌",  "매실장아찌",
            "무장아찌",  "양배추 깻잎 피클",  "양파장아찌",  "오이피클",  "복숭아샤벳",  "서양식 씨리얼(뮤즐리)",  "식혜 팥빙수",  "홍시쉐이크",  "수정과",  "식혜",  "우유생강차",  "복숭아화채",  "수박화채",  "오렌지",  "과일 젤리",
            "두부양갱",  "딸기잼",  "가래떡",  "고구마경단",  "고구마란",  "고구마쑥단자",  "고추 장떡",  "김치떡",  "꿀떡",  "녹두시루떡",  "떡, 메밀전병 ",  "떡, 송편(깨) ",  "떡, 송편(콩) ",  "떡, 수수부꾸미",  "떡, 수수팥떡 ",
            "떡, 쑥떡     ",  "떡,경단(깨)  ",  "떡,경단(콩)  ",  "떡,기피편    ",  "모듬옥수수",  "모듬찰떡",  "무지개떡",  "백설기",  "백설기",  "시루떡",  "약식",  "인절미",  "절편",  "증편",  "찹쌀떡",  "초당순두부(강릉)",
            "닭고기 양배추 쌈",  "참치 소고기 양상추 쌈",  "낙지강회",  "대구포",  "돼지고기, 머리고기",  "북어포       ",  "어묵",  "육회",  "1955 버거",  "2014 랏츠버거",  "辛리브샌드",  "갈릭스테이크하우스버거",  "그릴디럭스버거",
            "그릴맥스버거",  "닭고기,살코기, 치킨텐더즈(4pieces)(버거킹)",  "닭고기,살코기, 치킨텐더즈(6pieces)(버거킹)",  "닭고기,살코기, 치킨텐더즈(9pieces)(버거킹)",  "더블치즈와퍼",  "더블 디럭스 슈프림버거",  "더블 쿼터파운더치즈",
            "더블치즈버거",  "데리버거",  "디럭스 슈프림 버거",  "디럭스 치즈버거",  "라이스버거(불고기)",  "랏츠버거",  "레이디샌드위치",  "로스트 갈릭버거",  "로얄그릴드 치킨버거",  "리치버거",  "맥더블",  "맥스파이시 더블 케이준 버거",
            "맥스파이시 상하이 디럭스",  "맥스파이시 상하이 버거",  "맥스파이시 케이준 버거",  "맥치킨",  "맥치킨버거",  "미니언 슈비 버거",  "바베큐 불고기 버거",  "베이컨 토마토 디럭스버거",  "베이컨갈릭징거버거",  "불갈비버거",
            "불고기버거",  "불고기와퍼쥬니어",  "불고기치킨 크리스피 버거",  "불고기크리스피치킨버거",  "불새버거",  "빅맥",  "빵,크로아쌍, 소시지크라상(버거킹)",  "사이다,사이다, 사이다(R)(버거킹)",  "상하이 스파이스 치킨 버거",
            "새우버거",  "소스류,머스터드소스, HoneyMustard(버거킹)",  "쉬림프징거버거",  "스위피버거",  "스파이시 텐더크리스피 버거",  "싱글즈불고기 버거",  "야채라이스불고기",  "양파링튀긴것,양파링튀긴것, 어니언링(L)(버거킹)",
            "양파링튀긴것,양파링튀긴것, 어니언링(R)(버거킹)",  "어린이불고기버거",  "오렌지쥬스,농축과즙, 오렌지주스(버거킹)",  "오징어버거",  "와일드쉬림프버거",  "와퍼주니어",  "우리한우버거",  "울트라치킨",  "자이언트더블버거",
            "징거버거",  "치즈버거",  "치즈징거버거",  "치킨버거",  "치킨불고기버거",  "치킨휠레샌드위치",  "커피,여과액, 커피(버거킹)",  "케이준통샌드위치",  "콜라,다이어트콜라, 다이어트(R)(버거킹)",  "콜라,일반콜라, 코카콜라(R)(버거킹)",
            "쿼터파운더치즈",  "쿼터파운더치즈버거",  "타바스코맛샌드위치",  "텐더그릴치킨",  "텐더그릴치킨버거",  "토마토 치즈버거",  "토마토케첩,토마토케첩, 케첩(버거킹)",  "통새우버거",  "트위스터",  "파이,사과파이, 애플파이(버거킹)",
            "한우불고기주니어",  "핫크리스피버거",  "해쉬브라운,해쉬브라운, 해쉬브라운(버거킹)",  "햄버거",  "햄버거,일반버거, 더블와퍼(버거킹)",  "햄버거,일반버거, 와퍼(버거킹)",  "햄버거,일반버거, 와퍼쥬니어(버거킹)",
            "햄버거,일반버거, 햄버거(버거킹)",  "햄버거,치즈버거, 베이컨더블치즈버거(버거킹)",  "햄버거,치즈버거, 치즈버거(버거킹)",  "햄버거,치즈버거, 치즈와퍼햄버거(버거킹)",  "햄버거,치킨버거, 치킨버거(버거킹)",
            "햄버거,치킨버거, 치킨와퍼(버거킹)",  "햄버거,휘시버거, BK휘시휠레햄버거(버거킹)",  "후렌치후라이,가공품, 일반,후렌치후라이(L)(버거킹)",  "후렌치후라이,가공품, 일반,후렌치후라이(R)(버거킹)",  "갈릭스테이크 (씬) M",
            "갈릭스테이크피자 (씬, L)",  "갈릭스테이크피자 (씬, M)",  "갈릭스테이크피자 (오리지널, L)",  "갈릭스테이크피자 (오리지널, M)",  "갈릭스테이크피자 씬L",  "갈릭스테이크피자 씬XL",  "갈릭스테이크피자 팬L",
            "갈릭치킨바베큐 피자",  "감자피자 씬'XL",  "감자피자 씬L",  "감자피자 팬'XL",  "감자피자 팬L",  "게살통통피자 팬L",  "고구마 바이트 피자",  "고구마(레귤러2조각)",  "고구마(스크린)",  "고구마무스바이트(L)",
            "고구마피자",  "고구마피자 L",  "고구마피자 M",  "고구마피자 씬'XL",  "고구마피자 씬L",  "고구마피자 팬'XL",  "고구마피자 팬L",  "고구마피자(라이스골드) F",  "고구마피자(라이스골드) L",  "고구마피자(라이스골드) R",
            "고구마피자(라이스바이트) F",  "고구마피자(라이스바이트) L",  "고구마피자(라이스바이트) R",  "고구마피자(백미) L",  "고구마피자(백미) R",  "베이컨체다치즈 (나폴리,L)",  "베이컨체다치즈 (나폴리,L)",
            "베이컨체다치즈 (씬,L) 1조각",  "베이컨체다치즈(스몰피자)",  "베이컨체다치즈피자 (씬, M)",  "베이컨체다치즈피자 (오리지널, L)",  "베이컨체다치즈피자 (오리지널, M)",  "베이컨포테이토바이트(L)",
            "베이컨포테이토바이트피자",  "베지테리안 오리진 L",  "베지테리안 오리진 R",  "베지테리안 치즈캡 L",  "베지테리안 치즈캡 R",  "볶은김치불고기(L)",  "볶은김치불고기(M)",  "불갈비바이트(L)",
            "불갈비바이트(킹)",  "불갈비바이트피자",  "불갈비피자",  "불갈비피자(L)",  "불갈비피자[R]",  "불고기 (나폴리,L)",  "불고기 (씬,L)",  "불고기 (오리지널,L)",  "불고기 바이트 피자",  "불고기 피자",
            "불고기 피자(씬) L",  "불고기(레귤러)",  "불고기(레귤러2조각)",  "불고기피자",  "불고기피자  (씬, L)",  "불고기피자 (씬, M)",  "불고기피자 (오리지널, L)",  "불고기피자 (오리지널, M)",  "불고기피자 L",
            "불고기피자 M",  "불고기피자(L)",  "불고기피자[R]",  "빙고슈프림 피자(씬) L",  "야채 바이트 피자",  "야채피자",  "야채피자 L",  "야채피자 M",  "치즈피자 (오리지널, L)",  "치즈피자 (오리지널, M)",  "치즈피자 L",
            "치즈피자 M",  "페페로니 피자",  "포테이토 피자",  "포테이토피자 (오리지널, L)",  "포테이토피자 (오리지널, M)",  "포테이토피자 L",  "포테이토피자 M",  "하와이언피자",  "하와이언피자(라이스골드) F",
            "한우불고기 라이스골드L",  "한우불고기 라이스골드R",  "한우불고기 라이스바이트L",  "한우불고기 라이스바이트R",  "한우불고기 백미떡도우L",  "한우불고기 백미떡도우R",  "한우불고기 석쇠L",  "한우불고기 석쇠R",
            "한우불고기 치즈크러스트L",  "한우불고기 치즈크러스트R",  "한우불고기 팬L",  "한우불고기 팬R",  "한우불고기 흑미떡도우L",  "한우불고기 흑미떡도우R",  "한우불고기(라이스골드)  F",  "한우불고기(라이스바이트)  F",
            "한우불고기(석쇠)  F",  "한우불고기(치즈크러스트)  F",  "한우불고기(팬)  F",  "한우불고기(팬) L",  "한우송이(치크)",  "한우송이(피타)",  "한우송이피자(스크린)",  "한우송이피자(팬)",  "한우핫불고기 라이스골드L",
            "한우핫불고기 라이스골드R",  "한우핫불고기 라이스바이트L",  "핫스파이스피자 L",  "핫스파이스피자 M",  "핫치킨  피자(씬) L",  "핫치킨 피자",  "핫치킨피자",  "핫치킨피자 씬L",  "핫치킨피자 팬L",  "허니치킨피자 L",
            "허니치킨피자 R",  "갈릭바게뜨",  "갈릭앤치즈브레드",  "거울공주미미",  "건포도빵",  "내가찾던초코케이크", };//자동완성에 들어갈 음식들






}

