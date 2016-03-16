package com.morfitrun.database;

import android.database.Cursor;

/**
 * Created by vasia on 25.03.2015.
 */
public class ColumnIndex {

    private int raceIdIndex, titleIndex, descriptionIndex, startDateIndex, deadlineIndex, repeatIndex, limitIndex;

    public ColumnIndex (Cursor _cursor){

        raceIdIndex       = _cursor.getColumnIndex(DBConstants.KEY_RACE_ID);
        titleIndex        = _cursor.getColumnIndex(DBConstants.KEY_TITLE);
        descriptionIndex  = _cursor.getColumnIndex(DBConstants.KEY_DESCRIPTION);
        startDateIndex    = _cursor.getColumnIndex(DBConstants.KEY_START_DATE);
        deadlineIndex     = _cursor.getColumnIndex(DBConstants.KEY_DEADLINE);
        repeatIndex       = _cursor.getColumnIndex(DBConstants.KEY_REPEAT);
        limitIndex        = _cursor.getColumnIndex(DBConstants.KEY_LIMIT);
    }

    public int getRaceIdIndex() {
        return raceIdIndex;
    }

    public int getTitleIndex() {
        return titleIndex;
    }

    public int getDescriptionIndex() {
        return descriptionIndex;
    }

    public int getStartDateIndex() {
        return startDateIndex;
    }

    public int getLimitIndex() {
        return limitIndex;
    }

    public int getDeadlineIndex() {
        return deadlineIndex;
    }

    public int getRepeatIndex() {
        return repeatIndex;
    }
}
