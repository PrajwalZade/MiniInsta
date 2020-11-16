package com.prajwal.miniinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageResource(R.drawable.insta);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new  Intent(StartActivity.this, StartActivity1.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
