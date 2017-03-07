package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentAction implements Action<EditCommentResult>, HasProjectId {

    private ProjectId projectId;

    private ThreadId threadId;

    private CommentId commentId;

    private String body;

    public static EditCommentAction editComment(@Nonnull ProjectId projectId,
                                                @Nonnull ThreadId threadId,
                                                @Nonnull CommentId commentId,
                                                @Nonnull String body) {
        return new EditCommentAction(projectId, threadId, commentId, body);
    }

    public EditCommentAction(@Nonnull ProjectId projectId,
                             @Nonnull ThreadId threadId,
                             @Nonnull CommentId commentId,
                             @Nonnull String body) {
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.commentId = checkNotNull(commentId);
        this.body = checkNotNull(body);
    }

    @GwtSerializationConstructor
    private EditCommentAction() {
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public String getBody() {
        return body;
    }
}
