package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class ProjectChange implements IsSerializable, Serializable {

    private RevisionNumber revisionNumber;

    private UserId author;

    private long timestamp;

    private String summary;

    private Page<DiffElement<String, SafeHtml>> diff;

    /**
     * For serialization purposes only.
     */
    private ProjectChange() {
    }

    public ProjectChange(RevisionNumber revisionNumber, UserId author, long timestamp, String summary, Page<DiffElement<String, SafeHtml>> diff) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.author = checkNotNull(author);
        this.timestamp = checkNotNull(timestamp);
        this.summary = checkNotNull(summary);
        this.diff = checkNotNull(diff);
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    public UserId getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Page<DiffElement<String, SafeHtml>> getDiff() {
        return diff;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(revisionNumber, author, timestamp, summary, diff);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectChange)) {
            return false;
        }
        ProjectChange other = (ProjectChange) obj;
        return this.revisionNumber.equals(other.revisionNumber)
                && this.author.equals(other.author)
                && this.summary.equals(other.summary)
                && this.timestamp == other.timestamp
                && this.diff.equals(other.diff);
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectChange")
                .addValue(revisionNumber)
                .add("author", author)
                .add("timestamp", timestamp)
                .add("summary", summary)
                .add("diff", diff)
                .toString();
    }
}
