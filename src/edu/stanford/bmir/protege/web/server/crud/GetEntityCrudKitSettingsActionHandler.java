package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectAdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<GetEntityCrudKitSettingsAction, GetEntityCrudKitSettingsResult> {

    @Override
    public Class<GetEntityCrudKitSettingsAction> getActionClass() {
        return GetEntityCrudKitSettingsAction.class;
    }

    @Override
    protected RequestValidator<GetEntityCrudKitSettingsAction> getAdditionalRequestValidator(GetEntityCrudKitSettingsAction action, RequestContext requestContext) {
        return UserHasProjectAdminPermissionValidator.get();
    }

    @Override
    protected GetEntityCrudKitSettingsResult execute(GetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetEntityCrudKitSettingsResult(project.getEntityCrudKitHandler().getSettings());
    }


}
