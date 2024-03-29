package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DataOpenHelper extends OrmLiteSqliteOpenHelper {
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public DataOpenHelper(Context context) {
        super(context, "alarm.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Alarm.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if(oldVersion == 1) {
                TableUtils.dropTable(connectionSource, Alarm.class,false);
                TableUtils.createTable(connectionSource, Alarm.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DataOpenHelper instance;

    public static synchronized DataOpenHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DataOpenHelper.class) {
                if (instance == null)
                    instance = new DataOpenHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
