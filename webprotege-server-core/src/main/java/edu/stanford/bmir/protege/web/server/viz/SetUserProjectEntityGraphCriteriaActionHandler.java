package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.viz.ProjectUserEntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphResult;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphSettingsAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphCriteriaActionHandler extends AbstractProjectActionHandler<SetUserProjectEntityGraphSettingsAction, SetUserProjectEntityGraphResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final EntityGraphSettingsRepository repository;

    @Inject
    public SetUserProjectEntityGraphCriteriaActionHandler(@Nonnull AccessManager accessManager,
                                                          @Nonnull EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.accessManager = accessManager;
        this.repository = checkNotNull(repository);
    }

    @Nonnull
    @Override
    public SetUserProjectEntityGraphResult execute(@Nonnull SetUserProjectEntityGraphSettingsAction action,
                                                   @Nonnull ExecutionContext executionContext) {

        if(action.getUserId()
                 .equals(Optional.of(UserId.getGuest()))) {
            return new SetUserProjectEntityGraphResult();
        }
        var settings = action.getSettings();
        var projectId = action.getProjectId();
        var userSettings = action.getUserId()
                                 .map(userId -> ProjectUserEntityGraphSettings.get(projectId, userId, settings))
                                 .orElse(
                                         ProjectUserEntityGraphSettings.get(projectId, null, settings)
                                 );
        repository.saveSettings(userSettings);
        return new SetUserProjectEntityGraphResult();
    }

    @Nonnull
    @Override
    public Class<SetUserProjectEntityGraphSettingsAction> getActionClass() {
        return SetUserProjectEntityGraphSettingsAction.class;
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(SetUserProjectEntityGraphSettingsAction action,
                                                             RequestContext requestContext) {
        return () -> {
            UserId userInSession = requestContext.getUserId();
            if(action.getUserId().equals(Optional.of(userInSession))) {
                // Users can set their own settings
                return RequestValidationResult.getValid();
            }
            else {
                if(accessManager.hasPermission(Subject.forUser(userInSession),
                                               ProjectResource.forProject(action.getProjectId()),
                                               BuiltInAction.EDIT_DEFAULT_VISUALIZATION_SETTINGS)) {
                    return RequestValidationResult.getValid();
                }
                else {
                    return RequestValidationResult.getInvalid("Permission denied");
                }
            }
        };

    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetUserProjectEntityGraphSettingsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }
}
