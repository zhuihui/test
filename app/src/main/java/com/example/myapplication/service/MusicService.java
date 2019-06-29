package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.R;

public class MusicService extends Service {
    private MediaPlayer mp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService", "onCreate");
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MusicService", "onStartCommand");
        mp.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MusicService", "onDestroy");
        mp.release();
    }
}
