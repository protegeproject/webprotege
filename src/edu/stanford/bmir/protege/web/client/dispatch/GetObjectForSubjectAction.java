package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public abstract class GetObjectForSubjectAction<S, O> implements Action<GetObjectResult<O>>, HasSubject<S> {

    private S subject;

    protected GetObjectForSubjectAction(S subject) {
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
