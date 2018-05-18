package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesResult implements Result {

    private Page<ProjectChange> changes;

    /**
     * For serialization purposes only
     */
    private GetProjectChangesResult() {
    }

    public GetProjectChangesResult(Page<ProjectChange> changes) {
        this.changes = checkNotNull(changes);
    }

    public Page<ProjectChange> getChanges() {
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
        if (!(obj instanceof GetProjectChangesResult)) {
            return false;
        }
        GetProjectChangesResult other = (GetProjectChangesResult) obj;
        return this.changes.equals(other.changes);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectChangesResult")
                .addValue(changes)
                .toString();
    }
}
