package com.changwon.wooogi.frequency;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainTest extends AppCompatActivity {

    private EditText editTextFreq;
    // private EditText editTextDuration;
    private TextView textViewvol;
    private TextView textViewFreq;
    // private EditText editTextVol;
    Boolean trigger = true;
    Boolean Ready = false;
    Boolean Stop = false;
    Boolean TextReady = false;      // 데시벨 텍스트 출력 여부
    Boolean ClickEvent = false;     // 버튼을 클릭했을때 사용할 변수 ( 10dB을 낮출때, 낮춘 그상태로 주파수 한번더 실행 )
    Boolean ButtonEvent = true;
    Boolean isPlaying = false;     // 주파수 소리가 나오고 있는지, 또는 중지상태인지 여부 (버튼클릭 이벤트)

    private BackPressCloseHandler backPressCloseHandler;

    AudioManager audioManager;

    int number[] = {1000, 2000, 3000, 4000, 6000, 8000, 1000, 500, 250, 125}; // 주파수 배열
    float sound_decibel[] = {0.000005f, 0.000005f, 0.000005f, 0.00012f, 0.00025f, 0.00050f, 0.00080f, 0.00120f, 0.00180f, 0.00280f, 0.00400f, 0.00600f,
            0.00900f, 0.01200f, 0.01800f, 0.03000f, 0.04500f, 0.06000f, 0.08000f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 1.0f};
    int dec = 10; // 현재 데시벨
    int duration = 2; // 지연시간
    int num = 0; // 버튼클릭횟수
    int count = 0; //시간
    int[] LeftValue = new int[100];     // 테스트용 왼쪽 데시벨 값
    int[] RightValue = new int[100];    // 테스트용 오른쪽 데시벨 값
    public static int[] RealLeftValue = new int[100];     // 실제 왼쪽 데시벨 값(결과화면용)
    public static int[] RealRightValue = new int[100];    // 실제 오른쪽 데시벨 값(결과화면용)
    int MindB = -20;      // 현재까지 사용자가 들리는 최소 데시벨
    int iscycle = 0;    // 검사 알고리즘에서 몇번째 사이클인지 구분 변수(iscycle=1 일 경우 +20dB씩 , 그이상일 경우 +5dB씩 으로 구분)
    boolean matchdB = false;    // MindB과 현재 내가 버튼을 누른 dB이 같은지 다른지 구분하는 변수
    int Testtime = 3;       // 데시벨을 몇초마다 증가시킬건지 결정하는 변수( ex, Testtime = 3 인 경우 3초마다 dB 증가)
    int LeftCount = 0;
    int RightCount = 0;
    int FqCount = 0; // 주파수
    int start = 0; //소리 딜레이
    float realdB = 0; //실제 데시벨
    boolean scenedelay = false; // 다음뷰로 이동하기전 딜레이
    int scenedelaycount = 0;    // 다음뷰로 이동하기전 딜레이 시간

    TextView LText;
    TextView RText;
    TextView FqValue;
    TextView Test;
    TextView StartText;
    TextView TestTitle;

    String Left;
    String Right;

    Button btn_test;

//    public static boolean Sound = true;     // 오디오를 왼쪽, 오른쪽으로 구분해주는 변
    Thread thread;

    private FloatingActionButton myFab;

    Intent gotoMain4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

//        for(int i = 0 ; i < 9 ; i++ ) {
//            RealLeftValue[i] = 40;
//            RealRightValue[i] = 50;
//        }
//
//        FqCount = 9;

        gotoMain4 = new Intent(this, MainResult.class);


        Bundle leftBun =new Bundle();
        leftBun.putIntArray("LeftBun", RealLeftValue);

        Bundle RightBun =new Bundle();
        RightBun.putIntArray("RightBun", RealRightValue);

        gotoMain4.putExtra("RealLeftValue", leftBun);
        gotoMain4.putExtra("RealRightValue", RightBun);

        backPressCloseHandler = new BackPressCloseHandler(this);

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        btn_test = (Button) findViewById(R.id.btn_test);
        // editTextFreq = (EditText) findViewById(R.id.editTextFreq);
        textViewvol = (TextView) findViewById(R.id.test_decibel_view);
//        LText = (TextView) findViewById(R.id.LeftValue);
//        Left = LText.getText().toString();
//        RText = (TextView) findViewById(R.id.RightValue);
//        Right = RText.getText().toString();
//        FqValue = (TextView) findViewById(R.id.FrequencyValue);
//        StartText = (TextView)findViewById(R.id.starttext);
//        TestTitle = (TextView)findViewById(R.id.testtitle);


//        Test = (TextView)findViewById(R.id.woogitest);



//        FqValue.setText("주파수 : 1000");

        SecondThread runnable = new SecondThread();
        thread = new Thread(runnable);
        thread.setDaemon(true);

//        btn_left.setEnabled(true);

        textViewvol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                realdB = getdB(dec);
                Log.d("safaud", ""+realdB);
//                ZenTone.getInstance().stop();
//                Toast.makeText(MainTest.this, "" + realdB, Toast.LENGTH_SHORT).show();
                handleTonePlay();
                ButtonEvent = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void onClick(View view) {

        boolean left = false;
        boolean right = false;

        switch (view.getId()) {
            case R.id.btn_test:
//                if (trigger && !Ready) {
//                    btn_left.setText("들려요");
//                    btn_left.setEnabled(false);
//                    thread.start();
//                    trigger = false;
//                }
//
//                if( Ready ) {
//                    mHandler.sendEmptyMessage(1);
//                }

                if (ButtonEvent) {

                    if(!Ready) {
                        if(trigger) {
//                            btn_left.setText("들려요");
//                            btn_left.setEnabled(false);
//                            StartText.setText("소리가 들리면 아래 버튼을 눌러주세요.");
//                            TestTitle.setText("자동 청력검사 중");
                            textViewvol.setText("30");
                            thread.start();
                            trigger = false;
                        }
                    }
                    else {

                        if(MindB == dec) {
                            matchdB = true;
                        }

                        MindB = dec;
//                        Toast.makeText(this, "MindB is " + MindB, Toast.LENGTH_SHORT).show();
                        isPlaying = false;
                        PlayTone.getInstance().stop();
//                        ZenTone.getInstance().stop();
                        mHandler.sendEmptyMessage(1);
                    }
                }

                ButtonEvent = false;
                if (!Ready) {
                    Ready = true;
                }
                break;
        }
    }
    //Thread MARK
    class SecondThread implements Runnable {
        public void run() {
            while (true) {

                if(isPlaying == false) {        // 주파수 실행 도중 버튼 클릭시 도중에 플레이를 끊고 다음 데시벨 주파수 실행
                    count = 3;
                }

                if ( ClickEvent ) {         // 버튼 클릭했을시 -10dB을 했을 경우의 주파수 실행문
                    if(count % 3 == 0 ) {
                        TextReady = true;
                        count = 0;
                        mHandler.sendEmptyMessage(0);
//                        ClickEvent = false;
                    }
                }
                else {          // 버튼 클릭을 안했을때의 평상시 실행문
                    if(dec <= 110){      // 최대 dB를 110dB 으로 제한
                        if(PlayTone.Sound) {     // 왼쪽 귀에 주파수 실행

                            if(!matchdB) {      // MindB과 버튼을 눌렀을때의 dB이 다른경우 if문 실행

                                if(iscycle == 0 && count % Testtime == 0) {    // 첫 주파수 실행 시 3초마다 20dB씩 증가
                                    if(dec < 110) {
                                        dec += 20;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }
                                else if(iscycle == 1 && count % Testtime == 0) {     // 버튼을 한번 누른 이후 3초마다 5dB씩 증가
                                    if(dec < 110) {
                                        dec += 5;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }
                                else if (iscycle == 2 && count % Testtime == 0) {      // 버튼을 두번 누른 이후 3초마다 5dB씩 증가
                                    if(dec < 110) {
                                        dec += 5;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }

                            }
                            else if(matchdB) {      // MindB 와 버튼을 눌렀을때의 dB이 같은경우 if문 실행
                                count = 0;
                                mHandler.sendEmptyMessage(2);
                                matchdB = false;
                            }
                        }

                        else {      // 오른쪽 귀에 주파수 실행

                            if(!matchdB) {
                                if(iscycle == 0 && count % Testtime == 0) {    // 첫 주파수 실행 시 3초마다 20dB씩 증가
                                    if(dec < 110) {
                                        dec += 20;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }
                                else if(iscycle == 1 && count % Testtime == 0) {     // 버튼을 한번 누른 이후 3초마다 5dB씩 증가
                                    if(dec < 110) {
                                        dec += 5;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }
                                else if (iscycle == 2 && count % Testtime == 0) {      // 버튼을 두번 누른 이후 3초마다 5dB씩 증가
                                    if(dec < 110) {
                                        dec += 5;
                                    }
                                    count = 0;
                                    TextReady = true;
                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                            else if(matchdB) {      // MindB 와 버튼을 눌렀을때의 dB이 같은경우 if문 실행
                                count = 0;
                                mHandler.sendEmptyMessage(3);
                                matchdB = false;
                            }
                        }

                    }
                }
                if(scenedelay) {                // 검사가 끝난 후 scenedelaycount 시간만큼 지난 후 뷰 이동
                    try {           // 쓰레드가 1초마다 실행
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        ;
                    }
                    scenedelaycount++;

                    if(scenedelaycount == 2) {
                        mHandler.sendEmptyMessage(4);
                    }
                }

                try {           // 쓰레드가 1초마다 실행
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    ;
                }

                count++;
//                mHandler.sendEmptyMessage(10);
                if(count % 3 == 0) {
                    count = 0;
                }

            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {        // 주파수 실행을 담당할 핸들메시지
//                // handleTonePlay();
                if (TextReady) {
                    textViewvol.setText("" + dec + "dB 입니다.");
                    count = 0;
                    TextReady = false;
                    ClickEvent = false;
                }
            }

            if (msg.what == 1) {        // 버튼 클릭을 담당할 핸들메시지
                if(iscycle < 2) {
                    iscycle += 1;
                }
                ClickEvent = true;
                if(!matchdB && dec > -5) {
                    dec -= 10;
                }
                else if(!matchdB && dec > -10) {
                    dec -= 5;
                }
                if(matchdB == true && PlayTone.Sound == true) {
                    mHandler.sendEmptyMessage(2);
                }
                else if(matchdB == true && PlayTone.Sound == false) {
                    mHandler.sendEmptyMessage(3);
                }
                else {
                    mHandler.sendEmptyMessage(0);
                }
            }

            if (msg.what == 2) {
                LeftValue[LeftCount] = MindB;
//                LText.setText(Left + " " + LeftValue[LeftCount] + "dB");
                LeftCount++;
                PlayTone.Sound = false;
                matchdB = false;
                iscycle = 0;
//                count = 0;
                TextReady = true;
                dec = 30;
                MindB = -20;
//                Left = LText.getText().toString();
                mHandler.sendEmptyMessage(0);

            }

            if (msg.what == 3) {
                RightValue[RightCount] = MindB;
//                RText.setText(Right + " " + RightValue[RightCount] + "dB");
                RightCount++;
                FqCount += 1;
//                count = 0;
                matchdB = false;
                PlayTone.Sound = true;
                iscycle = 0;
                TextReady = true;

                if (FqCount < 10) {
//                    FqValue.setText("주파수 :" + number[FqCount]);
                } else {
                    // 왼쪽 귀 데시벨 값을 결과화면을 위한 배열로 다시 저장
                    RealLeftValue[0] = LeftValue[9];
                    RealLeftValue[1] = LeftValue[8];
                    RealLeftValue[2] = LeftValue[7];

                    if(LeftValue[0] >= LeftValue[6]) {
                        RealLeftValue[3] = LeftValue[6];
                    }
                    else {
                        RealLeftValue[3] = LeftValue[0];
                    }

                    RealLeftValue[4] = LeftValue[1];
                    RealLeftValue[5] = LeftValue[2];
                    RealLeftValue[6] = LeftValue[3];
                    RealLeftValue[7] = LeftValue[4];
                    RealLeftValue[8] = LeftValue[5];

                    // 오른쪽 귀 데시벨 값을 결과화면을 위한 배열로 다시 저장
                    RealRightValue[0] = RightValue[9];
                    RealRightValue[1] = RightValue[8];
                    RealRightValue[2] = RightValue[7];

                    if(RightValue[0] >= RightValue[6]) {
                        RealRightValue[3] = RightValue[6];
                    }
                    else {
                        RealRightValue[3] = RightValue[0];
                    }

                    RealRightValue[4] = RightValue[1];
                    RealRightValue[5] = RightValue[2];
                    RealRightValue[6] = RightValue[3];
                    RealRightValue[7] = RightValue[4];
                    RealRightValue[8] = RightValue[5];

                    scenedelay = true;
//                    Toast.makeText(MainTest.this, "자동 청력검사가 종료되었습니다.\n검사결과 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainTest.this, "The automatic hearing test has ended.\nGo to the test result screen.", Toast.LENGTH_SHORT).show();

                }
                dec = 30;
                MindB = -20;
//                Right = RText.getText().toString();
                mHandler.sendEmptyMessage(0);
//                RightValue[RightCount] = dec;
//                RText.setText(Right + " " + RightValue[RightCount] + "dB");
//                RightCount++;
//                Sound = true;
//                num = 0;
////                count = 0;
//                TextReady = true;
//
//                if (FqCount < 7) {
//                    FqValue.setText("주파수 :" + number[FqCount]);
//
//                } else {
//                    startActivityForResult(gotoMain4, 0);
//                }
//                FqCount += 1;
//                dec = 30;
//                Right = RText.getText().toString();
//                mHandler.sendEmptyMessage(0);
            }

            if (msg.what == 4) {
                startActivityForResult(gotoMain4, 0);
                finish();
            }

        }
    };

    private void handleTonePlay() {

        if (FqCount >= 10) return;
        btn_test.setEnabled(true);

        isPlaying = true;
        PlayTone.getInstance().makeTone(number[FqCount], duration, realdB);
        PlayTone.getInstance().play();

//        ZenTone.getInstance().generate(number[FqCount], duration, realdB, new ToneStoppedListener() {
//
//            @Override
//            public void onToneStopped() {
//
//                // btn_left.setEnabled(false);
//
//                // myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
//            }
//        });
    }


    private float getdB(int decibel) {        // 실제 데시벨로 변환 함수
//        float dB = decibel - 10;
        int real_dB = decibel/5 + 2;
//        Toast.makeText(this, ""+sound_decibel[real_dB], Toast.LENGTH_SHORT).show();
//        if(decibel < 10) return 0.000005f;
//        else if(decibel > 90) return 1.0f;
        return sound_decibel[real_dB];
//        dB /= 1000f;

//        return dB;
    }


//    //Back Intent Active
//
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


//                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                MainTest.this.finish();
                Intent t = new Intent(activity, MainActivity.class);
                activity.startActivity(t);
//                activity.moveTaskToBack(true);

            }
        }

        public void showGuide() {
//            toast = Toast.makeText(activity, "한번 더 누르시면 처음화면으로 돌아갑니다.", Toast.LENGTH_SHORT);
            toast = Toast.makeText(activity, "Press again to return to the initial screen.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}