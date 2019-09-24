package com.changwon.wooogi.frequency;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class dBTest extends AppCompatActivity {

    Thread thread;
    int[] realfrequency = {125, 250, 500, 1000, 2000, 3000, 4000, 6000, 8000};
    int frequencyCount = 2;
    float Amp = 1.0000f;
    TextView frequencytext;
    TextView Amptext;
    boolean isPlaying = false;
    int duration = 7;
    int count = 0;
    boolean Play = false;
    Button TestLeft;
    Button TestRight;
    Button PlayButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_btest);

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SecondThread2 runnable2 = new SecondThread2();
        thread = new Thread(runnable2);
        thread.setDaemon(true);
        thread.start();

        frequencytext = (TextView)findViewById(R.id.testfrequencytext);
        Amptext = (TextView)findViewById(R.id.testAmptest);

        TestLeft = (Button)findViewById(R.id.testLeft);
        TestRight = (Button)findViewById(R.id.testRight);
        PlayButton = (Button)findViewById(R.id.testPlay);


    }

    public void onClick1(View v) {
        switch(v.getId()) {
            case R.id.testLeft:
                PlayTone.Sound = true;
                TestLeft.setBackgroundColor(Color.rgb(27,32,139));
                TestRight.setBackgroundColor(Color.rgb(229,118,141));
                break;
            case R.id.testRight:
                PlayTone.Sound = false;
                TestLeft.setBackgroundColor(Color.rgb(107,112,225));
                TestRight.setBackgroundColor(Color.rgb(175,58,83));
                break;
            case R.id.testfrequencydown:
                if(frequencyCount > 0) {
                    frequencyCount -= 1;
                    frequencytext.setText(realfrequency[frequencyCount] + " Hz");
                }
                break;
            case R.id.testfrequencyup:
                if(frequencyCount < 8) {
                    frequencyCount += 1;
                    frequencytext.setText(realfrequency[frequencyCount] + " Hz");
                }

                break;
            case R.id.test0_5down:
                if(Amp >= 0.5) {
                    Amp = Amp - 0.5f;
                    Amp = Math.round(Amp * 10) / 10.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_5up:
                if(Amp <= 0.5) {
                    Amp = Amp + 0.5f;
                    Amp = Math.round(Amp * 10) / 10.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_1down:
                if(Amp >= 0.1) {
                    Amp = Amp - 0.1f;
                    Amp = Math.round(Amp * 10) / 10.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_1up:
                if(Amp <= 0.9) {
                    Amp = Amp + 0.1f;
                    Amp = Math.round(Amp * 10) / 10.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_05down:
                if(Amp >= 0.05) {
                    Amp -= 0.05;
//                    Log.d("Amplitute = ", "Amp " + Amp);
                    Amp = Math.round(Amp * 100) / 100.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_05up:
                if(Amp <= 0.95) {
                    Amp += 0.05;
                    Amp = Math.round(Amp * 100) / 100.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_01down:
                if(Amp >= 0.01) {
                    Amp -= 0.01;
                    Amp = Math.round(Amp * 100) / 100.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_01up:
                if(Amp <= 0.99) {
                    Amp += 0.01;
                    Amp = Math.round(Amp * 100) / 100.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_005down:
                if(Amp >= 0.005) {
                    Amp -= 0.005;
                    Amp = Math.round(Amp * 1000) / 1000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_005up:
                if(Amp <= 0.995) {
                    Amp += 0.005;
                    Amp = Math.round(Amp * 1000) / 1000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_001down:
                if(Amp >= 0.001) {
                    Amp -= 0.001;
                    Amp = Math.round(Amp * 1000) / 1000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_001up:
                if(Amp <= 0.999) {
                    Amp += 0.001;
                    Amp = Math.round(Amp * 1000) / 1000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_0005down:
                if(Amp >= 0.0005) {
                    Amp -= 0.0005;
                    Amp = Math.round(Amp * 10000) / 10000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_0005up:
                if(Amp <= 0.9995) {
                    Amp += 0.0005;
                    Amp = Math.round(Amp * 10000) / 10000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_0001down:
                if(Amp >= 0.0001) {
                    Amp -= 0.0001;
                    Amp = Math.round(Amp * 10000) / 10000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_0001up:
                if(Amp <= 0.9999) {
                    Amp += 0.0001;
                    Amp = Math.round(Amp * 10000) / 10000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_00005down:
                if(Amp >= 0.00005) {
                    Amp -= 0.00005;
                    Amp = Math.round(Amp * 100000) / 100000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_00005up:
                if(Amp <= 0.99995) {
                    Amp += 0.00005;
                    Amp = Math.round(Amp * 100000) / 100000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_00001down:
                if(Amp >= 0.00001) {
                    Amp -= 0.00001;
                    Amp = Math.round(Amp * 100000) / 100000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_00001up:
                if(Amp <= 0.99999) {
                    Amp += 0.00001;
                    Amp = Math.round(Amp * 100000) / 100000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_000001down:
                if(Amp >= 0.000001) {
                    Amp -= 0.000001;
                    Amp = Math.round(Amp * 1000000) / 1000000.0f;
                    setTextFunction();
                }
                break;
            case R.id.test0_000001up:
                if(Amp <= 0.999999) {
                    Amp += 0.000001;
                    Amp = Math.round(Amp * 1000000) / 1000000.0f;
                    setTextFunction();
                }
                break;
            case R.id.testPlay:
                if(isPlaying == true) {
                    Play = true;
                    PlayTone.getInstance().stop();
                    PlayButton.setBackgroundColor(Color.rgb(245,21,69));
                    PlayTone.getInstance().makeTone(realfrequency[frequencyCount], duration, Amp);
                    PlayTone.getInstance().play();
                    isPlaying = true;
                }
                if(isPlaying == false) {
                    PlayButton.setBackgroundColor(Color.rgb(245,21,69));
                    PlayTone.getInstance().makeTone(realfrequency[frequencyCount], duration, Amp);
                    PlayTone.getInstance().play();
                    isPlaying = true;
                }
                break;
            case R.id.testStop:
                isPlaying = false;
                PlayButton.setBackgroundColor(Color.rgb(244,119,145));
                PlayTone.getInstance().stop();

        }
    }

    class SecondThread2 implements Runnable {
        public void run() {
            while (true) {

                if(Play == true) {
                    count = 0;
                    Play = false;
                }
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
                if(count == duration) {
                    count = 0;
                    mHandler.sendEmptyMessage(0);
                    Play = true;
                    isPlaying = false;
                }
            }
        }
    }

    void setTextFunction() {
        String temp = String.format("%.6f", Amp);
        Amptext.setText(temp);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                PlayButton.setBackgroundColor(Color.rgb(244,119,145));
            }
        }
    };

}
