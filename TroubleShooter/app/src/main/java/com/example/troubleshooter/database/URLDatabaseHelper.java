package com.example.troubleshooter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class URLDatabaseHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "URLBase.db";
    public URLDatabaseHelper(Context context){
        super(context ,DATABASE_NAME ,null , DATABASE_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCommands.CREATE_SCEN_TABLE);
        db.execSQL(SQLCommands.CREATE_CASE_TABLE);
        Log.v("test", "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLCommands.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
