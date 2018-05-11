package com.skyfree.speed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skyfree.speed.model.Result;

import java.util.ArrayList;

/**
 * Created by KienBeu on 4/26/2018.
 */

public class DataHandler  extends SQLiteOpenHelper {

    private ArrayList<Result> mListResult;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RESULT_SPEED";
    private static final String TABLE_RESULT = "TABLE_RESULT";

    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_DOWNLOAD = "KEY_DOWNLOAD";
    private static final String KEY_UPLOAD = "KEY_UPLOAD";
    private static final String KEY_PING = "KEY_PING";

    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULT_TABLE = "CREATE TABLE " + TABLE_RESULT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TYPE + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_DOWNLOAD + " TEXT,"
                + KEY_UPLOAD + " TEXT,"
                + KEY_PING + " TEXT" + ")";
        db.execSQL(CREATE_RESULT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addResult(Result rs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, rs.getType());
        values.put(KEY_DATE, rs.getDate());
        values.put(KEY_DOWNLOAD, rs.getSpeedDownload());
        values.put(KEY_UPLOAD, rs.getSpeedUpload());
        values.put(KEY_PING, rs.getPing());
        db.insert(TABLE_RESULT, null, values);
        db.close();
    }

    public void deleteAllResult(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_RESULT;
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<Result> getListResult(){
        mListResult = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select " + KEY_ID + "," + KEY_TYPE + "," + KEY_DATE + "," + KEY_DOWNLOAD + "," + KEY_UPLOAD + "," + KEY_PING + " from " + TABLE_RESULT;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            mListResult.add(new Result(cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        }
        return mListResult;
    }
}
