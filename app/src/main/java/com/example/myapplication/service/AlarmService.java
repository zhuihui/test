package com.example.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.myapplication.database.Alarm;
import com.example.myapplication.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

public class AlarmService extends Service {

    private AlarmManager manager;
    private static final String TAG = "AlarmService";
    private long intervalTime = 1000*60*60*24;
    Intent i;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("AlarmService","onCreate executed");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AlarmService","onStartCommand executed");
        Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
        int id = alarm.getId();
        Long calendar = intent.getLongExtra("calendar",0);
        i = new Intent(this, AlarmReceiver.class);
        i.putExtra("name", alarm.getName());
        Log.i("name:", alarm.getName());
        int result = intent.getIntExtra("result",0);
        switch (result){
            case 1:
                startAlarm(id,i,calendar,intervalTime);
                Log.d(TAG, "Alarm is Set:"+alarm.toString());
                break;
            case 2:
                stopAlarm(id);
                break;
            case 3:
                if(alarm.isIsopen()){
                    stopAlarm(id);
                    startAlarm(alarm.getId(),i,calendar,intervalTime);
                    Log.d(TAG, "Alarm is Set:"+alarm.toString());
                }
                else {
                    stopAlarm(id);
                }
        }
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("AlarmService","onDestroy executed");
    }

    public void startAlarm(int id, Intent i, Long calendar, long intervalTime) {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar, intervalTime, pi);
                Log.d(TAG, "Alarm is Set,id为"+id);
                Log.d(TAG, "现在时间是:" + new Date().toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Alarm is not Set: " + e.toString());
            }
        }


        public void stopAlarm(int id) {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            try {
                manager.cancel(PendingIntent.getBroadcast(this, id, i, 0));
                Log.d(TAG, "Alarm is Canceled,id为"+id);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Alarm is not Canceled:" + e.toString());
            }

        }

}




