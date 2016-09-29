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
public class IssueTargetAdded extends AbstractIssueEvent {

    @Nonnull
    private OWLEntity targetEntity;

    public IssueTargetAdded(@Nonnull UserId userId, long timestamp, @Nonnull OWLEntity targetEntity) {
        super(userId, timestamp);
        this.targetEntity = checkNotNull(targetEntity);
    }

    @GwtSerializationConstructor
    private IssueTargetAdded() {
    }

    @Nonnull
    public OWLEntity getTargetEntity() {
        return targetEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), getTargetEntity());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueTargetAdded)) {
            return false;
        }
        IssueTargetAdded other = (IssueTargetAdded) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.getTargetEntity().equals(other.getTargetEntity());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueEntityTargetAdded" )
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("entity", getTargetEntity())
                .toString();
    }
}
