package com.example.labapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db"; // название бд
    private static final int SCHEMA = 1; // версия бд

    private static final String TAG = "log";

    public static final String TABLE_1 = "USERS";
    public static final String T1_COLUMN_ID = "id_1";
    public static final String T1_COLUMN_LOGIN = "login";
    public static final String T1_COLUMN_PASSWORD = "password";

    public static final String TABLE_2 = "DATA";
    public static final String T2_COLUMN_ID = "id_2";
    public static final String T2_COLUMN_USERS_ID = "user_id";
    public static final String T2_COLUMN_NAME = "name";
    public static final String T2_COLUMN_COMPANY = "company";
    public static final String T2_COLUMN_POST = "post";


    public DataBase(Context context){
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_1 + " (" + T1_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T1_COLUMN_LOGIN + " TEXT," + T1_COLUMN_PASSWORD + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_2 + " (" + T2_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T2_COLUMN_USERS_ID + " TEXT, " +  T2_COLUMN_NAME + " TEXT," + T2_COLUMN_COMPANY + " TEXT," + T2_COLUMN_POST + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
