package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.admin.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.admin.GetApplicationSettingsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class GetApplicationSettingsActionHandler implements ApplicationActionHandler<GetApplicationSettingsAction, GetApplicationSettingsResult> {

    private final AccessManager manager;

    private final ApplicationSettingsManager applicationSettingsManager;

    @Inject
    public GetApplicationSettingsActionHandler(@Nonnull AccessManager manager,
                                               @Nonnull ApplicationSettingsManager applicationSettingsManager) {
        this.manager = checkNotNull(manager);
        this.applicationSettingsManager = checkNotNull(applicationSettingsManager);
    }

    @Override
    public Class<GetApplicationSettingsAction> getActionClass() {
        return GetApplicationSettingsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetApplicationSettingsAction action, RequestContext requestContext) {
        return new ApplicationPermissionValidator(manager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Override
    public GetApplicationSettingsResult execute(GetApplicationSettingsAction action, ExecutionContext executionContext) {
        return new GetApplicationSettingsResult(applicationSettingsManager.getAdminSettings());
    }
}
