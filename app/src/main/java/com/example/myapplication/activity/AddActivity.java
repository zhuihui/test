package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.myapplication.collector.ActivityCollector;
import com.example.myapplication.database.Alarm;
import com.example.myapplication.database.AlarmDao;
import com.example.myapplication.service.AlarmService;
import com.example.myapplication.R;

import java.util.Calendar;

public class AddActivity extends BaseActivity implements View.OnClickListener {

    private Alarm alarm;
    private EditText editText;
    private TimePicker timePicker_spinner;
    private RadioGroup radioGroup;
    private boolean isopen;
    private ImageView imageButton;

    private Calendar c = Calendar.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_add);
        editText = findViewById(R.id.edit_name);
        editText.setText("闹钟");
        radioGroup = findViewById(R.id.radiogroup);
        imageButton = findViewById(R.id.setdata_timepicker);
        imageButton.setImageResource(R.drawable.dui);
        timePicker_spinner = findViewById(R.id.time_piker_spinner);
        //设置闹钟是否开启

        imageButton.setOnClickListener(this);
        //设置其显示方式为24小时（如果参数为false则分为AM与PM显示）
        timePicker_spinner.setIs24HourView(true);
        //自动获取焦点
        timePicker_spinner.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        //设置初始值
        Calendar cal = Calendar.getInstance();//获取现在的时间
        timePicker_spinner.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker_spinner.setCurrentMinute(cal.get(Calendar.MINUTE));
        //通过OnTimeChangedListener来得到当前的时与分
        timePicker_spinner.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            if(c.getTimeInMillis()<System.currentTimeMillis()) {
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //添加闹钟
            case R.id.setdata_timepicker:

                String alarm_name = editText.getText().toString();
                if (TextUtils.isEmpty(alarm_name)) {
                    alarm_name = "闹钟";
                } else {
                }
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_isopen:
                        isopen = true;
                        break;
                    case R.id.radio_noopen:
                        isopen = false;
                        break;
                }
                alarm = new Alarm(alarm_name,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),isopen);
                AlarmDao alarmDao = new AlarmDao(getApplicationContext());
                int id = alarmDao.lastId()+1;
                alarm.setId(id);
                new Thread(() -> {
                    alarmDao.addAlarm(alarm);
                    //如果选了开，现在就开启闹钟
                    if(isopen){
                        Intent intent = new Intent(this,AlarmService.class);
                        intent.putExtra("calendar",c.getTimeInMillis());
                        intent.putExtra("alarm", alarm);
                        intent.putExtra("result",1);
                        startService(intent);
                    }
                }).start();
                Intent resultData = new Intent();
                resultData.putExtra("alarm", alarm);
                this.setResult(1,resultData);
                this.finish();
                break;
            default:
                break;
        }
    }

    /*private void addAlarm(Alarm alarm) throws SQLException {
        AlarmDao alarmDao = new AlarmDao(getApplicationContext());
        //DataOpenHelper dataOpenHelper = new DataOpenHelper(this);
        //Dao<Alarm, Integer> dao = dataOpenHelper.getDao(Alarm.class);
        alarmDao.addAlarm(alarm);
    }*/
}