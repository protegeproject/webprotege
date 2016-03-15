package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.dispatch.validators.CompositeRequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectExistsValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 * <p>
 *     A skeleton handler for handling actions that pertain to projects (i.e. {@link Action}s that implement
 *     {@link HasProjectId}.  Subclasses implement a specialised execute method which has a reference to the relevant
 *     {@link OWLAPIProject} as a parameter.  Further more, the validation includes a check to see if the project
 *     actually exists and fails if this isn't the case.
 * </p>
 */
public abstract class AbstractHasProjectActionHandler<A extends Action<R> & HasProjectId, R extends Result> implements ActionHandler<A, R> {

    private OWLAPIProjectManager projectManager;

    @Inject
    public AbstractHasProjectActionHandler(OWLAPIProjectManager projectManager) {
        this.projectManager = checkNotNull(projectManager);
    }

    @Override
    final public RequestValidator getRequestValidator(A action, RequestContext requestContext) {
        final RequestValidator additionalRequestValidator = getAdditionalRequestValidator(action, requestContext);
        final ProjectExistsValidator projectExistsValidator = new ProjectExistsValidator(action.getProjectId());
        List<RequestValidator> validators = new ArrayList<>();
        validators.add(additionalRequestValidator);
        validators.add(projectExistsValidator);
        return CompositeRequestValidator.get(validators);
    }

    /**
     * Gets an additional validator that is specific to the implementing handler.  This is returned as part of a
     * {@link CompositeRequestValidator} by the the implementation of
     * the {@link #getRequestValidator(edu.stanford.bmir.protege.web.shared.dispatch.Action,
     * edu.stanford.bmir.protege.web.server.dispatch.RequestContext)} method.
     * @param action The action that the validation will be completed against.
     * @param requestContext The {@link RequestContext} that describes the context for the request.
     * @return A {@link RequestValidator} for this handler.  Not {@code null}.
     */
    protected abstract RequestValidator getAdditionalRequestValidator(A action, RequestContext requestContext);

    @Override
    final public R execute(A action, ExecutionContext executionContext) {
        OWLAPIProject project = projectManager.getProject(action.getProjectId());
        return execute(action, project, executionContext);
    }


    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param project The project that the action should be executed with respect to.
     * @param executionContext The {@link ExecutionContext} that should be used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    protected abstract R execute(A action, OWLAPIProject project, ExecutionContext executionContext);
}
