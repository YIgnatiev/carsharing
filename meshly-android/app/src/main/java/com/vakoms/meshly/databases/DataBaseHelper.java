package com.vakoms.meshly.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallModelMy;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "meshly.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MeshlyColumns.generateTableFromClass(NewUser.class));
        db.execSQL(MeshlyColumns.generateTableFromClass(UserMe.class));
        db.execSQL(MeshlyColumns.generateTableFromClass(Event.class));
        db.execSQL(MeshlyColumns.generateTableFromClass(WallModel.class));
        db.execSQL(MeshlyColumns.generateTableFromClass(WallModelMy.class));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MeshlyColumns.getTableName(NewUser.class));
        db.execSQL("DROP TABLE IF EXISTS " + MeshlyColumns.getTableName(UserMe.class));
        onCreate(db);
    }
}
