package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class GetEntityDiscussionThreadsResult implements Result {

    @SuppressWarnings("GwtInconsistentSerializableClass" )
    private OWLEntityData entityData;

    private ImmutableList<EntityDiscussionThread> threads;

    @Inject
    public GetEntityDiscussionThreadsResult(@Nonnull OWLEntityData entityData,
                                            @Nonnull ImmutableList<EntityDiscussionThread> threads) {
        this.entityData = checkNotNull(entityData);
        this.threads = checkNotNull(threads);
    }

    @GwtSerializationConstructor
    private GetEntityDiscussionThreadsResult() {
    }

    @Nonnull
    public OWLEntityData getEntityData() {
        return entityData;
    }

    @Nonnull
    public ImmutableList<EntityDiscussionThread> getThreads() {
        return threads;
    }
}
