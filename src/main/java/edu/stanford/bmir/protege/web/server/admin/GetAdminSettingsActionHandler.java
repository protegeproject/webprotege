package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.admin.GetAdminSettingsAction;
import edu.stanford.bmir.protege.web.shared.admin.GetAdminSettingsResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class GetAdminSettingsActionHandler implements ApplicationActionHandler<GetAdminSettingsAction, GetAdminSettingsResult> {

    private final AccessManager manager;

    private final AdminSettingsManager adminSettingsManager;

    @Inject
    public GetAdminSettingsActionHandler(AccessManager manager,
                                         AdminSettingsManager adminSettingsManager) {
        this.manager = manager;
        this.adminSettingsManager = adminSettingsManager;
    }

    @Override
    public Class<GetAdminSettingsAction> getActionClass() {
        return GetAdminSettingsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetAdminSettingsAction action, RequestContext requestContext) {
        return new ApplicationPermissionValidator(manager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Override
    public GetAdminSettingsResult execute(GetAdminSettingsAction action, ExecutionContext executionContext) {
        return new GetAdminSettingsResult(adminSettingsManager.getAdminSettings());
    }
}
