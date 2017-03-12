package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadResult implements Result {

    private ImmutableList<EntityDiscussionThread> threads;

    public CreateEntityDiscussionThreadResult(@Nonnull ImmutableList<EntityDiscussionThread> threads) {
        this.threads = checkNotNull(threads);
    }

    @GwtSerializationConstructor
    private CreateEntityDiscussionThreadResult() {
    }

    public ImmutableList<EntityDiscussionThread> getThreads() {
        return threads;
    }
}
