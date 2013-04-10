package edu.stanford.bmir.protege.web.client.ui.library.timelabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 * <p>
 *     A label which displays the elapsed time since some base time.  The display shows elapsed time in a coarse grained
 *     textual way e.g. "less than one minute ago", "2 minutes ago", "one hour ago" etc.
 * </p>
 */
public class ElapsedTimeLabel extends Composite {

    public static final int ONE_MINUTE = 60 * 1000;

    public static final int ONE_HOUR = 60 * 60 * 1000;

    public static final int ONE_DAY = 24 * 60 * 60 * 1000;



    private static String getTimeRendering(long timestamp) {
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
        if(delta < ONE_DAY) {
            int hours = (int) delta / ONE_HOUR;
            if(hours == 1) {
                return "one hour ago";
            }
            else {
                return hours + " hours ago";
            }
        }
        int days = (int) delta / ONE_DAY;
        if(days == 1) {
            return "1 day ago";
        }
        else {
            return days + " days ago";
        }
    }

    interface TimeLabelUiBinder extends UiBinder<HTMLPanel, ElapsedTimeLabel> {

    }

    @UiField
    protected Label elapsedTimeLabel;

    private long baseTime = getCurrentTime();

    private static long getCurrentTime() {
        return new Date().getTime();
    }


    private static TimeLabelUiBinder ourUiBinder = GWT.create(TimeLabelUiBinder.class);

    public ElapsedTimeLabel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    /**
     * Sets the base time from which the elapsed time is computed.
     * @param baseTime The base time.  Time since this time is computed as elapsed time.
     */
    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
        updateDisplay();
    }

    /**
     * Gets the set base time.  By default the base time is automatically set to the current construction time.
     * @return The base time as a timestamp.
     */
    public long getBaseTime() {
        return baseTime;
    }


    /**
     * Causes the text on the panel to be updated to reflect the time elapsed between the base time and now.
     */
    public void update() {
        updateDisplay();
    }

    private void updateDisplay() {
        String rendering = getTimeRendering(baseTime);
        elapsedTimeLabel.setText(rendering);
    }

}