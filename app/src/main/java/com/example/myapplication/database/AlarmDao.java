package com.example.myapplication.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class AlarmDao {
    private DataOpenHelper helper;
    private Dao<Alarm,Integer> alarmDao;

    public AlarmDao(Context applicationContext) {
        try {
            helper = DataOpenHelper.getHelper(applicationContext);
            alarmDao = helper.getDao(Alarm.class);
            if (alarmDao == null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 增
     * @param alarm
     */
    public void addAlarm(Alarm alarm) {
        try {
            alarmDao.create(alarm);
            Log.i("AlarmDao","add a alarm："+ alarm.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删（通过实体）
     * @param alarm
     */
    public void delAlarm(Alarm alarm) {
        try {
            alarmDao.delete(alarm);
            Log.i("AlarmDao","delete alarm："+ alarm.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删（通过id）
     * @param id
     */
    public void delAlarmById(Integer id) {
        try {
            alarmDao.deleteById(id);
            Log.i("AlarmDao","delete alarm："+id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改
     * @param alarm
     */
    public void updateAlarm(Alarm alarm) {
        try {
            alarmDao.update(alarm);
            Log.i("AlarmDao","update alarm："+ alarm.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAlarm(Alarm alarm, int id) {
        alarm.setId(id);
        try {
            alarmDao.update(alarm);
            Log.i("AlarmDao","update alarm："+ alarm.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查
     * @return
     */
    public ArrayList<Alarm> queryAllAlarm() {
        ArrayList<Alarm> alarms = null;
        try {
            alarms = (ArrayList<Alarm>) alarmDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarms;
    }

    public Alarm findAlarmById(int id){
        Alarm alarm = null;
        try{
            alarm = alarmDao.queryBuilder().where().eq("id", id).query().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarm;
    }

    public int lastId(){
        int id = 0;
        try {
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) alarmDao.queryBuilder().orderBy("id", false).query();
            if(!alarms.isEmpty()){
                Alarm alarm = alarms.get(0);
                Log.i("AlarmDao","the last alarm is:"+ alarm.toString());
                id = alarm.getId();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


}
