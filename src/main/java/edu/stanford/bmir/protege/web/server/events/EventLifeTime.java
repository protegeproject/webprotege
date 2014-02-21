package edu.stanford.bmir.protege.web.server.events;

import java.util.concurrent.TimeUnit;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class EventLifeTime {

    private long eventLifeTimeMS;

    private EventLifeTime(long eventLifeTimeMS) {
        this.eventLifeTimeMS = eventLifeTimeMS;
    }

    public static EventLifeTime getInMilliseconds(long timeInMilliseconds) {
        return new EventLifeTime(timeInMilliseconds);
    }

    public long getEventLifeTimeInMilliseconds() {
        return eventLifeTimeMS;
    }

    public static EventLifeTime get(long time, TimeUnit unit) {
        return getInMilliseconds(TimeUnit.MILLISECONDS.convert(time, unit));
    }


    @Override
    public int hashCode() {
        return "EventLifeTime".hashCode() + (int) eventLifeTimeMS;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EventLifeTime)) {
            return false;
        }
        EventLifeTime other = (EventLifeTime) obj;
        return this.eventLifeTimeMS == other.eventLifeTimeMS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventLifeTime");
        sb.append("(");
        sb.append(eventLifeTimeMS);
        sb.append("ms");
        sb.append(")");
        return sb.toString();
    }
}
