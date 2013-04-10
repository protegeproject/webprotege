package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractHasSubjectAction<S, R extends Result> extends AbstractHasProjectAction<R> implements HasSubject<S> {

    private S subject;

    /**
     * For serialization purposes only
     */
    protected AbstractHasSubjectAction() {
        super();
    }

    protected AbstractHasSubjectAction(ProjectId projectId, S subject) {
        super(projectId);
        this.subject = subject;
    }

    /**
     * Gets the subject of this object.
     * @return The subject.  Not {@code null}.
     */
    @Override
    public S getSubject() {
        return subject;
    }
}
