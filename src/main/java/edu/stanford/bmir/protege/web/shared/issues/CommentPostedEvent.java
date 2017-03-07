package edu.stanford.bmir.protege.web.shared.issues;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public class CommentPostedEvent extends ProjectEvent<CommentPostedHandler> implements HasProjectId {


    public static final transient Event.Type<CommentPostedHandler> ON_COMMENT_POSTED = new Event.Type<>();

    private ProjectId projectId;

    private ThreadId threadId;

    private Comment comment;

    @Nullable
    private OWLEntityData entity;

    public CommentPostedEvent(@Nonnull ProjectId projectId,
                              @Nonnull ThreadId threadId,
                              @Nonnull Comment comment,
                              @Nonnull Optional<OWLEntityData> entity) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.comment = checkNotNull(comment);
        this.entity = checkNotNull(entity).orElse(null);
    }

    @GwtSerializationConstructor
    private CommentPostedEvent() {
    }

    @Override
    public Event.Type<CommentPostedHandler> getAssociatedType() {
        return ON_COMMENT_POSTED;
    }

    @Override
    protected void dispatch(CommentPostedHandler handler) {
        handler.handleCommentPosted(this);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    public Comment getComment() {
        return comment;
    }

    public Optional<OWLEntityData> getEntity() {
        return Optional.ofNullable(entity);
    }
}
