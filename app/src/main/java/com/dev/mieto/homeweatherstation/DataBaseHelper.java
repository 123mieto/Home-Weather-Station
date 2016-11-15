package com.dev.mieto.homeweatherstation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Mieto on 2016-11-08.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    public static final String DATABASE_NAME = "temp_db";

    public static final String TEMPERATURES_TAB = "temp_tab";
    public static final String TEMPERATURES_ID = "t_id";
    public static final String TEMPERATURES_TIME = "time";
    public static final String TEMPERATURES_VAL = "temperature";
    public static final String TEMPERATURES_DATE = "date";

    public static final String LOGGING_TAB = "log_tab";
    public static final String LOGGING_ID = "id";
    public static final String LOGGING_START_TIME = "start_time";
    public static final String LOGGING_STOP_TIME = "stop_time";
    public static final String LOGGING_CONN_IP = "connector_ip";

    public static final String LIGHT_TAB = "light_tab";
    public static final String LIGHT_ID = "l_id";
    public static final String LIGHT_TIME = "time";
    public static final String LIGHT_VAL = "light";
    public static final String LIGHT_DATE = "date";

    private SQLiteDatabase mDb;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TEMPERATURES_TAB + " ("
                    + TEMPERATURES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TEMPERATURES_VAL + " TEXT, "
                    + TEMPERATURES_TIME + " TEXT, "
                    + TEMPERATURES_DATE + " TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + LIGHT_TAB + " ("
                    + LIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LIGHT_VAL + " TEXT, "
                    + LIGHT_TIME + " TEXT, "
                    + LIGHT_DATE + " TEXT)");
        } catch (SQLException e) {
            Log.d(TAG, "createUserTab: " + e.getMessage());
        }

        mDb = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEMPERATURES_TAB);
        db.execSQL("DROP TABLE IF EXISTS " + LIGHT_TAB);
        onCreate(db);
    }

    public boolean insertTemperatureData(int[] values, long[] times, String date) {
        ContentValues contentValues = new ContentValues();
        boolean isDate = false;
        long result;
        //Check if there is no such result in db
        Cursor cr = mDb.rawQuery("SELECT CASE WHEN " + TEMPERATURES_DATE + " = '" + date + "' THEN 1" +
                "  ELSE 0 END AS isDate FROM  " + TEMPERATURES_TAB + ";", null);
        if (cr.moveToFirst()) {
            isDate = (cr.getInt(cr.getColumnIndex("isDate")) == 1);
        }

        if (!isDate) {
            StringBuffer valBuff = new StringBuffer();
            for (int val : values) {
                valBuff.append(val);
                valBuff.append(" ");
            }
            contentValues.put(TEMPERATURES_VAL, valBuff.toString());

            StringBuffer timesBuff = new StringBuffer();
            for (long time : times) {
                timesBuff.append(time);
                timesBuff.append(" ");
            }
            contentValues.put(TEMPERATURES_TIME, timesBuff.toString());
            contentValues.put(TEMPERATURES_DATE, date);
            result = mDb.insert(TEMPERATURES_TAB, null, contentValues);
        } else {
            result = -1;
        }

        return result != -1;
    }

    public boolean insertLightData(int[] values, long[] times, String date) {
        ContentValues contentValues = new ContentValues();
        boolean isDate = false;
        long result;
        //Check if there is no such result in db
        Cursor cr = mDb.rawQuery("SELECT CASE WHEN " + LIGHT_DATE + " = '" + date + "' THEN 1" +
                "  ELSE 0 END AS isDate FROM  " + LIGHT_TAB + ";", null);
        if (cr.moveToFirst()) {
            isDate = (cr.getInt(cr.getColumnIndex("isDate")) == 1);
        }

        if (!isDate) {
            StringBuffer valBuff = new StringBuffer();
            for (int val : values) {
                valBuff.append(val);
                valBuff.append(" ");
            }
            contentValues.put(LIGHT_VAL, valBuff.toString());

            StringBuffer timesBuff = new StringBuffer();
            for (long time : times) {
                timesBuff.append(time);
                timesBuff.append(" ");
            }
            contentValues.put(LIGHT_TIME, timesBuff.toString());
            contentValues.put(LIGHT_DATE, date);
            result = mDb.insert(LIGHT_TAB, null, contentValues);
        } else {
            result = -1;
        }

        return result != -1;
    }

    public ArrayList<DayDataLight> selectLightData() {
        ArrayList<DayDataLight> elems = new ArrayList<>();
        Cursor cr;
        try {
            /*"DISTINCT " is a little hack to use query with all its benefits but have all distinct elements*/
            cr = mDb.query(LIGHT_TAB, new String[]{"DISTINCT " + LIGHT_VAL, LIGHT_TIME, LIGHT_DATE}, null, null, null, null, LIGHT_DATE + " DESC", "10");
        } catch (SQLiteException e) {
            throw new SQLiteException(e.getMessage(), e.getCause());
        }

        if (cr.moveToFirst()) {
            while (cr.moveToNext()) {
                DayDataLight ddLtemp = new DayDataLight();
                String tStr = cr.getString(0); //LIGHT_VAL
                String[] lStr = tStr.split("\\s+");
                int[] lTemp = new int[lStr.length];
                for (int i = 0; i < lStr.length; i++) {
                    try {
                        lTemp[i] = Integer.valueOf(lStr[i]);
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "selectLightData: " + e.getMessage());
                        lTemp = new int[0];
                        break;
                    }
                }
                ddLtemp.setLight(lTemp);

                tStr = cr.getString(1); //LIGHT_TIME
                lStr = tStr.split("\\s+");
                long[] llTemp = new long[lStr.length];
                for (int i = 0; i < lStr.length; i++) {
                    try {
                        llTemp[i] = Integer.valueOf(lStr[i]);
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "selectLightData: " + e.getMessage());
                        llTemp = new long[0];
                        break;
                    }
                }
                ddLtemp.setTimes(llTemp);

                tStr = cr.getString(2); //LIGHT_DATE
                ddLtemp.setDate(tStr);

                elems.add(ddLtemp);
            }
        }
        return elems;
    }

    public ArrayList<DayDataTemp> selectTemperatureData() {
        ArrayList<DayDataTemp> elems = new ArrayList<>();
        Cursor cr;
        try {
            /*"DISTINCT " is a little hack to use query with all its benefits but have all distinct elements*/
            cr = mDb.query(TEMPERATURES_TAB, new String[]{"DISTINCT " + TEMPERATURES_VAL, TEMPERATURES_TIME, TEMPERATURES_DATE}, null, null, null, null, TEMPERATURES_DATE + " DESC", "10");
        } catch (SQLiteException e) {
            throw new SQLiteException(e.getMessage(), e.getCause());
        }

        if (cr.moveToFirst()) {
            while (cr.moveToNext()) {
                DayDataTemp ddLtemp = new DayDataTemp();
                String tStr = cr.getString(0); //LIGHT_VAL
                String[] lStr = tStr.split("\\s+");
                int[] lTemp = new int[lStr.length];
                for (int i = 0; i < lStr.length; i++) {
                    try {
                        lTemp[i] = Integer.valueOf(lStr[i]);
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "selectLightData: " + e.getMessage());
                        lTemp = new int[0];
                        break;
                    }
                }
                ddLtemp.setTemperatures(lTemp);

                tStr = cr.getString(1); //LIGHT_TIME
                lStr = tStr.split("\\s+");
                long[] llTemp = new long[lStr.length];
                for (int i = 0; i < lStr.length; i++) {
                    try {
                        llTemp[i] = Integer.valueOf(lStr[i]);
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "selectLightData: " + e.getMessage());
                        llTemp = new long[0];
                        break;
                    }
                }
                ddLtemp.setTimes(llTemp);

                tStr = cr.getString(2); //LIGHT_DATE
                ddLtemp.setDate(tStr);

                elems.add(ddLtemp);
            }
        }
        return elems;
    }
}
