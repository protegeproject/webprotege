package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.REBUILD_PERMISSIONS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
public class RebuildPermissionsActionHandler implements ApplicationActionHandler<RebuildPermissionsAction, RebuildPermissionsResult> {

    private final AccessManager accessManager;

    @Inject
    public RebuildPermissionsActionHandler(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    @Nonnull
    @Override
    public Class<RebuildPermissionsAction> getActionClass() {
        return RebuildPermissionsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull RebuildPermissionsAction action, @Nonnull RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager, requestContext.getUserId(), REBUILD_PERMISSIONS.getActionId());
    }

    @Nonnull
    @Override
    public RebuildPermissionsResult execute(@Nonnull RebuildPermissionsAction action, @Nonnull ExecutionContext executionContext) {
        accessManager.rebuild();
        return new RebuildPermissionsResult();
    }
}
