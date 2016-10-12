package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusResult implements Result {

    private ThreadId threadId;

    private Status result;

    public SetDiscussionThreadStatusResult(@Nonnull ThreadId threadId,
                                           @Nonnull Status result) {
        this.threadId = checkNotNull(threadId);
        this.result = checkNotNull(result);
    }

    @GwtSerializationConstructor
    private SetDiscussionThreadStatusResult() {
    }

    @Nonnull
    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Status getResult() {
        return result;
    }
}
