package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddEntityCommentAction implements Action<AddEntityCommentResult> {

    private ProjectId projectId;

    private ThreadId threadId;

    private String comment;

    @Inject
    public AddEntityCommentAction(@Nonnull ProjectId projectId,
                                  @Nonnull ThreadId threadId,
                                  @Nonnull String comment) {
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.comment = checkNotNull(comment);
    }

    @GwtSerializationConstructor
    private AddEntityCommentAction() {
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    public String getComment() {
        return comment;
    }
}
