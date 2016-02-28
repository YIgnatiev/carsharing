package com.example.troubleshooter.database;


import android.provider.BaseColumns;


public class SQLCommands implements BaseColumns {
    public static final String TEXT_TYPE = "TEXT";
    public static final String INTEGER_TYPE = "INT";

    public static final String TABLE_NAME_SCEN = "scen_table";


    public static final String COLUMN_SCEN_TEXT = "text";

    public static final String CREATE_SCEN_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_SCEN + " (" + SQLCommands._ID + " INTEGER PRIMARY KEY , " +

            COLUMN_SCEN_TEXT + " " + TEXT_TYPE + " )";


    public static final String TABLE_NAME_CASE = "case_table";

    public static final String COLUMN_CASE_IMAGE = "case_image";
    public static final String COLUMN_CASE_TEXT = "case_text";
    public static final String COLUMN_CASE_YES_ID = "case_yes_id";
    public static final String COLUMN_CASE_YES_TEXT = "case_yes_text";
    public static final String COLUMN_CASE_NO_ID = "case_no_id";
    public static final String COLUMN_CASE_NO_TEXT = "case_no_text";


    public static final String CREATE_CASE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_CASE + " ("
            + SQLCommands._ID + " INTEGER PRIMARY KEY , " +

            COLUMN_CASE_IMAGE + " " + TEXT_TYPE+ " ," +
            COLUMN_CASE_TEXT + " " + TEXT_TYPE + " ," +
            COLUMN_CASE_NO_ID + " " + INTEGER_TYPE + " ," +
            COLUMN_CASE_NO_TEXT + " " + TEXT_TYPE+ " ," +
            COLUMN_CASE_YES_TEXT + " " + TEXT_TYPE + " ," +
            COLUMN_CASE_YES_ID + " " + INTEGER_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME_SCEN;
}

