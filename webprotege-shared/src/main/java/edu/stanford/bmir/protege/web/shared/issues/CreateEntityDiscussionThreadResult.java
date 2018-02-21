package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadResult implements Result, HasEventList<ProjectEvent<?>> {

    private ImmutableList<EntityDiscussionThread> threads;

    private EventList<ProjectEvent<?>> eventList;

    public CreateEntityDiscussionThreadResult(@Nonnull ImmutableList<EntityDiscussionThread> threads,
                                              @Nonnull EventList<ProjectEvent<?>> eventList) {
        this.threads = checkNotNull(threads);
        this.eventList = checkNotNull(eventList);
    }

    @GwtSerializationConstructor
    private CreateEntityDiscussionThreadResult() {
    }

    public ImmutableList<EntityDiscussionThread> getThreads() {
        return threads;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
