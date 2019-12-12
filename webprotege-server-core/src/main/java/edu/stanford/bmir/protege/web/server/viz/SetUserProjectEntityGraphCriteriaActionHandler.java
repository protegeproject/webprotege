package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.ProjectUserEntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphSettingsAction;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphCriteriaActionHandler extends AbstractProjectActionHandler<SetUserProjectEntityGraphSettingsAction, SetUserProjectEntityGraphResult> {

    @Nonnull
    private final EntityGraphSettingsRepository repository;

    @Inject
    public SetUserProjectEntityGraphCriteriaActionHandler(@Nonnull AccessManager accessManager,
                                                          @Nonnull EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }


    @Nonnull
    @Override
    public Class<SetUserProjectEntityGraphSettingsAction> getActionClass() {
        return SetUserProjectEntityGraphSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public SetUserProjectEntityGraphResult execute(@Nonnull SetUserProjectEntityGraphSettingsAction action,
                                                   @Nonnull ExecutionContext executionContext) {

        var settings = action.getSettings();
        var projectId = action.getProjectId();
        var userId = executionContext.getUserId();
        repository.saveSettings(ProjectUserEntityGraphSettings.get(projectId, userId, settings));
        return new SetUserProjectEntityGraphResult();
    }
}
