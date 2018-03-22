package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class UpdateEntityTagsResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    @Nonnull
    public UpdateEntityTagsResult(EventList<ProjectEvent<?>> eventList) {
        this.eventList = checkNotNull(eventList);
    }

    @GwtSerializationConstructor
    private UpdateEntityTagsResult() {
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
