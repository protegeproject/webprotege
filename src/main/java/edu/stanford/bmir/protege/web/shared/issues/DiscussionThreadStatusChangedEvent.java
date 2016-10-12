package edu.stanford.bmir.protege.web.shared.issues;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class DiscussionThreadStatusChangedEvent extends ProjectEvent<DiscussionThreadStatusChangedHandler> {

    public static final Event.Type<DiscussionThreadStatusChangedHandler> ON_STATUS_CHANGED = new Event.Type<>();

    private ProjectId projectId;

    private ThreadId threadId;

    private Status status;

    public DiscussionThreadStatusChangedEvent(@Nonnull ProjectId projectId,
                                              @Nonnull ThreadId threadId,
                                              @Nonnull Status status) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.status = checkNotNull(status);
    }

    @GwtSerializationConstructor
    private DiscussionThreadStatusChangedEvent() {
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public Event.Type<DiscussionThreadStatusChangedHandler> getAssociatedType() {
        return ON_STATUS_CHANGED;
    }

    @Override
    protected void dispatch(DiscussionThreadStatusChangedHandler handler) {
        handler.handleDiscussionThreadStatusChanged(this);
    }
}
