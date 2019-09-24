package com.changwon.wooogi.frequency;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MenualTest extends AppCompatActivity {

    Thread thread;
    int decibel = 30;            // 측정 데시벨
    float dB = 0;
    int frequency = 1000;   // 측정 주파수
    float sound_decibel[] = {0.000005f, 0.000005f, 0.000005f, 0.00012f, 0.00025f, 0.00050f, 0.00080f, 0.00120f, 0.00180f, 0.00280f, 0.00400f, 0.00600f,
            0.00900f, 0.01200f, 0.01800f, 0.03000f, 0.04500f, 0.06000f, 0.08000f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 1.0f };
    int frequencynumber[] = {125, 250, 500, 1000, 2000, 3000, 4000, 6000, 8000}; // 주파수 배열
    public static int[] menualrealrightvalue = new int[100];      // 수동검사 화면에서의 주파수별 역치값 저장 변수(오른쪽)
    public static int[] menualrealleftvalue = new int[100];        // 수동검사 화면에서의 주파수별 역치값 저장 변수(왼쪽)
    int fnumber = 3;
    int maskingdB = 10;     // 측정 에어마스킹에서의 데시벨
    boolean sound = true;   // 왼쪽 귀, 오른쪽 귀 구분 변수
    boolean isPlaying = false;
    int duration = 3;
    int count = 0;
    int test = 0;
    public int[] decibel_value = new int[100];  // 데시벨 값 배열 변수
    MediaPlayer mp        = null;       // 화이트노이즈 미디어 플레이어

    TextView frequencytext;
    TextView dbtext;
    TextView maskingdbtext;
    Button lefttoggle;
    Button righttoggle;
    Button frequencydown;
    Button frequencyup;
    Button dbdown;
    Button dbup;
    Button playingbutton;
    Button responsebutton;
    Button maskingright;
    Button maskingdown;
    Button maskingup;
    Button resultcheck;
    ConstraintLayout button_event;

//    Intent goto_manual_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menual_test);

        for(int i = 0; i < 100 ; i++) {
            menualrealleftvalue[i] = -10;
            menualrealrightvalue[i] = -10;
        }

//        goto_manual_result = new Intent(this, MenualResult.class);
//
//        Bundle leftBun = new Bundle();
//        leftBun.putIntArray("LeftBun", menualrealleftvalue);
//
//        Bundle rightBun = new Bundle();
//        rightBun.putIntArray("RightBun", menualrealrightvalue);
//
//        goto_manual_result.putExtra("Manual_LeftValue", leftBun);
//        goto_manual_result.putExtra("Manual_RightValue", rightBun);

//        Toast.makeText(this, "menual = " + menualrealleftvalue[5], Toast.LENGTH_SHORT).show();

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SecondThread1 runnable1 = new SecondThread1();
        thread = new Thread(runnable1);
        thread.setDaemon(true);
        thread.start();

        lefttoggle = (Button)findViewById(R.id.menual_left_btn);
        righttoggle = (Button)findViewById(R.id.menual_right_btn);
        frequencydown = (Button)findViewById(R.id.menual_hz_down_btn);
        frequencyup = (Button)findViewById(R.id.menual_hz_up_btn);
        dbdown = (Button)findViewById(R.id.menual_db_down_btn);
        dbup = (Button)findViewById(R.id.menual_db_up_btn);

        frequencytext = (TextView)findViewById(R.id.frequencytext);
        dbtext = (TextView)findViewById(R.id.decibeltext);

        playingbutton = (Button)findViewById(R.id.playing);
        responsebutton = (Button)findViewById(R.id.response);
        resultcheck = (Button)findViewById(R.id.resultcheck);

        button_event = (ConstraintLayout)findViewById(R.id.menual_button_event);

//        maskingright = (Button)findViewById(R.id.maskingright);
//        maskingdown = (Button)findViewById(R.id.maskingdown);
//        maskingup = (Button)findViewById(R.id.maskingup);
//
//        maskingdbtext = (TextView)findViewById(R.id.maskingdbtext);

        lefttoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayTone.Sound = true;
                button_event.setBackground(ContextCompat.getDrawable(MenualTest.this, R.drawable.new_icon_menual_left));
//                lefttoggle.setBackgroundColor(Color.rgb(27,32,139));
//                righttoggle.setBackgroundColor(Color.rgb(229,118,141));
            }
        });

        righttoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayTone.Sound = false;
                button_event.setBackground(ContextCompat.getDrawable(MenualTest.this, R.drawable.new_icon_menual_right));
//                lefttoggle.setBackgroundColor(Color.rgb(107,112,225));
//                righttoggle.setBackgroundColor(Color.rgb(175,58,83));
            }
        });

        frequencydown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fnumber > 0) {
                    fnumber -= 1;
                    frequency = frequencynumber[fnumber];
                    frequencytext.setText(frequency + " Hz");
                }
            }
        });

        frequencyup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fnumber < 8) {
                    fnumber += 1;
                    frequency = frequencynumber[fnumber];
                    frequencytext.setText(frequency + " Hz");
                }
            }
        });

        dbdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(decibel > -10) {
                    decibel -= 5;
                    dbtext.setText(decibel + "dB");
                }
            }
        });

        dbup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(decibel < 110) {
                    decibel += 5;
                    dbtext.setText(decibel + "dB");
                }
            }
        });

        playingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dB = getdB(decibel);
                if(isPlaying == false) {
                    PlayTone.getInstance().makeTone(frequencynumber[fnumber], duration, dB);
                    PlayTone.getInstance().play();
//                    playingbutton.setBackgroundColor(Color.rgb(238,146,14));
                    isPlaying = true;
                }
//                Toast.makeText(MenualTest.this, "isPlaying = true", Toast.LENGTH_SHORT).show();
            }
        });


        responsebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                decibel_list();
                if(PlayTone.Sound) {        // 왼쪽
                    if(frequency == 125) {
                        menualrealleftvalue[0] = decibel;
                    }
                    else if(frequency == 250) {
                        menualrealleftvalue[1] = decibel;
                    }

                    else if(frequency == 500) {
                        menualrealleftvalue[2] = decibel;
                    }

                    else if(frequency == 1000) {
                        menualrealleftvalue[3] = decibel;
                    }

                    else if(frequency == 2000) {
                        menualrealleftvalue[4] = decibel;
                    }

                    else if(frequency == 3000) {
                        menualrealleftvalue[5] = decibel;
                    }

                    else if(frequency == 4000) {
                        menualrealleftvalue[6] = decibel;
                    }

                    else if(frequency == 6000) {
                        menualrealleftvalue[7] = decibel;
                    }

                    else {
                        menualrealleftvalue[8] = decibel;
                    }
                }

                else {                      // 오른쪽
                    if(frequency == 125) {
                        menualrealrightvalue[0] = decibel;
                    }
                    else if(frequency == 250) {
                        menualrealrightvalue[1] = decibel;
                    }

                    else if(frequency == 500) {
                        menualrealrightvalue[2] = decibel;
                    }

                    else if(frequency == 1000) {
                        menualrealrightvalue[3] = decibel;
                    }

                    else if(frequency == 2000) {
                        menualrealrightvalue[4] = decibel;
                    }

                    else if(frequency == 3000) {
                        menualrealrightvalue[5] = decibel;
                    }

                    else if(frequency == 4000) {
                        menualrealrightvalue[6] = decibel;
                    }

                    else if(frequency == 6000) {
                        menualrealrightvalue[7] = decibel;
                    }

                    else {
                        menualrealrightvalue[8] = decibel;
                    }
                }
            }
        });

        resultcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenualTest.this, MenualResult.class);
//                intent.putExtra("menualrightvalue", menualrealrightvalue);
//                intent.putExtra("menualleftvalue", menualrealleftvalue);
                startActivity(intent);

//                startActivityForResult(goto_manual_result, 0);
                finish();
            }
        });

    }

    class SecondThread1 implements Runnable {
        public void run() {
            while (true) {

                if(isPlaying == true){
                    try {           // 쓰레드가 1초마다 실행
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        ;
                    }
                    count++;
                }
//                else {
//                    count = 0;
//                }
//
                if(count == 3) {
                    count = 0;
                    isPlaying = false;
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
                if (msg.what == 0) {
//                    playingbutton.setBackgroundColor(Color.rgb(241,187,109));
                }
            }
    };

    private float getdB(int decibel) {        // 실제 데시벨로 변환 함수
//        float dB = decibel - 10;
//        dB /= 100;

        int real_dB = decibel/5 + 2;
        Log.d("safaud", ""+sound_decibel[real_dB]);
        return sound_decibel[real_dB];
    }

    void playWhiteNoise() {
        mp = MediaPlayer.create(MenualTest.this, R.raw.white);
        mp.setVolume(1.0f, 0.1f);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.start();
    }

    public void decibel_list() {

        Map<String, double[]> map = new HashMap<String, double[]>();

        // 왼쪽
        double[] l_decibel_125_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_125Hz", l_decibel_125_value);

        double[] l_decibel_250_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_250Hz", l_decibel_250_value);

        double[] l_decibel_500_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_500Hz", l_decibel_500_value);

        double[] l_decibel_1000_value = {0.000005f, 0.000005f, 0.000005f, 0.000005f, 0.00001f, 0.00002f, 0.00004f, 0.00008f, 0.00012f, 0.00025f, 0.00050f, 0.00080f, 0.00140f, 0.00220f, 0.00400f, 0.00600f, 0.00900f,
                0.01600f, 0.02400f, 0.032768f, 0.065536f, 1.0f, 1.0f, 1.0f, 1.0f};
        map.put("l_1000Hz", l_decibel_1000_value);

        double[] l_decibel_2000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_2000Hz", l_decibel_2000_value);

        double[] l_decibel_3000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_3000Hz", l_decibel_3000_value);

        double[] l_decibel_4000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_4000Hz", l_decibel_4000_value);

        double[] l_decibel_6000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_6000Hz", l_decibel_6000_value);

        double[] l_decibel_8000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("l_8000Hz", l_decibel_8000_value);

        // 오른쪽
        double[] r_decibel_125_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_125Hz", r_decibel_125_value);

        double[] r_decibel_250_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_250Hz", r_decibel_250_value);

        double[] r_decibel_500_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_500Hz", r_decibel_500_value);

        double[] r_decibel_1000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_1000Hz", r_decibel_1000_value);

        double[] r_decibel_2000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_2000Hz", r_decibel_2000_value);

        double[] r_decibel_3000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_3000Hz", r_decibel_3000_value);

        double[] r_decibel_4000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_4000Hz", r_decibel_4000_value);

        double[] r_decibel_6000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_6000Hz", r_decibel_6000_value);

        double[] r_decibel_8000_value = {0, 0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28, 0.32, 0.36, 0.40, 0.44, 0.48, 0.52, 0.56, 0.60, 0.64, 0.68, 0.72, 0.76, 0.80, 0.84, 0.88, 0.92, 0.96};
        map.put("r_8000Hz", r_decibel_8000_value);
//
//        double[] value = map.get("r_8000Hz");
//
//        Toast.makeText(this, "r_decibel_8000_value = " + value[3], Toast.LENGTH_SHORT).show();

//        System.out.println(map.get("dog"));
    }
}
