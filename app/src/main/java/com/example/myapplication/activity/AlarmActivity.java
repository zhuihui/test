package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.service.MusicService;

public class AlarmActivity extends Activity {

    private TextView textView;
    private ImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        textView = findViewById(R.id.text_alarm);
        imageButton = findViewById(R.id.ImageButton);


        String name = getIntent().getStringExtra("name");
        Log.i("name:",name);
//        String name = "闹钟";
        textView.setText(name);
        imageButton.setImageResource(R.drawable.close);

        Intent i = new Intent(this, MusicService.class);
        startService(i);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, MusicService.class);
                stopService(intent);
                AlarmActivity.this.finish();
            }
        });
    }
}
