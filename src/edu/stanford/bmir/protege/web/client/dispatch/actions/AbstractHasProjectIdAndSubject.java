package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 * <p>
 *     A skeletal implementation that binds together a project and some other object which is referred to as a subject.
 *     This class is serializable and has a no-arg constructor that subclasses can use to make themselves serializable.
 * </p>
 *
 */
public abstract class AbstractHasProjectIdAndSubject<S> implements HasProjectId, HasSubject<S>, Serializable {

    private ProjectId projectId;

    private S subject;

    /**
     * For serialization purposes only.  Subclasses should override both constructors in this class.
     */
    protected AbstractHasProjectIdAndSubject() {

    }

    public AbstractHasProjectIdAndSubject(S subject, ProjectId projectId) {
        this.projectId = projectId;
        this.subject = subject;
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Override
    final public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the subject of this object.
     * @return The subject.  Not {@code null}.
     */
    @Override
    final public S getSubject() {
        return subject;
    }

    /**
     * Computes the hashCode as the sum of {@link #getProjectId()#hashCode()} and {@link #getSubject()#hashCode()}.
     * @return The hashcode based on the result of {@link #getProjectId()} and {@link #getSubject()}.
     */
    @Override
    public int hashCode() {
        return "HasSubjectAndProjectBase".hashCode() + projectId.hashCode() + subject.hashCode();
    }

    /**
     * Determines if this object is equal to {@code obj} as specified below.
     * @param obj The specified object.
     * @return {@code true} if {@code this == obj}.  {@code false} if {@code obj} is not an instance of {@link AbstractHasProjectIdAndSubject}.
     * {@code true} if the {@link ProjectId} of this is equal to the {@link ProjectId} of {@code obj} and the subject of
     * this is equal to the subject of other.  Otherwise, {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AbstractHasProjectIdAndSubject)) {
            return false;
        }
        AbstractHasProjectIdAndSubject other = (AbstractHasProjectIdAndSubject) obj;
        return this.projectId.equals(other.projectId) && this.subject.equals(other.subject);
    }
}
