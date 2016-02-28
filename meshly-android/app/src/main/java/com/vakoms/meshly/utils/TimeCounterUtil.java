package com.vakoms.meshly.utils;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.text.SimpleDateFormat;
import java.util.Date;

import meshly.vakoms.com.meshly.R;

/**
 * Created by taras.melko on 8/26/14.
 * Modified & fixed by Sviatoslav Kashchin on 24.01.2015
 */
public class TimeCounterUtil {

    private static final String TAG = "TimeCounterUtil";
    private static SimpleDateFormat formatter = new SimpleDateFormat("EEEE");


    public static String getChatTime(long chatTime, Context context) {
        DateTime now = new DateTime();
        DateTime then = new DateTime(chatTime * 1000);

        Days jd = Days.daysBetween(then, now);
        Hours jh = Hours.hoursBetween(then, now).minus(jd.toStandardHours());
        Minutes jm = Minutes.minutesBetween(then, now).minus(jd.toStandardMinutes()).minus(jh.toStandardMinutes());
        Seconds js = Seconds.secondsBetween(then, now).minus(jd.toStandardSeconds()).minus(jh.toStandardSeconds()).minus(jm.toStandardSeconds());

        int day = jd.getDays();
        int hour = jh.getHours();
        int min = jm.getMinutes();
        int sec = js.getSeconds();

        if (day == 0 && hour == 0 && min == 0 && sec < 30) {
            return context.getResources().getString(R.string.inbox_JustNow);
        }

        if (day == 0 && hour == 0 && min == 0 && sec < 60) {
            return context.getResources().getString(R.string.inbox_Seconds);
        }

        if (day == 0 && hour == 0 && min < 60) {
            return context.getResources().getQuantityString(R.plurals.inboxTimeCaptionsForMinutes, min, min);
        }

        if (day == 0 && hour < 24) {
            return context.getResources().getQuantityString(R.plurals.inboxTimeCaptionsForHours, hour, hour);
        }

        if (day > 1 && day <= 7) {
            return formatter.format(new Date(chatTime * 1000));
        }

        if (day < 30) {
            return context.getResources().getQuantityString(R.plurals.inboxTimeCaptionsForDays, day, day);

        }

        if (day < 365) {
            return context.getResources().getString(R.string.inbox_FewMonthsAgo);
        }

        return context.getResources().getString(R.string.inbox_YearsAgo);
    }

    /**
     * Convert string from exponential notation to normal
     *
     * @param expLong "long" string in exponential notation
     * @return long value
     */
    public static long convertExpLongToLong(String expLong) {
        if (expLong == null) {
            return 0;
        }

        return Double.valueOf(expLong).longValue();
    }

    /**
     * Get string representation how long user was seen. <br/>
     * For example, user was seen FEB 1, 2015. Current date FEB 2, 2015. Method will return
     * "yesterday"
     *
     * @param lastSeenTime last seen time in ms divided by 1000
     * @return string representation when was user seen
     */
    public static String calculateNearby(long lastSeenTime) {
        if (lastSeenTime == 0) {
            return "";
        }

        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - lastSeenTime;

        long days = (Math.round(elapsedTime) / 86400);
        long hours = (Math.round(elapsedTime) / 3600) - (days * 24);
        long minutes = (Math.round(elapsedTime) / 60) - (days * 1440) - (hours * 60);

        //just now
        if (days == 0 && hours == 0 && minutes <= 10) {
            return "Just now";
        }

        //more then 10 min, but no more then 59m
        if (days == 0 && hours == 0 && minutes > 10 && minutes <= 59) {
            return String.format("%dm ago", minutes);
        }

        //from 1 to 3 hours
        if (days == 0 && hours >= 1 && hours <= 3) {
            return String.format("%dh ago", hours);
        }

        //convert minutes and days to hours
        hours += minutes / 60 + days * 24;

        //more then 3 hours, but no more then 24
        if (hours > 3 && hours <= 24) {
            return "today";
        }

        if (hours > 24 && hours <= 48) {
            return "yesterday";
        }

        //more then 48 h - must be empty
        return "";
    }

    /**
     * Get string representation when user mailed to you. <br/>
     * For example, user mailed JAN 1, 2015. Current date  JAN 24, 2015. Method will return
     * "3w ago" (3 weeks ago)
     *
     * @param mailTime mail time in ms divided by 1000
     * @return string representation when user mailed to you
     */
    public static String calculateTimeInChat(long mailTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - mailTime;

        long days = (Math.round(elapsedTime) / 86400);
        long hours = (Math.round(elapsedTime) / 3600) - (days * 24);
        long minutes = (Math.round(elapsedTime) / 60) - (days * 1440) - (hours * 60);

        //just now
        if (days == 0 && hours == 0 && minutes <= 5) {
            return "Just now";
        }

        //more then 5 min, but no more then 1h
        if (days == 0 && hours == 0 && minutes > 5) {
            return String.format("%d" + "m ago", minutes);
        }

        //more then 1h
        if (days == 0 && hours > 0) {
            return String.format("%d" + "h ago", hours);
        }

        //less then 1 month
        if (days < 30) {
            if (days == 1) {
                return "Yesterday";
            }

            //1-7 days, will show day name (Monday, Friday etc)
            if (days > 1 && days < 7) {
                return formatter.format(new Date(mailTime * 1000));
            }

            //1 week - 7 or more days
            if (days >= 7 && days < 14) {
                return "1w ago";
            }

            //two weeks
            if (days >= 14 && days < 21) {
                return "2w ago";
            }

            //
            if (days >= 21 && days < 28) {
                return "3w ago";
            }

            //28-29 days
            return "4w ago";
        }

        //more then 1 month
        if (days < 365) {
            return ((int) days / 30) + "mo ago";
        }

        //more then 1 year
        return ((int) days / 365) + "y ago";
    }

    public static String timeCalculateEvents(long eventStartTime, Context context) {
        DateTime now = new DateTime();
        DateTime event = new DateTime(eventStartTime * 1000);

        int days = Days.daysBetween(now, event).getDays();

        if (days <= 0) {
            return "Today";
        }

        if (days < 7) {
            return context.getResources().getQuantityString(R.plurals.eventTimeCaptionsForDays, days, days);
        }

        if (days < 30) {
            days = Math.round((float) days / (float) 7);
            return context.getResources().getQuantityString(R.plurals.eventTimeCaptionsForWeeks, days, days);
        }

        if (days < 365) {
            days = Math.round((float) days / (float) 30);
            return context.getResources().getQuantityString(R.plurals.eventTimeCaptionsForMonths, days, days);
        }

        days = Math.round((float) days / (float) 365);

        return context.getResources().getQuantityString(R.plurals.eventTimeCaptionsForYears, days, days);
    }
}