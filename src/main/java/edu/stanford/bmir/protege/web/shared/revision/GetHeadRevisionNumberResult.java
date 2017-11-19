package edu.stanford.bmir.protege.web.shared.revision;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberResult implements Result {

    private RevisionNumber revisionNumber;

    /**
     * For serialization purposes only
     */
    private GetHeadRevisionNumberResult() {
    }

    public GetHeadRevisionNumberResult(RevisionNumber revisionNumber) {
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(revisionNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetHeadRevisionNumberResult)) {
            return false;
        }
        GetHeadRevisionNumberResult other = (GetHeadRevisionNumberResult) obj;
        return this.revisionNumber.equals(other.revisionNumber);
    }


    @Override
    public String toString() {
        return toStringHelper("GetHeadRevisionNumberResult")
                .addValue(revisionNumber)
                .toString();
    }
}
