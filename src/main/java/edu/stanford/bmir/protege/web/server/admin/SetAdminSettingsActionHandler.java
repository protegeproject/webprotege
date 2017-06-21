package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.admin.SetAdminSettingsAction;
import edu.stanford.bmir.protege.web.shared.admin.SetAdminSettingsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetAdminSettingsActionHandler implements ApplicationActionHandler<SetAdminSettingsAction, SetAdminSettingsResult> {

    private final AdminSettingsManager adminSettingsManager;

    private final AccessManager accessManager;

    @Inject
    public SetAdminSettingsActionHandler(@Nonnull AdminSettingsManager adminSettingsManager,
                                         @Nonnull AccessManager accessManager) {
        this.adminSettingsManager = adminSettingsManager;
        this.accessManager = accessManager;
    }

    @Override
    public Class<SetAdminSettingsAction> getActionClass() {
        return SetAdminSettingsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(SetAdminSettingsAction action, RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Override
    public SetAdminSettingsResult execute(SetAdminSettingsAction action, ExecutionContext executionContext) {
        adminSettingsManager.setAdminSettings(action.getAdminSettings());
        return new SetAdminSettingsResult();
    }
}
