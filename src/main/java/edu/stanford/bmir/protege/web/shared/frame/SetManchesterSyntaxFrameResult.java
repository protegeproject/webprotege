package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    private String frameText;


    private SetManchesterSyntaxFrameResult() {
    }

    public SetManchesterSyntaxFrameResult(EventList<ProjectEvent<?>> eventList, String frameText) {
        this.eventList = checkNotNull(eventList);
        this.frameText = checkNotNull(frameText);
    }

    public String getFrameText() {
        return frameText;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventList, frameText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetManchesterSyntaxFrameResult)) {
            return false;
        }
        SetManchesterSyntaxFrameResult other = (SetManchesterSyntaxFrameResult) obj;
        return this.frameText.equals(other.frameText) && this.eventList.equals(other.eventList);
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameResult")
                .add("frameText", frameText)
                .addValue(eventList)
                .toString();
    }
}
