package com.changwon.wooogi.frequency;

/**
 * Created by wooogi on 2017. 7. 30..
 */
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

public class PlayTone {
    int ch = 2;     // Stereo 채널
    private static final PlayTone INSTANCE = new PlayTone();
    public static boolean Sound = true;

    public static PlayTone getInstance() {
        return INSTANCE;
    }
    private boolean isPlaying = false;
    //    private final int freqOfTone=1000;
//    private final int duration = 10;
    private AudioTrack audioTrack = null;
    //    private float volume = 1.0f;
    byte[] generatedSnd;

    void makeTone(int freqOfTone, int duration, float volume) {
        if(audioTrack != null) {
            Log.d("audioTrack is " , "not null");
            audioTrack.stop();
//            audioTrack.release();
//            audioTrack = null;
        }
        if (!isPlaying) {
            isPlaying = true;

            int sampleRate = 44100;// 44.1 KHz

            double dnumSamples = (double)  ch * duration * sampleRate;
            dnumSamples = Math.ceil(dnumSamples);
            int numSamples = (int) dnumSamples;
            double[] sample = new double[numSamples];
            generatedSnd = new byte[2 * numSamples];

            for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
                sample[i] = Math.sin(2 * freqOfTone * Math.PI * i / (sampleRate * 2));
            }

            // convert to 16 bit pcm sound array
            // assumes the sample buffer is normalized.
            // convert to 16 bit pcm sound array
            // assumes the sample buffer is normalised.
            int idx = 0;
            int i;

            int ramp = numSamples / 20;  // Amplitude ramp as a percent of sample count

            for (i = 0; i < ramp; i+=2) {  // Ramp amplitude up (to avoid clicks)
                double dVal = sample[i];
                // Ramp up to maximum
                final short val = (short) (dVal * 32767 * i / ramp);
                // in 16 bit wav PCM, first byte is the low order byte

                if(Sound) {
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                }

                else {
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                }
            }

            for (i = i; i < numSamples - ramp;
                 i+=2) {                        // Max amplitude for most of the samples
                double dVal = sample[i];
                // scale to maximum amplitude
                final short val = (short) (dVal * 32767);
                // in 16 bit wav PCM, first byte is the low order byte
                if(Sound) {
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                }

                else {
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                }
            }

            for (i = i; i < numSamples; i+=2) { // Ramp amplitude down
                double dVal = sample[i];
                // Ramp down to zero
                final short val = (short) (dVal * 32767 * (numSamples - i) / ramp);
                // in 16 bit wav PCM, first byte is the low order byte
                if(Sound) {
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                }

                else {
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = 0;
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                }
            }


            try {
                int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT);
                audioTrack =
                        new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                audioTrack.setNotificationMarkerPosition(numSamples);
//                Log.d("bufferSize " , "=" + bufferSize);
//                Log.d("numSamples " , "=" + numSamples);
                audioTrack.setPlaybackPositionUpdateListener(
                        new AudioTrack.OnPlaybackPositionUpdateListener() {
                            @Override public void onPeriodicNotification(AudioTrack track) {
                                // nothing to do
                            }

                            @Override public void onMarkerReached(AudioTrack track) {
//                                toneStoppedListener.onToneStopped();
                            }
                        });

                // Sanity Check for max volume, set after write method to handle issue in android
                // v 4.0.3
                float maxVolume = AudioTrack.getMaxVolume();

                if (volume > maxVolume) {
                    volume = maxVolume;
                } else if (volume < 0) {
                    volume = 0;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    audioTrack.setVolume(volume);
                } else {
                    audioTrack.setStereoVolume(volume, volume);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            stop();

        }
    }

    void play() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
//                Log.d("thread", "Thread");
                audioTrack.play();
                audioTrack.write(generatedSnd, 0, generatedSnd.length);
            }
        };
        t1.start();
    }

    void stop() {
        audioTrack.stop();
        isPlaying = false;
//        audioTrack.release();
    }

}
