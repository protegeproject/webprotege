package edu.stanford.bmir.protege.web.server.dispatch;


import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public interface RequestValidator<A extends Action<? extends Result>> {

    RequestValidationResult validateAction(A action, RequestContext requestContext);
}
