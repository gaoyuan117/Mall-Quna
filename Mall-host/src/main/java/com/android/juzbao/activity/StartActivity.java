package com.android.juzbao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.juzbao.activity.jifen.GuideActivity;
import com.android.quna.activity.R;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, TabHostActivity.class));
                finish();
                timer.cancel();
            }
        }, 3000);
    }
}
