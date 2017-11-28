package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.admin.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.admin.SetApplicationSettingsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetApplicationSettingsActionHandler implements ApplicationActionHandler<SetApplicationSettingsAction, SetApplicationSettingsResult> {

    private final ApplicationSettingsManager applicationSettingsManager;

    private final AccessManager accessManager;

    @Inject
    public SetApplicationSettingsActionHandler(@Nonnull ApplicationSettingsManager applicationSettingsManager,
                                               @Nonnull AccessManager accessManager) {
        this.applicationSettingsManager = checkNotNull(applicationSettingsManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Override
    public Class<SetApplicationSettingsAction> getActionClass() {
        return SetApplicationSettingsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(SetApplicationSettingsAction action, RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Override
    public SetApplicationSettingsResult execute(SetApplicationSettingsAction action, ExecutionContext executionContext) {
        applicationSettingsManager.setApplicationSettings(action.getApplicationSettings());
        return new SetApplicationSettingsResult();
    }
}
