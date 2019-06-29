package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AlarmAdapter;
import com.example.myapplication.database.Alarm;
import com.example.myapplication.database.AlarmDao;
import com.example.myapplication.service.AlarmService;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private ListView listView;
    private ImageView imageButton;
    private ArrayList<Alarm> alarms;
    private AlarmAdapter alarmAdapter;
    private static Alarm theAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.ImageButton);
        imageButton.setImageResource(R.drawable.jia);
        listView = findViewById(R.id.ListView);
        //数据库操作
        AlarmDao alarmDao = new AlarmDao(getApplicationContext());
        alarms = alarmDao.queryAllAlarm();
        //适配器
        alarmAdapter = new AlarmAdapter(this,alarms);
        listView.setAdapter(alarmAdapter);
        //listView的监听
        listView.setOnItemClickListener(this);
        //相应单击某一个闹钟
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm a = alarms.get(position);
                if(a.isIsopen()){
                    a.setIsopen(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AlarmDao alarmDao1 = new AlarmDao(getApplicationContext());
                            alarmDao1.updateAlarm(a);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                            calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
                            calendar.set(Calendar.MINUTE, a.getMin());
                            Log.i(TAG,"主页页传过去的时间"+calendar.getTime());
                            Intent intent = new Intent(MainActivity.this,AlarmService.class);
                            intent.putExtra("calendar",calendar.getTimeInMillis());
                            intent.putExtra("alarm",a);
                            intent.putExtra("result",2);
                            startService(intent);
                        }
                    }).start();
                }else {
                    a.setIsopen(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AlarmDao alarmDao1 = new AlarmDao(getApplicationContext());
                            alarmDao1.updateAlarm(a);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                            calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
                            calendar.set(Calendar.MINUTE, a.getMin());
                            Log.i(TAG,"主页页传过去的时间"+calendar.getTime());
                            Intent intent = new Intent(MainActivity.this,AlarmService.class);
                            intent.putExtra("calendar",calendar.getTimeInMillis());
                            intent.putExtra("alarm",a);
                            intent.putExtra("result",1);
                            startService(intent);
                        }
                    }).start();
                }
                alarmAdapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarmOfThis = alarms.get(position);
                theAlarm = alarmOfThis;
                Intent intentToEdit = new Intent(MainActivity.this, EditActivity.class);
                intentToEdit.putExtra("oneAlarm", alarmOfThis);
                startActivityForResult(intentToEdit, 2);
                return true;
            }
        });

        imageButton.setOnClickListener(v -> {
            switch (v.getId()){
                case R.id.ImageButton:
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivityForResult(intent,1);
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        alarmAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == 1){
                    Alarm newAlarm = (Alarm)data.getSerializableExtra("alarm");
                    this.alarms.add(newAlarm);
                    alarmAdapter.notifyDataSetChanged();
                    Log.i(TAG,"页面更新了");
                }
                break;
            case 2:
                if (resultCode == 1){
                    Alarm newAlarm = (Alarm)data.getSerializableExtra("edit");
//                    Toast.makeText(this,newAlarm.toString(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "edit: " + newAlarm);
                    //alarmOfThis.setId(newAlarm.getId());
                    theAlarm.setName(newAlarm.getName());
                    theAlarm.setHour(newAlarm.getHour());
                    theAlarm.setMin(newAlarm.getMin());
                    theAlarm.setIsopen(newAlarm.isIsopen());
                    alarmAdapter.notifyDataSetChanged();
                }
                else if(resultCode == 2){
                    alarms.remove(theAlarm);
                    alarmAdapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
