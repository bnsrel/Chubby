package com.bb.chubby;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.provider.FontRequest;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    /** Change this to {@code false} when you want to use the downloadable Emoji font. */
    private static final boolean USE_BUNDLED_EMOJI = true;
    private CardView btnPlay, btnExit;
    private TextView txtCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPointers();
        setActions();


        initEmojiCompat();
    }


    private void setPointers() {
        btnPlay = findViewById(R.id.btn_play);
        btnExit = findViewById(R.id.btn_exit);
        txtCredits = findViewById(R.id.txt_credits);
    }

    private void setActions() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

        txtCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreditsActivity.class));
//                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    private void initEmojiCompat() {
        final EmojiCompat.Config config;
        if (USE_BUNDLED_EMOJI) {
            // Use the bundled font for EmojiCompat
            config = new BundledEmojiCompatConfig(getApplicationContext());
        } else {
            // Use a downloadable font for EmojiCompat
            final FontRequest fontRequest = new FontRequest(
                    "com.google.android.gms.fonts",
                    "com.google.android.gms",
                    "Noto Color Emoji Compat",
                    R.array.com_google_android_gms_fonts_certs);
            config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest);
        }

        config.setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i(TAG, "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e(TAG, "EmojiCompat initialization failed", throwable);
                    }
                });

        EmojiCompat.init(config);
    }
}