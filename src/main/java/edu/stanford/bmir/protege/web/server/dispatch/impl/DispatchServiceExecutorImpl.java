package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public class DispatchServiceExecutorImpl implements DispatchServiceExecutor {

    private static final Logger logger = Logger.getLogger(DispatchServiceExecutorImpl.class.getName());

    private final ActionHandlerRegistry handlerRegistry;
    
    @Inject
    public DispatchServiceExecutorImpl(ActionHandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    @Override
    public <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, RequestContext requestContext, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        ActionHandler<A, R> actionHandler = handlerRegistry.getActionHandler(action);
        RequestValidator validator = actionHandler.getRequestValidator(action, requestContext);
        RequestValidationResult validationResult = validator.validateAction();
        if(!validationResult.isValid()) {
            throw  getPermissionDeniedException(validationResult);
        }

        try {
            R result = actionHandler.execute(action, executionContext);
            return new DispatchServiceResultContainer(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred whilst executing an action", e);
            throw new ActionExecutionException(e);
        }
    }

    private static PermissionDeniedException getPermissionDeniedException(RequestValidationResult validationResult) {
        if(validationResult.getInvalidException().isPresent()) {
            Exception validationException = validationResult.getInvalidException().get();
            if(validationException instanceof PermissionDeniedException) {
                return  ((PermissionDeniedException) validationException);
            }
        }
        throw new PermissionDeniedException(validationResult.getInvalidMessage());
    }
}
