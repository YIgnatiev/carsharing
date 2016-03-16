package com.vakoms.meshly.databases;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallModelMy;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/15/15.
 */
public class  UserProvider extends ContentProvider {


    public static final String AUTH = "com.vakoms.meshly.databases.UserProvider";
    public static final Uri USERS_URI = Uri.parse("content://" + AUTH + "/" + MeshlyColumns.getTableName(NewUser.class));
    public static final Uri USER_ME_URI = Uri.parse("content://" + AUTH + "/" + MeshlyColumns.getTableName(UserMe.class));
    public static final Uri EVENTS_URI = Uri.parse("content://" + AUTH + "/" + MeshlyColumns.getTableName(Event.class));
    public static final Uri WALL_URI = Uri.parse("content://" + AUTH + "/" + MeshlyColumns.getTableName(WallModel.class));
    public static final Uri WALL_MY_URI = Uri.parse("content://" + AUTH + "/" + MeshlyColumns.getTableName(WallModelMy.class));

    public static final int NEW_USER = 1;
    public static final int USER_ME = 2;
    public static final int EVENTS = 3;
    public static final int WALL = 4;
    public static final int WALL_MY = 5;

    DataBaseHelper mDbHelper;

    private final static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTH, MeshlyColumns.getTableName(NewUser.class), NEW_USER);
        mUriMatcher.addURI(AUTH, MeshlyColumns.getTableName(UserMe.class), USER_ME);
        mUriMatcher.addURI(AUTH, MeshlyColumns.getTableName(Event.class), EVENTS);
        mUriMatcher.addURI(AUTH, MeshlyColumns.getTableName(WallModel.class), WALL);
        mUriMatcher.addURI(AUTH, MeshlyColumns.getTableName(WallModelMy.class), WALL_MY);

    }


    @Override
    public boolean onCreate() {
        mDbHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {

            case NEW_USER:
                cursor = db.query(MeshlyColumns.getTableName(NewUser.class), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;

            case USER_ME:
                cursor = db.query(MeshlyColumns.getTableName(UserMe.class), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;

            case EVENTS:
                cursor = db.query(MeshlyColumns.getTableName(Event.class), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case WALL:
                cursor = db.query(MeshlyColumns.getTableName(WallModel.class), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case WALL_MY:
                cursor = db.query(MeshlyColumns.getTableName(WallModelMy.class), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {


        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (mUriMatcher.match(uri)) {
            case NEW_USER:
                db.insertWithOnConflict(MeshlyColumns.getTableName(NewUser.class),null,values,SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case USER_ME:
                db.insertWithOnConflict(MeshlyColumns.getTableName(UserMe.class),null,values,SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case EVENTS:
                db.insertWithOnConflict(MeshlyColumns.getTableName(Event.class),null,values,SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case WALL:
                db.insertWithOnConflict(MeshlyColumns.getTableName(WallModel.class),null,values,SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case WALL_MY:
                db.insertWithOnConflict(MeshlyColumns.getTableName(WallModelMy.class),null,values,SQLiteDatabase.CONFLICT_REPLACE);
                break;

        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (mUriMatcher.match(uri)){
            case NEW_USER:
                db.delete(MeshlyColumns.getTableName(NewUser.class),null,null);
                break;
            case USER_ME:
                db.delete(MeshlyColumns.getTableName(UserMe.class),null,null);
                break;
            case EVENTS:
                db.delete(MeshlyColumns.getTableName(Event.class),null,null);
                break;
            case WALL:
                db.delete(MeshlyColumns.getTableName(WallModel.class),null,null);
                break;
            case WALL_MY:
                db.delete(MeshlyColumns.getTableName(WallModelMy.class),null,null);
                break;


        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
