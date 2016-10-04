package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesResult implements Result {

    private ImmutableList<ProjectChange> changes;

    private GetWatchedEntityChangesResult() {
    }

    public GetWatchedEntityChangesResult(ImmutableList<ProjectChange> changes) {
        this.changes = checkNotNull(changes);
    }

    public ImmutableList<ProjectChange> getChanges() {
        return changes;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(changes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetWatchedEntityChangesResult)) {
            return false;
        }
        GetWatchedEntityChangesResult other = (GetWatchedEntityChangesResult) obj;
        return this.changes.equals(other.changes);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("GetWatchedEntityChangesResult")
                .addValue(changes)
                .toString();
    }
}
