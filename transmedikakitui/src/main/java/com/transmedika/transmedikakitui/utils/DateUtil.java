package com.transmedika.transmedikakitui.utils;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by codeest on 16/8/13.
 */

public class DateUtil {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Constants.DATE_TIME_ZONE,
            Constants.DATE_TIME_ZONE_1,
            Constants.DATE_TIME_ZONE_2,
            Constants.DATE_TIME_ZONE_3,
            Constants.DATE_TIME_ZONE_4,
            Constants.DATE_TIME_ZONE_5,
            Constants.DATE_TIME_ZONE_6,
            Constants.DATE_TIME_ZONE_7,
            Constants.DATE_TIME_ZONE_8,
            Constants.DATE_TIME_ZONE_9})
    public @interface DateTimeZone { }

    public static String ddMMMyyyy(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_5, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String MMMddyyyy(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_6, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String HHmm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_7, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String dateType9(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_9, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String dateType10(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_10, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String dateType4(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_ZONE_4, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date.getTime());
    }


    public static String calToString(Calendar calendar, String formatDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static Date ddMMMyyyy(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_ZONE_5, Locale.getDefault());
        Date date1 = null;
        try {
            date1 = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static Date toDate(String sDate, @DateTimeZone String dateTimeZone) {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeZone, Locale.getDefault());
        Date date1 = null;
        try {
            date1 = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static boolean isSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
