package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.project.UIConfigurationManager;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class SetUIConfigurationActionHandler extends AbstractHasProjectActionHandler<SetUIConfigurationAction, SetUIConfigurationResult> {

    @Inject
    public SetUIConfigurationActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<SetUIConfigurationAction> getActionClass() {
        return SetUIConfigurationAction.class;
    }

    @Override
    protected RequestValidator<SetUIConfigurationAction> getAdditionalRequestValidator(SetUIConfigurationAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected SetUIConfigurationResult execute(SetUIConfigurationAction action, OWLAPIProject project, ExecutionContext executionContext) {
        if(executionContext.getUserId().isGuest()) {
            throw new NotSignedInException("The project layout cannot be saved because you are not signed in");
        }
        project.getUiConfigurationManager().saveProjectLayoutConfiguration(
                action.getProjectId(),
                executionContext.getUserId(),
                action.getConfiguration());
        return new SetUIConfigurationResult();
    }


}
