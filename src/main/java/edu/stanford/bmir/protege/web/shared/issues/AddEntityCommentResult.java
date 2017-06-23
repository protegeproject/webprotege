package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddEntityCommentResult implements Result, HasProjectId {

    private ProjectId projectId;

    private ThreadId threadId;

    private Comment comment;

    private String commentRendering;

    @Inject
    public AddEntityCommentResult(@Nonnull ProjectId projectId,
                                  @Nonnull ThreadId threadId,
                                  @Nonnull Comment comment,
                                  @Nonnull String commentRendering) {
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.comment = checkNotNull(comment);
        this.commentRendering = checkNotNull(commentRendering);
    }

    @GwtSerializationConstructor
    private AddEntityCommentResult() {
    }

    @Nonnull
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

    public String getCommentRendering() {
        return commentRendering;
    }
}
