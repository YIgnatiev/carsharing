package com.vakoms.meshly.databases;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */
public class TodoContentProvider extends ContentProvider {


    public static final String TODO_TABLE_NAME = "todos";
    public static final String AUTHORITY = TodoContentProvider.class.getCanonicalName();
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS_FLAG = "status_flag";
    private static final int TODO = 1;
    private static final int TODO_ID = 2;
    private static HashMap<String, String> projectionMap;
    private static final UriMatcher sUriMatcher;


    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.androidhacks.todo";
    public static final String CONTENT_TYPE_ID = "vnd.android.cursor.item/vnd.androidhacks.todo";


    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TODO_TABLE_NAME);
    private DataBaseHelper dbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TODO_TABLE_NAME, TODO);
        sUriMatcher.addURI(AUTHORITY, TODO_TABLE_NAME + "/#", TODO_ID);


        projectionMap = new HashMap<String, String>();
        projectionMap.put(COLUMN_ID, COLUMN_ID);
        projectionMap.put(COLUMN_SERVER_ID, COLUMN_SERVER_ID);
        projectionMap.put(COLUMN_TITLE, COLUMN_TITLE);
        projectionMap.put(COLUMN_STATUS_FLAG, COLUMN_STATUS_FLAG);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case TODO:
                qb.setTables(TODO_TABLE_NAME);
                qb.setProjectionMap(projectionMap);
                break;
            case TODO_ID:
                qb.setTables(TODO_TABLE_NAME);
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;


            default:
                throw new RuntimeException("Unknown URI");
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);


        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
