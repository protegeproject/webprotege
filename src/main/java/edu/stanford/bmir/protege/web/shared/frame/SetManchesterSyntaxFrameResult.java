package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

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
        this.eventList = eventList;
        this.frameText = checkNotNull(frameText);
    }

    public String getFrameText() {
        return frameText;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
