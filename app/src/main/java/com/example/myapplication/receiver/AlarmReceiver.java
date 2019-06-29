package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.activity.AlarmActivity;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {


        String name = intent.getStringExtra("name");
        Intent intentAlarm = new Intent(context, AlarmActivity.class);
        intentAlarm.putExtra("name",name);
        intentAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAlarm);
        Log.d(TAG, "运行了，现在时间是:" + new Date().toString());
    }
}
