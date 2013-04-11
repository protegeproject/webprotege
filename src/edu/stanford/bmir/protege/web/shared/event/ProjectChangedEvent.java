package edu.stanford.bmir.protege.web.shared.event;


import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectChangedEvent extends ProjectEvent<ProjectChangedHandler> {

    public transient static final Type<ProjectChangedHandler> TYPE = new Type<ProjectChangedHandler>();

    private UserId userId;

    private long timestamp;

    private Set<OWLEntityData> subjects;

    private RevisionNumber revisionNumber;

    /**
     * For serialization purposes only.
     */
    private ProjectChangedEvent() {
    }

    /**
     * Creates a {@link ProjectChangedEvent}.
     * @param source The source of the event.  The project that was changed.  Not {@code null}.
     * @param userId The id of the user that made the changes.  Not {@code null}.
     * @param timestamp The timestamp of when the changes were made. Not {@code null}.
     * @param subjects The possibly empty set of subjects of the changes.  Not {@code null}.
     * @param revisionNumber The revision number after the changes were applied.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public ProjectChangedEvent(ProjectId source, UserId userId, long timestamp, Set<OWLEntityData> subjects, RevisionNumber revisionNumber) {
        super(source);
        this.userId = checkNotNull(userId);
        this.timestamp = timestamp;
        this.subjects = new HashSet<OWLEntityData>(subjects);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    /**
     * Gets the {@link RevisionNumber} of the project after the changes were applied.
     * @return The {@link RevisionNumber}.  Not {@code null}.
     */
    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * Gets the {@link UserId} of the user that caused the changes to be applied.
     * @return The {@link UserId} of the user.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Gets the timestamp that represents the time the changes were applied.
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the subjects of the changes.
     * @return A possibly empty set of {@link OWLEntity} objects which represent the subject of the changes.  Not {@code null}.
     */
    public Set<OWLEntityData> getSubjects() {
        return new HashSet<OWLEntityData>(subjects);
    }




    @Override
    public Type<ProjectChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectChangedHandler handler) {
        handler.handleProjectChanged(this);
    }


    @Override
    public int hashCode() {
        return "ProjectChangedEvent".hashCode() + (int) timestamp + userId.hashCode() + subjects.hashCode() + revisionNumber.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectChangedEvent)) {
            return false;
        }
        ProjectChangedEvent other = (ProjectChangedEvent) obj;
        return this.timestamp == other.timestamp && this.userId.equals(other.userId) && this.subjects.equals(other.subjects) & this.revisionNumber.equals(other.revisionNumber);
    }
}
