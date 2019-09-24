package com.changwon.wooogi.frequency;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    static FrequencyDAO dao;
    private BackPressCloseHandler backPressCloseHandler;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new FrequencyDAO(getApplicationContext(), 1);

//        FrequencyVO frequencydate = new FrequencyVO(0,"8월 21일","3시 10분",10,10, 20,30, 40, 50, 60, 70, 80, 30,20, 50,-10, 40, 15, 60, 35, 80, 55,33,"경도난청","중도난청",44,11,22);
//        dao.insert(frequencydate);

//        frequencydate = new FrequencyVO(0,"3월 8일","1시 30분",1000, 200,300, 400, 500, 60, 70, 80, 10, 20,30, 40, 50, 60, 70, 80, 55,33,4,11,22);
//        dao.insert(frequencydate);

//        dao.delete(9);

// 리스트 보기
//        List<FrequencyVO> list = dao.list();
//        for(int i=0;i<list.size();i++) {
//            Log.i("번호 ",list.get(i).empno+"");
//            Log.i("날짜 ",list.get(i).date+"");
//            Log.i("시간 ",list.get(i).time+"");
//            Log.i("l_250Hz ",list.get(i).l_a250+"");
//            Log.i("l_500Hz ",list.get(i).l_a500+"");
//            Log.i("l_1000Hz ",list.get(i).l_a1000+"");
//            Log.i("l_2000Hz ",list.get(i).l_a2000+"");
//            Log.i("l_3000Hz ",list.get(i).l_a3000+"");
//            Log.i("l_4000Hz ",list.get(i).l_a4000+"");
//            Log.i("l_6000Hz ",list.get(i).l_a6000+"");
//            Log.i("l_8000Hz ",list.get(i).l_a8000+"");
//            Log.i("r_250Hz ",list.get(i).r_a250+"");
//            Log.i("r_500Hz ",list.get(i).r_a500+"");
//            Log.i("r_1000Hz ",list.get(i).r_a1000+"");
//            Log.i("r_2000Hz ",list.get(i).r_a2000+"");
//            Log.i("r_3000Hz ",list.get(i).r_a3000+"");
//            Log.i("r_4000Hz ",list.get(i).r_a4000+"");
//            Log.i("r_6000Hz ",list.get(i).r_a6000+"");
//            Log.i("r_8000Hz ",list.get(i).r_a8000+"");
//            Log.i("Lcon ",list.get(i).l_sextant+"");
//            Log.i("Rcon ",list.get(i).r_sextant+"");
//            Log.i("Lloss_ratio ",list.get(i).l_loss_ratio+"");
//            Log.i("Rloss_ratio ",list.get(i).r_loss_ratio+"");
//            Log.i("Tloss_ratio ",list.get(i).loss_ratio+"");
//        }




        backPressCloseHandler = new BackPressCloseHandler(this);

        Button btn_main = (Button) findViewById(R.id.btn_main);
        Button btn_sub = (Button) findViewById(R.id.btn_sub);
        TextView textView = (TextView)findViewById(R.id.test1);
//        Button testing = (Button) findViewById(R.id.testing);
        Button menual = (Button) findViewById(R.id.menual);
        Button test_btn = (Button) findViewById(R.id.test_btn);
//        Button dB_btn = (Button)findViewById(R.id.dbtest);
//        Button  listbutton = (Button)findViewById(R.id.listtest);

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,VolumeCheck.class);
                startActivity(intent);

            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,ResultList.class);
                startActivity(intent);

            }
        });
//        testing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ResultTest.class);
//                startActivity(intent);
//            }
//        });

        menual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenualTest.class);
                startActivity(intent);
            }
        });

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, dBTest.class);
                startActivity(intent);
            }
        });

//        listbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MainResult.class);
//                startActivity(intent);
//            }
//        });

//        btn_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(MainActivity.this,FrequencyTest.class);
//                startActivity(intent);
//
//            }
//        });

//        textView.setText("HEARING TEST는 청각검사를 위한 어플리케이션입니다.\n청력이란 소리의 강도에 대한 청각의 감수성입니다.\n" +
//                "소리의 감도를 나타내는 데시벨이라는 단위를 사용합니다.\n소리의 세기는 소리의 크기 그 자체는 아닙니다.");

        textView.setText("SAFAUD AUDIOMETRY is an application to identify an individual's hearing threshold.\nHearing threshold is the sensitivity of auditory sense to the loudness of sound.\n" +
                "Decibel(dB) is used to express the loudness of sound.");

    }

    //Back Intent Active

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                toast.cancel();

                Intent t = new Intent(activity, MainActivity.class);
                activity.startActivity(t);

                activity.moveTaskToBack(true);
                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
