package com.changwon.wooogi.frequency;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VolumeCheck extends AppCompatActivity {

    private TextView TextVol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_check);

//        TextVol = (TextView) findViewById(R.id.editTextVol);

        Button btn_start = (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(VolumeCheck.this,MainTestPre.class);
                startActivity(intent);
                finish();

            }
        });


//        seek.setProgressDrawable(getResources().getDrawable(android.R.drawable.progress);


//        SeekBar seekBarVol = (SeekBar) findViewById(R.id.seekBarVol);
//        seekBarVol.setMax(15);
//        seekBarVol.setProgressDrawable(getResources().getDrawable(R.drawable.progress));

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        int nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        seekBarVol.setMax(nMax); seekBarVol.setProgress(nCurrentVolumn);
//        seekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override public void onStopTrackingTouch(SeekBar seekBar) {
//                // TODO Auto-generated method stub
//            }
//            @Override public void onStartTrackingTouch(SeekBar seekBar) {
//                // TODO Auto-generated method stub
//            }
//            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // TODO Auto-generated method stub
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
//                Toast.makeText(this, "" + String.valueOf(progress) , Toast.LENGTH_SHORT).show();
//                Toast.makeText(VolumeCheck.this, "progress + " + progress, Toast.LENGTH_SHORT).show();
//                progress = progress * 100 / 15;
//
//                 TextVol.setText(String.valueOf(progress) + "%");
//            }
//        });


    }
//    public static void setSeekberThumb(final SeekBar seekBar, final Resources res) {
//        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//
//                Drawable thumb;
//                if (seekBar.getHeight() > 0) {
//                    if(SDK_INT >= 21) {
//                        thumb = res.getDrawable(R.drawable.newvolumecontrolbutton, null);
//                    }
//                    else {
//                        thumb = res.getDrawable(R.drawable.newvolumecontrolbutton);
//                    }
//                    int h = seekBar.getMeasuredHeight();
//                    int w = h;
//                    Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
//                    Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, h, w, true);
//                    Drawable newThumb = new BitmapDrawable(res, bmpScaled);
//                    newThumb.setBounds(0, 0, newThumb.getIntrinsicHeight(), newThumb.getIntrinsicWidth());
//                    seekBar.setThumb(newThumb);
//                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
//                }
//                return true;
//            }
//        });
//    }
}