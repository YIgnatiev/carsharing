package com.morfitrun.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vasia on 19.03.2015.
 */
public abstract class DateWorker {

    private static final SimpleDateFormat dateInFormatterDirty = new SimpleDateFormat("yyyy-MM-dd'T04':HH:mm.ss'Z'");
    private static final SimpleDateFormat dateInFormatterClean = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat dateOutFormatterForTideTableDataMainDate = new SimpleDateFormat("dd-MM-yyyy");
    //time for HIGH and LOW tide values
    private static final SimpleDateFormat timeOutFormatterForTideValues = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @param _dateStr in format "2014-01-18T00:00:00Z"
     * @return
     */
    public static final String formatServerDate(final String _dateStr) {
        if (_dateStr == null) return "";
        if (_dateStr.isEmpty()) return _dateStr;

        String result = "";
        try {
            final Date date = dateInFormatterDirty.parse(_dateStr);
            result = dateOutFormatterForTideTableDataMainDate.format(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param _dateStr in format "2014-01-23 00:38:00"
     * @return
     */
    public static final String formatTimeForTideValues(final String _dateStr) {
        if (_dateStr.isEmpty()) return _dateStr;

        String result = "";
        try {
            final Date date = dateInFormatterClean.parse(_dateStr);
            result = timeOutFormatterForTideValues.format(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
