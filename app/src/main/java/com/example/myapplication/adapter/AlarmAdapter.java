package com.example.myapplication.adapter;

/**
 * Created by hewie on 2019/6/10
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.database.Alarm;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Alarm> alarms;

    public AlarmAdapter(Activity activity, ArrayList<Alarm> alarms){
        this.activity = activity;
        this.alarms = alarms;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alarms.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v;

        if(convertView == null) {
            LayoutInflater lif = activity.getLayoutInflater();
            v = lif.inflate(R.layout.alarm_item, null);
        }else{
            v = convertView;
        }
        Alarm alarm = alarms.get(position);

        TextView alarmName = v.findViewById(R.id.alarm_name);
        TextView alarmTime = v.findViewById(R.id.alarm_time);
        ImageView alarmIsOpen = v.findViewById(R.id.alarm_isopen);
        alarmName.setText(alarm.getName());
        alarmTime.setText(alarm.getHour() + ":" + alarm.getMin());
        if(alarm.isIsopen() == true){
            alarmIsOpen.setVisibility(View.VISIBLE);
        }else {
            alarmIsOpen.setVisibility(View.INVISIBLE);
        }
        return v;
    }
}

