package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectSettingsActionHandler(ProjectManager projectManager,
                                           ProjectDetailsManager projectDetailsManager,
                                           AccessManager accessManager) {
        super(projectManager, accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_PROJECT_SETTINGS;
    }

    @Override
    protected GetProjectSettingsResult execute(GetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        return new GetProjectSettingsResult(projectDetailsManager.getProjectSettings(projectId));
    }

}
