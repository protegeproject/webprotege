package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.project.UIConfigurationManager;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class SetUIConfigurationActionHandler implements ActionHandler<SetUIConfigurationAction, SetUIConfigurationResult> {

    private UIConfigurationManager uiConfigurationManager;

    @Inject
    public SetUIConfigurationActionHandler(UIConfigurationManager uiConfigurationManager) {
        this.uiConfigurationManager = uiConfigurationManager;
    }

    @Override
    public Class<SetUIConfigurationAction> getActionClass() {
        return SetUIConfigurationAction.class;
    }

    @Override
    public RequestValidator<SetUIConfigurationAction> getRequestValidator(SetUIConfigurationAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public SetUIConfigurationResult execute(SetUIConfigurationAction action, ExecutionContext executionContext) {
        uiConfigurationManager.saveProjectLayoutConfiguration(
                action.getProjectId(),
                executionContext.getUserId(),
                action.getConfiguration());
        return new SetUIConfigurationResult();
    }
}
