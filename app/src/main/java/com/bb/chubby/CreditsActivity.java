package com.bb.chubby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreditsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

    }

    public void returnToMain(View view) {
        finish();
        startActivity(new Intent(CreditsActivity.this, MainActivity.class));
    }
}