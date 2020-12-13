package com.bb.chubby.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.bb.chubby.R;

public class SoundHelper {
    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;
    private static SoundPool soundPool;
    private static int hitSound;
    private static int gameOverSound;
    private static int minusHeartSound;


    public SoundHelper(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(audioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        }

        hitSound = soundPool.load(context, R.raw.got_gift, 1);
        gameOverSound = soundPool.load(context, R.raw.gameover, 1);
        minusHeartSound = soundPool.load(context, R.raw.fail, 1);
    }

    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void gameOverSound() {
        soundPool.play(gameOverSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }


    public void minusLive() {
        soundPool.play(minusHeartSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
