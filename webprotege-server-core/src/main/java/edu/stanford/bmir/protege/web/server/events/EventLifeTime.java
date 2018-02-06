package edu.stanford.bmir.protege.web.server.events;

import com.google.common.base.MoreObjects;

import java.util.concurrent.TimeUnit;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 20/03/2013
 */
public class EventLifeTime {

    private long eventLifeTimeMS;

    private EventLifeTime(long eventLifeTimeMS) {
        this.eventLifeTimeMS = eventLifeTimeMS;
    }

    public static EventLifeTime getInMilliseconds(long timeInMilliseconds) {
        return new EventLifeTime(timeInMilliseconds);
    }

    public static EventLifeTime get(long time, TimeUnit unit) {
        return getInMilliseconds(TimeUnit.MILLISECONDS.convert(time, unit));
    }

    public long getEventLifeTimeInMilliseconds() {
        return eventLifeTimeMS;
    }

    @Override
    public int hashCode() {
        return "EventLifeTime".hashCode() + (int) eventLifeTimeMS;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EventLifeTime)) {
            return false;
        }
        EventLifeTime other = (EventLifeTime) obj;
        return this.eventLifeTimeMS == other.eventLifeTimeMS;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("EventLifeTime")
                          .add("lifetime ms", eventLifeTimeMS)
                          .toString();
    }
}
