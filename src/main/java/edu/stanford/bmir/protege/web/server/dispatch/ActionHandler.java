package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     Handles {@link Action}s from a client request.  An {@link ActionHandler} supplies a {@link RequestValidator}
 *     which can be used to determine whether an action can be executed within a given request context.
 * </p>
 * <p>
 *     Action handlers are <b>stateless</b>.  They are thread safe.
 * </p>
 */
@ThreadSafe
public interface ActionHandler<A extends Action<R>, R extends Result> {

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    Class<A> getActionClass();

    RequestValidator getRequestValidator(A action, RequestContext requestContext);

    R execute(A action, ExecutionContext executionContext);

}
