package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdActionHandler extends AbstractHasProjectActionHandler<GetRootOntologyIdAction, GetRootOntologyIdResult> {

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetRootOntologyIdAction> getActionClass() {
        return GetRootOntologyIdAction.class;
    }

    /**
     * Returns an additional validator which ensures the requesting user had read permission for the project.
     */
    @Override
    protected RequestValidator getAdditionalRequestValidator(GetRootOntologyIdAction action, RequestContext requestContext) {
        return new UserHasProjectReadPermissionValidator();
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param project The project that the action should be executed with respect to.
     * @param executionContext The {@link edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext} that should be
     * used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.client.rpc.data.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Override
    protected GetRootOntologyIdResult execute(GetRootOntologyIdAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final OWLOntology rootOntology = project.getRootOntology();
        final OWLOntologyID result = rootOntology.getOntologyID();
        return new GetRootOntologyIdResult(project.getProjectId(), result);
    }
}
