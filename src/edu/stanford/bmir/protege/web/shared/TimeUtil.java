package edu.stanford.bmir.protege.web.shared;

import com.google.gwt.i18n.shared.DateTimeFormat;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class TimeUtil {


    public static final int ONE_MINUTE = 60 * 1000;

    public static final int ONE_HOUR = 60 * 60 * 1000;

    public static final int ONE_DAY = 24 * 60 * 60 * 1000;

    public static final DateTimeFormat DAY_MONTH_FORMAT = DateTimeFormat.getFormat("dd MMMM");

    public static final DateTimeFormat DAY_MONTH_YEAR_FORMAT = DateTimeFormat.getFormat("dd MMMM YYYY");


    public static long getCurrentTime() {
        return new Date().getTime();
    }

    public static String getTimeRendering(long timestamp) {
        long currentTimestamp = getCurrentTime();
        long delta = currentTimestamp - timestamp;
        if(delta <= ONE_MINUTE) {
            return "Less than one minute ago";
        }
        if(delta < ONE_HOUR) {
            int mins = (int) delta / ONE_MINUTE;
            if(mins == 1) {
                return "one minute ago";
            }
            else {
                return mins + " minutes ago";
            }
        }
        final Date timestampDate = new Date(timestamp);
        final Date todayDate = new Date();

        final int timestampDay = timestampDate.getDay();
        final int todayDay = todayDate.getDay();
        if(todayDay - timestampDay == 1) {
            return "yesterday";
        }
        else if (delta < ONE_DAY) {
            int hours = (int) delta / ONE_HOUR;
            if(hours == 1) {
                return "one hour ago";
            }
            else {
                return hours + " hours ago";
            }
        }
        // Not sure of a nice way to do this
        if(timestampDate.getYear() == todayDate.getYear()) {
            return DAY_MONTH_FORMAT.format(timestampDate);
        }
        else {
            return DAY_MONTH_YEAR_FORMAT.format(timestampDate);
        }
    }

}
