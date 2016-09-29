package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public abstract  class AbstractIssueEvent implements IssueEvent {

    @Nonnull
    private UserId userId;

    private long timestamp;

    @GwtSerializationConstructor
    protected AbstractIssueEvent() {
    }

    public AbstractIssueEvent(@Nonnull UserId userId, long timestamp) {
        this.userId = checkNotNull(userId);
        this.timestamp = timestamp;
    }

    @Nonnull
    @Override
    public UserId getUserId() {
        return userId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
