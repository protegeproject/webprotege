package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MANAGE_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public SetProjectSettingsActionHandler(OWLAPIProjectManager projectManager,
                                           ProjectDetailsManager projectDetailsManager,
                                           AccessManager accessManager) {
        super(projectManager, accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<SetProjectSettingsAction> getActionClass() {
        return SetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return MANAGE_PROJECT;
    }

    @Override
    protected SetProjectSettingsResult execute(SetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        projectDetailsManager.setProjectSettings(action.getProjectSettings());
        return new SetProjectSettingsResult(projectDetailsManager.getProjectSettings(action.getProjectId()));
    }
}
