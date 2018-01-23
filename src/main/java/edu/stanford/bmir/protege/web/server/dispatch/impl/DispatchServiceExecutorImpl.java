package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public class DispatchServiceExecutorImpl implements DispatchServiceExecutor {

    private static final Logger logger = Logger.getLogger(DispatchServiceExecutorImpl.class.getName());

    @Nonnull
    private final ApplicationActionHandlerRegistry handlerRegistry;

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;
    
    @Inject
    public DispatchServiceExecutorImpl(@Nonnull ApplicationActionHandlerRegistry handlerRegistry,
                                       @Nonnull ProjectManager projectManager,
                                       @Nonnull UserInSessionFactory userInSessionFactory) {
        this.handlerRegistry = checkNotNull(handlerRegistry);
        this.projectManager = checkNotNull(projectManager);
        this.userInSessionFactory = userInSessionFactory;
    }

    @Override
    public <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, RequestContext requestContext, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        ActionHandler<A, R> actionHandler = null;
        if(action instanceof ProjectAction) {
            ProjectAction projectAction = (ProjectAction) action;
            ProjectId projectId = projectAction.getProjectId();
            ProjectActionHandlerRegistry actionHanderRegistry = projectManager.getActionHandlerRegistry(projectId);
            actionHandler = actionHanderRegistry.getActionHandler(action);
        }
        else {
            actionHandler = handlerRegistry.getActionHandler(action);
        }

        RequestValidator validator = actionHandler.getRequestValidator(action, requestContext);
        RequestValidationResult validationResult = validator.validateAction();
        if(!validationResult.isValid()) {
            throw  getPermissionDeniedException(requestContext.getUserId(),
                                                validationResult);
        }

        try {
            R result = actionHandler.execute(action, executionContext);
            return new DispatchServiceResultContainer(result);
        } catch (PermissionDeniedException e) {
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred whilst executing an action", e);
            throw new ActionExecutionException(e);
        }
    }

    private PermissionDeniedException getPermissionDeniedException(@Nonnull UserId userId,
                                                                   @Nonnull RequestValidationResult validationResult) {
        if(validationResult.getInvalidException().isPresent()) {
            Exception validationException = validationResult.getInvalidException().get();
            if(validationException instanceof PermissionDeniedException) {
                return  ((PermissionDeniedException) validationException);
            }
        }
        throw new PermissionDeniedException(validationResult.getInvalidMessage(),
                                            userInSessionFactory.getUserInSession(userId));
    }
}
