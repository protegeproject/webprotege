package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_NEW_ENTITY_SETTINGS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<GetEntityCrudKitSettingsAction, GetEntityCrudKitSettingsResult> {

    @Inject
    public GetEntityCrudKitSettingsActionHandler(ProjectManager projectManager,
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
        return EDIT_NEW_ENTITY_SETTINGS;
    }

    @Override
    protected GetEntityCrudKitSettingsResult execute(GetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetEntityCrudKitSettingsResult(project.getEntityCrudKitHandler().getSettings());
    }


}
