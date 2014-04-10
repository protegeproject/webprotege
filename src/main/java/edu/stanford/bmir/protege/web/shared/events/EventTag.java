package edu.stanford.bmir.protege.web.shared.events;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 * <p>
 *     Represents a tag at some point in an event list.  Each tag can be compared with another tag.
 * </p>
 */
@Immutable
public class EventTag implements Serializable, IsSerializable, Comparable<EventTag> {

    private static final EventTag FIRST = new EventTag(0);

    private int ordinal;

    /**
     * For serialization only
     */
    private EventTag() {

    }

    private EventTag(int ordinal) {
        this.ordinal = ordinal;
    }

    public static EventTag getFirst() {
        return FIRST;
    }

    public static EventTag get(int index) {
        return new EventTag(index);
    }

    /**
     * Gets the tag that comes immediately after this tag.
     * @return The tag that comes immediately after this tag. Not {@code null}.
     */
    public EventTag next() {
        return EventTag.get(ordinal + 1);
    }

    /**
     * Gets the ordinal of this {@link EventTag}.
     * @return An int representing the ordinal of this {@link EventTag}.
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Determines whether this {@link EventTag} is greater or equal to the specified {@link EventTag}.
     * @param tag The tag.
     * @return {@code true} if this {@link EventTag} is greater or equal to {@code tag}, otherwise {@code false}.
     */
    public boolean isGreaterOrEqualTo(EventTag tag) {
        return this.ordinal >= tag.ordinal;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return "EventManagerTag".hashCode() + ordinal;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EventTag)) {
            return false;
        }
        EventTag other = (EventTag) obj;
        return this.ordinal == other.ordinal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventTag");
        sb.append("(");
        sb.append(ordinal);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int compareTo(EventTag o) {
        return this.ordinal - o.ordinal;
    }
}
