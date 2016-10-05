package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Sep 2016
 */
public class IssueTargetRemoved extends AbstractIssueEvent {

    private OWLEntity entity;

    public IssueTargetRemoved(@Nonnull UserId userId, long timestamp, @Nonnull OWLEntity entity) {
        super(userId, timestamp);
        this.entity = checkNotNull(entity);
    }

    @GwtSerializationConstructor
    public IssueTargetRemoved() {
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), getEntity());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueTargetRemoved)) {
            return false;
        }
        IssueTargetRemoved other = (IssueTargetRemoved) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getEntity().equals(other.getEntity())
                && this.getUserId().equals(other.getUserId());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueEntityTargetRemoved" )
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("entity", getEntity())
                .toString();
    }
}
