package com.example.myapplication.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable(tableName = "t_alarm")
public class Alarm implements Serializable {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;
    @DatabaseField(columnName = "a_name")
    private String name;
    @DatabaseField(columnName = "a_hour")
    private int hour;
    @DatabaseField(columnName = "a_min")
    private int min;
    @DatabaseField(columnName = "a_isopen")
    private boolean isopen;

    public Alarm(){
    }
    public Alarm(int id, String name, int hour, int min, boolean isopen){
        this.id = id;
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.isopen = isopen;
    }

    public Alarm(String name, int hour, int min, boolean isopen){
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.isopen = isopen;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isIsopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", name=" + name +
                ", hour=" + hour +
                ", min=" + min +
                ", isopen=" + isopen +
                '}';
    }
}
