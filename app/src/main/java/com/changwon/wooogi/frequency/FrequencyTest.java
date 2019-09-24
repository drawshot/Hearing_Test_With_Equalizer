package com.changwon.wooogi.frequency;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FrequencyTest extends AppCompatActivity {

    private int duration = 3; // seconds
    private int sampleRate = 44100;
    private double freqOfTone = 8000; // hz
    private short val;
    private TextView text_View;
    private TextView text_dval;
    private TextView text_vol;
    private int StreamType = AudioManager.STREAM_MUSIC;

    AudioManager mAudioManager = null;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_test);

        Button btn_1 = (Button) findViewById(R.id.btn_1);
        Button KEYCODE_VOLUME_UP = (Button) findViewById(R.id.KEYCODE_VOLUME_UP);
        Button KEYCODE_VOLUME_DOWN = (Button) findViewById(R.id.KEYCODE_VOLUME_DOWN);


        text_View = (TextView) findViewById(R.id.text_View);
        text_dval = (TextView) findViewById(R.id.text_dval);
        text_vol = (TextView) findViewById(R.id.text_vol);

        KEYCODE_VOLUME_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volUP();

            }
        });

        KEYCODE_VOLUME_DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volDOWN();



            }
        });



        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 1000; // hz
                genTone(1000, 3);
                //  playSound();


            }
        });

        Button btn_2 = (Button) findViewById(R.id.btn_2);

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 2000; // hz
                genTone(2000, 3);
                //   playSound();


            }
        });
        Button btn_4 = (Button) findViewById(R.id.btn_4);

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 4000; // hz
                genTone(4000, 3);
                //   playSound();


            }
        });
        Button btn_8 = (Button) findViewById(R.id.btn_8);

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 8000; // hz
                genTone(8000, 3);
                //   playSound();


            }
        });
        Button btn_22 = (Button) findViewById(R.id.btn_22);

        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 250; // hz
                genTone(250, 3);
                //  playSound();


            }
        });
        Button btn_55 = (Button) findViewById(R.id.btn_55);

        btn_55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freqOfTone = 500; // hz
                genTone(500, 3);
                //  playSound();


            }
        });


    }

    public void volUP(){
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        int currvol = mAudioManager.getStreamVolume(StreamType);
        int maxvol = mAudioManager.getStreamMaxVolume(StreamType);

        if(currvol<=maxvol){ mAudioManager.setStreamVolume(StreamType,currvol+1,AudioManager.FLAG_PLAY_SOUND);
            text_vol.setText("" + currvol + "입니다.");}


    }

    public void volDOWN(){
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        int currvol = mAudioManager.getStreamVolume(StreamType);

        if(currvol>=0){ mAudioManager.setStreamVolume(StreamType,currvol-1,AudioManager.FLAG_PLAY_SOUND);
            text_vol.setText("" + currvol + "입니다.");}

    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP :
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//                return true;
//
//        }
//        return false;
//    }
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP :
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
//                return true;
//
//        }
//        return false;
//    }


    @Override
    protected void onResume() {
        super.onResume();

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


        // Use a new tread as this can take a while
        Thread thread = new Thread(new Runnable()
        {
            public void run() {
                // genTone(1000, 3);
                handler.post(new Runnable()
                {
                    public void run() {
                        //playSound();


                    }
                });
            }
        });

        thread.start();
    }

    public void genTone(double freqOfTone, double duration){


        double dnumSamples = duration * sampleRate;
        dnumSamples = Math.ceil(dnumSamples);
        int numSamples = (int) dnumSamples;
        double sample[] = new double[numSamples];
        byte generatedSnd[] = new byte[2 * numSamples];

        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.

        int idx = 0;
        int i = 0;
//        for (double dVal : sample) {
//             val = (short) (dVal * 32767);
//            generatedSnd[idx++] = (byte) (val & 0x00ff);
//            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
//        }

        int ramp = numSamples / 20;                                    // Amplitude ramp as a percent of sample count


        for (i = 0; i < ramp; ++i) {                                     // Ramp amplitude up (to avoid clicks)
            double dVal = sample[i];
            // Ramp up to maximum
            final short val = (short) ((dVal * 32767 * i / ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }


        for (i = i; i < numSamples - ramp; ++i) {                        // Max amplitude for most of the samples
            double dVal = sample[i];
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (i = i; i < numSamples; ++i) {                               // Ramp amplitude down
            double dVal = sample[i];
            // Ramp down to zero
            final short val = (short) ((dVal * 32767 * (numSamples - i) / ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        AudioTrack audioTrack = null ; // Get audio track

        try {
            int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();                                          // Play the track
            audioTrack.write(generatedSnd, 0, generatedSnd.length);     // Load the track
        } catch (Exception e) {
        }
        if (audioTrack != null) audioTrack.release();
        text_View.setText("" + freqOfTone + "주파수 입니다.");//(끝)
        text_dval.setText("" + val + "데시벨 입니다.");//(끝)

    }

//    void playSound(){
//        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//
//                AudioFormat.ENCODING_PCM_16BIT, numSamples, AudioTrack.MODE_STATIC);
//        audioTrack.write(generatedSnd, 0, numSamples);
//        audioTrack.play();
//
//        text_View.setText("" + freqOfTone + "주파수 입니다.");//(끝)
//        text_dval.setText("" + val + "데시벨 입니다.");//(끝)
//
//    }


}
