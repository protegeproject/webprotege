package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.Property;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Date;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 *
 * A {@link ProjectId} and timestamp pair.
 */
public class RecentProjectRecord implements Comparable<RecentProjectRecord> {

    public static final String PROJECT_ID = "projectId";

    public static final String TIMESTAMP = "timestamp";

    @Property(value = PROJECT_ID)
    private ProjectId projectId;

    @Property(value = TIMESTAMP)
    private Date timestamp;

    private static Comparator<RecentProjectRecord> comparator = Comparator.comparing(RecentProjectRecord::getTimestamp)
                                                               .reversed()
                                                               .thenComparing(r -> r.getProjectId().getId());

    // For Morphia
    private RecentProjectRecord() {
    }

    /**
     * Constructs a {@link RecentProjectRecord} with the specified id and timestamp.
     * @param projectId The project id.
     * @param timestamp The timestamp.
     */
    public RecentProjectRecord(@Nonnull ProjectId projectId, long timestamp) {
        this.projectId = checkNotNull(projectId);
        this.timestamp = new Date(timestamp);
    }

    /**
     * Gets the project id.
     * @return The project id.
     */
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the timestamp.
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp.getTime();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecentProjectRecord)) {
            return false;
        }
        RecentProjectRecord other = (RecentProjectRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.timestamp.equals(other.timestamp);
    }


    @Override
    public String toString() {
        return toStringHelper("RecentProjectRecord" )
                .addValue(projectId)
                .add("timestamp", timestamp.getTime())
                .toString();
    }

    /**
     * Compare this {@link RecentProjectRecord} with another one.  Comparison is done
     * by timestamp in reverse order and then by project id.
     * @param o The other object.
     * @return The comparison value.
     */
    @Override
    public int compareTo(@Nonnull RecentProjectRecord o) {
        return comparator.compare(this, o);
    }
}
