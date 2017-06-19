package edu.stanford.bmir.protege.web.shared.issues;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class DiscussionThreadStatusChangedEvent extends ProjectEvent<DiscussionThreadStatusChangedHandler> {

    public static final transient Event.Type<DiscussionThreadStatusChangedHandler> ON_STATUS_CHANGED = new Event.Type<>();

    private ProjectId projectId;

    private ThreadId threadId;

    @Nullable
    private OWLEntity entity;

    private int openThreadsForEntity;

    private Status status;

    public DiscussionThreadStatusChangedEvent(@Nonnull ProjectId projectId,
                                              @Nonnull ThreadId threadId,
                                              @Nonnull Optional<OWLEntity> entity,
                                              int openThreadsForEntity,
                                              @Nonnull Status status) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.entity = checkNotNull(entity).orElse(null);
        this.openThreadsForEntity = openThreadsForEntity;
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

    @Nonnull
    public Optional<OWLEntity> getEntity() {
        return Optional.ofNullable(entity);
    }

    /**
     * Gets the open threads for the entity that this thread pertains to.
     * @return The number of open threads.  Undefined if the entity for this thread is not set.
     */
    public int getOpenThreadsForEntity() {
        return openThreadsForEntity;
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


    @Override
    public String toString() {
        return toStringHelper("DiscussionThreadStatusChangedEvent" )
                .addValue(threadId)
                .addValue(entity)
                .addValue(status)
                .toString();
    }
}
