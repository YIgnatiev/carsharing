package com.morfitrun.database;

/**
 * Created by vasia on 25.03.2015.
 */
public class DBConstants {

    public static final String DB_NAME                     = "db_mfr";
    public static final String DICTIONARY_TABLE_NAME       = "mfr_race";
    public static final int DATABASE_VERSION               = 1;

    public static final String KEY_RACE_ID                 = "race_id";
    public static final String KEY_TITLE                   = "title";
    public static final String KEY_DESCRIPTION             = "description";
    public static final String KEY_START_DATE              = "start_date";
    public static final String KEY_DEADLINE                = "deadline";
    public static final String KEY_REPEAT                  = "repeat";
    public static final String KEY_LIMIT                   = "r_limit";

    public static final String DICTIONARY_TABLE_CREATE     =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    KEY_RACE_ID      + " TEXT, " +
                    KEY_TITLE        + " TEXT, " +
                    KEY_DESCRIPTION  + " TEXT, " +
                    KEY_START_DATE   + " TEXT, " +
                    KEY_DEADLINE     + " TEXT, " +
                    KEY_REPEAT       + " TEXT, " +
                    KEY_LIMIT        + " TEXT)";
}
