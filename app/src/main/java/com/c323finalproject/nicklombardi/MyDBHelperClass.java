package com.c323finalproject.nicklombardi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class MyDBHelperClass extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FinalProject.db";
    public static final String TABLE_NAME = "Login_Table";
    public static final String COLUMN_1 = "USERNAME";
    public static final String COLUMN_2 = "PASSWORD";
    public static final String COLUMN_3 = "EMAIL";
    public static final String COLUMN_4 = "PHONE";
    public static final String COLUMN_5 = "PROFILE_PICTURE";
    public static final String COLUMN_6 = "OFFLINE_SETTING";
    public static final String COLUMN_7 = "VIDEO_SETTING";

    public MyDBHelperClass(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (USERNAME TEXT PRIMARY KEY, PASSWORD TEXT, EMAIL TEXT, PHONE TEXT, PROFILE_PICTURE BLOB, OFFLINE_SETTING TEXT, VIDEO_SETTING TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public boolean insertData(String username, String password, String email, String phone, byte[] profile, String offline, String youtube){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, username);
        contentValues.put(COLUMN_2, password);
        contentValues.put(COLUMN_3, email);
        contentValues.put(COLUMN_4, phone);
        contentValues.put(COLUMN_5, profile);
        contentValues.put(COLUMN_6, offline);
        contentValues.put(COLUMN_7, youtube);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null, null);
        return res;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public boolean deleteRow(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_1 + "=?", new String[]{name}) > 0;
    }
}
