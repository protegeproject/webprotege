package edu.stanford.bmir.protege.web.shared.issues;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class DiscussionThreadCreatedEvent extends ProjectEvent<DiscussionThreadCreatedHandler> implements HasProjectId {

    public static final transient Event.Type<DiscussionThreadCreatedHandler> ON_DISCUSSION_THREAD_CREATED = new Event.Type<>();

    private EntityDiscussionThread thread;

    public DiscussionThreadCreatedEvent(EntityDiscussionThread thread) {
        super(thread.getProjectId());
        this.thread = thread;
    }

    @GwtSerializationConstructor
    private DiscussionThreadCreatedEvent() {
    }

    public EntityDiscussionThread getThread() {
        return thread;
    }

    @Override
    public Event.Type<DiscussionThreadCreatedHandler> getAssociatedType() {
        return ON_DISCUSSION_THREAD_CREATED;
    }

    @Override
    protected void dispatch(DiscussionThreadCreatedHandler handler) {
        handler.handleDiscussionThreadCreated(this);
    }

}
