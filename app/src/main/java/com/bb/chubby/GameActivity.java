package com.bb.chubby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.widget.EmojiTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bb.chubby.util.SoundHelper;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements GameStateListener {
    private static final String TAG = "GameActivity";
    private int lives = 3;
    private int score = 0;
    private int level = 1;
    private int speed = 3500;
    private int dropId = 0;
    private boolean gameOver = false;
    private boolean started = false;
    private boolean resume = true;

    private int chubbyXpos = 0;
    private float dx;

    private GameStateListener gameStateListener;

    private TextView txtLives, txtLevel, txtScore, readySetGoText;

    private ImageView imgChubby, imgPause;

    private RelativeLayout board;
    private int boardHeight;
    private int boardWidth;

    private SoundHelper sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setPointers();


    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    private void readySetGo() {
        final Integer[] i = {4};
        new CountDownTimer(i[0] * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                readySetGoText.setVisibility(View.VISIBLE);
                i[0]--;
                if (i[0].equals(0) ){
                    readySetGoText.setText("GO!");
                }else{
                    readySetGoText.setText(String.valueOf(i[0]));
                }

            }

            public void onFinish() {
                readySetGoText.setVisibility(View.GONE);
                startGame();
            }
        }.start();

    }

    private void setPointers() {
        gameStateListener = this;
        sound = new SoundHelper(getApplicationContext());
        board = findViewById(R.id.rl_game_board);
        imgChubby = findViewById(R.id.img_chubby);
        imgPause = findViewById(R.id.img_pause);
        txtLevel = findViewById(R.id.txt_level);
        txtLives = findViewById(R.id.txt_lives);
        txtScore = findViewById(R.id.txt_score);
        readySetGoText = findViewById(R.id.txt_readySetGo);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dx = chubbyXpos - event.getRawX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX() + dx;
                    int rightBorder = boardWidth - imgChubby.getWidth();
                    if (!(x > 0 && x < rightBorder))
                        return false;
                    chubbyXpos = (int) x;
                    imgChubby.animate()
                            .x(x)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;

    }

    private void startGame() {
        updateGui();
        new CountDownTimer(Long.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
//                if (gameOver || dropId > 100 || score > 1000) {
                if (gameOver || score >= 1000) {
                    cancel();
                    String s = "success";
                    gameStateListener.onGameOver(s);

                }else if(resume) {
                    dropId++;
                    int xPos = new Random().nextInt(boardWidth - 250) + 50;
                    Droplet droplet = new Droplet(dropId, xPos);
                    createDropletImage(droplet);
                }
            }

            public void onFinish() {
            }
        }.start();
    }

    private void createDropletImage(Droplet droplet) {
        EmojiTextView dropletImage = new EmojiTextView(getApplicationContext());
        dropletImage.setTextColor(Color.BLACK);
        dropletImage.setX(droplet.getxPos());
        dropletImage.setText(droplet.getEmoji());
        dropletImage.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);
        dropletImage.setTag(dropId);
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        dropletImage.setLayoutParams(params);

        TranslateAnimation anim = new TranslateAnimation(0f, 0f, 0, boardHeight);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(speed);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                board.removeView(dropletImage);
                if (droplet.getxPos() > chubbyXpos - 100 && droplet.getxPos() < chubbyXpos + 100) {
                    consumeDropletImage(droplet);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        dropletImage.startAnimation(anim);


        board.addView(dropletImage);
    }

    private void consumeDropletImage(Droplet droplet) {
        if (droplet.getType() == DropletType.GAMEOVER) {
            lives--;
            vibrate();
            // make a sound
            updateGui();
            if (lives == 0)
                gameOver = true;
        }

        int prevScore = score;
        if (score + droplet.getType().getPoints() > 1000){
            score = 1000;
            gameOver = true;
        }else {
            score += droplet.getType().getPoints();
        }

        if (level < 10 && score / 100 > prevScore / 100 ) {
            level++;
            speed *= 0.95;
        }
        updateGui();


    }

    private void updateGui() {
        txtLives.setText(String.valueOf(lives));
        txtScore.setText(String.valueOf(score));
        txtLevel.setText(String.valueOf(level));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewTreeObserver vto = board.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                board.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                boardWidth = board.getMeasuredWidth();
                boardHeight = board.getMeasuredHeight();
                chubbyXpos = boardWidth / 2;
                if (started){
                    startGame();
                    started = true;
                }else {
                    readySetGo();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        resume = false;
        started = true;
    }

    @Override
    public void onGameOver(String s) {
        sound.gameOverSound();
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish();
    }
}

interface GameStateListener {
    void onGameOver(String s);
}