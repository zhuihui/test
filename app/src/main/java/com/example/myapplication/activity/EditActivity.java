package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.myapplication.R;
import com.example.myapplication.collector.ActivityCollector;
import com.example.myapplication.database.Alarm;
import com.example.myapplication.database.AlarmDao;
import com.example.myapplication.service.AlarmService;

import java.util.Calendar;

public class EditActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "EditActivity";
    private Alarm alarm;
    private Alarm alarm2;
    private int id;
    private EditText editText;
    private TimePicker timePicker;
    private boolean isopen;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private ImageView imageView1;
    private ImageView imageView2;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_edit);

        alarm = (Alarm) getIntent().getSerializableExtra("oneAlarm");

        id = alarm.getId();
        Log.i("EditActivity","attempt to update alarm:"+alarm.toString());
        editText = findViewById(R.id.edit_edit_name);
        editText.setText(alarm.getName());

        radioGroup = findViewById(R.id.edit_radiogroup);
        radioButton1 = findViewById(R.id.radio_edit_isopen);
        radioButton2 = findViewById(R.id.radio_edit_noopen);
        if(alarm.isIsopen()){
            radioButton1.setChecked(true);
        }
        else {
            radioButton2.setChecked(true);
        }

        timePicker = findViewById(R.id.time_edit_piker_spinner);
        timePicker.setIs24HourView(true);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setCurrentHour(alarm.getHour());
        timePicker.setCurrentMinute(alarm.getMin());

//        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY,alarm.getHour());
        c.set(Calendar.MINUTE,alarm.getMin());
        if(c.getTimeInMillis()<System.currentTimeMillis()) {
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
        }
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            if(c.getTimeInMillis()<System.currentTimeMillis()) {
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
            }
        });

        imageView1 = findViewById(R.id.image_delete);
        imageView1.setImageResource(R.drawable.delete);
        imageView2 = findViewById(R.id.image_edit_dui);
        imageView2.setImageResource(R.drawable.dui);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_edit_dui:
                String alarm_name1 = editText.getText().toString();
                if (TextUtils.isEmpty(alarm_name1)) {
                    alarm_name1 = "闹钟";
                } else {
                }
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_edit_isopen:
                        isopen = true;
                        break;
                    case R.id.radio_edit_noopen:
                        isopen = false;
                        break;
                }
                alarm2 = new Alarm(id,alarm_name1,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),isopen);
                AlarmDao alarmDao1 = new AlarmDao(getApplicationContext());
                alarmDao1.updateAlarm(alarm2);
                Log.i(TAG,"编辑页传过去的时间"+c.getTime());
                Intent intent = new Intent(this,AlarmService.class);
                intent.putExtra("calendar",c.getTimeInMillis());
                intent.putExtra("alarm", alarm2);
                intent.putExtra("result",3);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startService(intent);
                    }
                }).start();
                Intent resultData1 = new Intent();
                resultData1.putExtra("edit", alarm2);
                this.setResult(1,resultData1);
                this.finish();
                break;
            case R.id.image_delete:
                String alarm_name2 = editText.getText().toString();
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_edit_isopen:
                        isopen = true;
                        break;
                    case R.id.radio_edit_noopen:
                        isopen = false;
                        break;
                }
                alarm2 = new Alarm(id,alarm_name2,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),isopen);
                AlarmDao alarmDao2 = new AlarmDao(getApplicationContext());
                alarmDao2.delAlarm(alarm2);
                Intent intent2 = new Intent(this,AlarmService.class);
                intent2.putExtra("calendar",c.getTimeInMillis());
                intent2.putExtra("alarm", alarm2);
                intent2.putExtra("result",2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startService(intent2);
                    }
                }).start();
                Intent resultData2 = new Intent();
                resultData2.putExtra("delete", alarm2);
                this.setResult(2,resultData2);
                this.finish();
                break;
        }
    }


}
