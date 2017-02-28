package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MANAGE_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<GetEntityCrudKitSettingsAction, GetEntityCrudKitSettingsResult> {

    @Inject
    public GetEntityCrudKitSettingsActionHandler(OWLAPIProjectManager projectManager,
                                                 AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetEntityCrudKitSettingsAction> getActionClass() {
        return GetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return MANAGE_PROJECT;
    }

    @Override
    protected GetEntityCrudKitSettingsResult execute(GetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetEntityCrudKitSettingsResult(project.getEntityCrudKitHandler().getSettings());
    }


}
