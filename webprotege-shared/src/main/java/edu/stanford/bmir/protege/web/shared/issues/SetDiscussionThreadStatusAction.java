package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusAction implements ProjectAction<SetDiscussionThreadStatusResult> {

    private ProjectId projectId;

    private ThreadId threadId;

    private Status status;

    public SetDiscussionThreadStatusAction(@Nonnull ProjectId projectId,
                                           @Nonnull ThreadId threadId,
                                           @Nonnull Status status) {
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.status = checkNotNull(status);
    }

    @GwtSerializationConstructor
    private SetDiscussionThreadStatusAction() {
    }

    public static SetDiscussionThreadStatusAction setDiscussionThreadStatus(@Nonnull ProjectId projectId,
                                                                            @Nonnull ThreadId threadId,
                                                                            @Nonnull Status status) {
        return new SetDiscussionThreadStatusAction(projectId, threadId, status);
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }
}
