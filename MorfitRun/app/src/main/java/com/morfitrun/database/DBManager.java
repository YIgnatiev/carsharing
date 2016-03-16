package com.morfitrun.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.morfitrun.data_models.race_model.Race;
import java.util.ArrayList;

/**
 * Created by vasia on 25.03.2015.
 */
public  class DBManager {

    public static final ArrayList<Race> getRace(Context _context){
        ArrayList<Race> races = new ArrayList<>();
        final SQLiteDatabase database = getReadableDatabase(_context);
        final Cursor cursor = database.query(DBConstants.DICTIONARY_TABLE_NAME, null, null, null, null, null, null);
        final ColumnIndex i = new ColumnIndex(cursor);

        String raceId, title, description, startDate, deadline, repeat;
        int  limit;

        while (cursor.moveToNext()){
            raceId            = cursor.getString(i.getRaceIdIndex());
            title             = cursor.getString(i.getTitleIndex());
            description       = cursor.getString(i.getDescriptionIndex());
            startDate         = cursor.getString(i.getStartDateIndex());
            deadline          = cursor.getString(i.getDeadlineIndex());
            repeat            = cursor.getString(i.getRepeatIndex());
            limit             = cursor.getInt(i.getLimitIndex());

            final Race race = new Race(raceId, title, description, startDate, deadline, repeat, limit);
            races.add(race);
        }
        cursor.close();
        database.close();
        return races;
    }

    public static final void writeToDatabase(Context _context, ArrayList<Race> _races){
        if (_races == null)
            return;
        final SQLiteDatabase database = getWritableDatabase(_context);
        clearDatabase(database);
        for (Race _race : _races) {
            final ContentValues cv = getCV(_race);
            database.insert(DBConstants.DICTIONARY_TABLE_NAME, null, cv);
        }
        database.close();
    }

    public static final void clearDatabase(final SQLiteDatabase _database){
        _database.delete(DBConstants.DICTIONARY_TABLE_NAME, null, null);
    }

//    public static final void deleteRace(final Context _context, final String _dbRaceId){
//        final SQLiteDatabase database = getWritableDatabase(_context);
//        database.delete(DBConstants.DICTIONARY_TABLE_NAME, "id = " + _dbRaceId, null);
//    }
//
//    private static final void addToDatabase(SQLiteDatabase _database, Race _race){
//        final ContentValues cv = getCV(_race);
//        _database.insert(DBConstants.DICTIONARY_TABLE_NAME, null, cv);
//        _database.close();
//    }

//    private static final void updateDatabase(SQLiteDatabase _database, Race _race){
//       int i =  _database.update(DBConstants.DICTIONARY_TABLE_NAME, getCV(_race), "race_id = ?", new String[]{_race.getId()});
//        i = i + 0;
//        _database.close();
//    }

    private static  ContentValues getCV(final Race _race) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_RACE_ID,        _race.getId());
        cv.put(DBConstants.KEY_TITLE,          _race.getTitle());
        cv.put(DBConstants.KEY_DESCRIPTION,    _race.getDescription());
        cv.put(DBConstants.KEY_START_DATE,     _race.getStarDate());
        cv.put(DBConstants.KEY_DEADLINE,       _race.getDeadline());
        cv.put(DBConstants.KEY_REPEAT,         _race.getRepeat());
        cv.put(DBConstants.KEY_LIMIT,          Integer.toString(_race.getLimit()));
        return cv;
    }

    private static SQLiteDatabase getReadableDatabase(Context _context){
        final DBOpenHelper openHelper = new DBOpenHelper(_context);
        return openHelper.getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context _context){
        final DBOpenHelper openHelper = new DBOpenHelper(_context);
        return openHelper.getWritableDatabase();
    }
}
