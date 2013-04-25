package edu.stanford.bmir.protege.web.shared;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class TimeUtil {


    private static final int ONE_SECOND = 1000;

    public static final long ONE_MINUTE = 60 * ONE_SECOND;

    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    public static final long ONE_DAY = 24 * ONE_HOUR;

    private static final long ONE_WEEK = 7 * ONE_DAY;

    private static final long ONE_YEAR = 52 * ONE_WEEK;


    private static final String [] DAY_FORMAT = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th", "31st"};

    private static final String [] MONTH_FORMAT = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static long getCurrentTime() {
        return new Date().getTime();
    }

    public static String getTimeRendering(final long timestamp) {
        return getTimeRendering(timestamp, getCurrentTime());
    }

    public static String getTimeRendering(final long timestamp, final long referenceTimestamp) {
        if(isLessThanOneMinuteAgo(timestamp, referenceTimestamp)) {
            return getLessThanOneMinuteAgoRendering();
        }

        if(isLessThanOneHourAgo(timestamp, referenceTimestamp)) {
            return getNMinutesAgoRendering(timestamp, referenceTimestamp);
        }

        if (isSameCalendarDay(timestamp, referenceTimestamp)) {
            return getNHoursAgoRendering(timestamp, referenceTimestamp);
        }

        if(isYesterday(timestamp, referenceTimestamp)) {
            return getYesterdayRendering();
        }

        return getMinimalDayMonthYearRendering(timestamp, referenceTimestamp);
    }

    /**
     * Determines if the specified timestamp is less than one minute before the reference timestamp.
     * @param timestamp The timestamp to test for.
     * @param referenceTimestamp The reference timestamp which the offset is calculated against.
     * @return {@code true} if the value of {@code timestamp} is less than one minute before the value of
     * {@code referenceTimestamp}, otherwise {@code false}.
     */
    public static boolean isLessThanOneMinuteAgo(long timestamp, long referenceTimestamp) {
        final long delta = referenceTimestamp - timestamp;
        return 0 <= delta && delta <= ONE_MINUTE;
    }

    /**
     * Determines if the specified timestamp is less than hour before the reference timestamp.
     * @param timestamp The timestamp to test for.
     * @param referenceTimestamp The reference timestamp which the offset is calculated against.
     * @return {@code true} if the value of {@code timestamp} is less than one hour before the value of
     * {@code referenceTimestamp}, otherwise {@code false}.
     */
    public static boolean isLessThanOneHourAgo(long timestamp, long referenceTimestamp) {
        final long delta = referenceTimestamp - timestamp;
        return 0 <= delta && delta < ONE_HOUR;
    }

    /**
     * Determines if the specified timestamp is less than one week before the reference timestamp.
     * @param timestamp The timestamp to test for.
     * @param referenceTimestamp The reference timestamp which the offset is calculated against.
     * @return {@code true} if the value of {@code timestamp} is less than one week before the value of
     * {@code referenceTimestamp}, otherwise {@code false}.
     */
    public static boolean isLessThanOneWeekAgo(long timestamp, long referenceTimestamp) {
        final long delta = referenceTimestamp - timestamp;
        return 0 <= delta && delta < ONE_WEEK;
    }

    /**
     * Determines if the specified timestamps denote time points within the same calendar day.
     * @param timestamp The timestamp
     * @param referenceTimestamp The timestamp to compare to.
     * @return {@code true} if the specified timestamps denote the same day, otherwise {@code false}.
     */
    @SuppressWarnings("deprecation")
    public static boolean isSameCalendarDay(long timestamp, long referenceTimestamp) {
        Date timestampDate = new Date(timestamp);
        Date referenceTimestampDate = new Date(referenceTimestamp);
        return referenceTimestampDate.getDate() == timestampDate.getDate() && (referenceTimestamp - timestamp < ONE_DAY);
    }

    /**
     * Determines if the specified timestamps denote time points within the same calendar month.
     * @param timestamp The timestamp
     * @param referenceTimestamp The timestamp to compare to.
     * @return {@code true} if the specified timestamps denote the same month, otherwise {@code false}.
     */
    @SuppressWarnings("deprecation")
    public static boolean isSameCalendarMonth(long timestamp, long referenceTimestamp) {
        Date timestampDate = new Date(timestamp);
        Date referenceTimestampDate = new Date(referenceTimestamp);
        return timestampDate.getMonth() == referenceTimestampDate.getMonth() && isSameCalendarYear(timestamp, referenceTimestamp);
    }

    /**
     * Determines if the specified timestamps denote time points within the same calendar year.
     * @param timestamp The timestamp
     * @param referenceTimestamp The timestamp to compare to.
     * @return {@code true} if the specified timestamps denote the same calendar year, otherwise {@code false}.
     */
    @SuppressWarnings("deprecation")
    public static boolean isSameCalendarYear(long timestamp, long referenceTimestamp) {
        Date timestampDate = new Date(timestamp);
        Date referenceTimestampDate = new Date(referenceTimestamp);
        return timestampDate.getYear() == referenceTimestampDate.getYear();
    }


    /**
     * Determines if the specified timestamps denote time points one calendar day apart (this could be less than 24
     * hours apart).
     * @param timestamp The timestamp
     * @param referenceTimestamp The timestamp to compare to.
     * @return {@code true} if the specified timestamps denote one calendar day apart, otherwise {@code false}.
     */
    @SuppressWarnings("deprecation")
    public static boolean isYesterday(long timestamp, long referenceTimestamp) {
        Date timestampDate = new Date(timestamp);
        Date referenceTimestampDate = new Date(referenceTimestamp);
        return referenceTimestampDate.getDate() - timestampDate.getDate() == 1 && isSameCalendarMonth(timestamp, referenceTimestamp) && isSameCalendarYear(timestamp, referenceTimestamp);
    }



    private static String getLessThanOneMinuteAgoRendering() {
        return "Less than one minute ago";
    }


    private static String getNMinutesAgoRendering(long timestamp, long referenceTimestamp) {
        int mins = (int) ((referenceTimestamp - timestamp) / ONE_MINUTE);
        if(mins == 1) {
            return "one minute ago";
        }
        else {
            return mins + " minutes ago";
        }
    }

    private static String getYesterdayRendering() {
        return "yesterday";
    }

    private static String getNHoursAgoRendering(long timestamp, long referenceTimestamp) {
        int hours = (int) ((referenceTimestamp - timestamp) / ONE_HOUR);
        if(hours == 1) {
            return "one hour ago";
        }
        else {
            return hours + " hours ago";
        }
    }

    private static String getNDaysAgoRendering(long timestamp, long referenceTimestamp) {
        int days = (int) ((referenceTimestamp - timestamp) / ONE_DAY);
        if(days == 1) {
            return "one day ago";
        }
        else {
            return days + " days ago";
        }
    }


    private static String getMinimalDayMonthYearRendering(long timestamp, long referenceTimestamp) {
        final Date timestampDate = new Date(timestamp);
        final Date referenceTimestampDate = new Date(referenceTimestamp);

        String rendering = formatDay(timestampDate.getDate()) + " " + formatMonth(referenceTimestampDate.getMonth());
        if(!isSameCalendarYear(timestamp, referenceTimestamp)) {
            rendering += " " + timestampDate.getYear();
        }
        return rendering;
    }


    public static String formatDay(int day) {
        return DAY_FORMAT[day - 1];
    }

    public static String formatMonth(int month) {
        return MONTH_FORMAT[month];
    }

}
