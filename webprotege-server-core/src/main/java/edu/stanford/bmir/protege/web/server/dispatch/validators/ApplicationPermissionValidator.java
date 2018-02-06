package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class ApplicationPermissionValidator implements RequestValidator {

    private final AccessManager accessManager;

    private final UserId userId;

    private final ActionId actionId;

    public ApplicationPermissionValidator(@Nonnull AccessManager accessManager,
                                          @Nonnull UserId userId,
                                          @Nonnull ActionId actionId) {
        this.accessManager = checkNotNull(accessManager);
        this.userId = checkNotNull(userId);
        this.actionId = checkNotNull(actionId);
    }

    public ApplicationPermissionValidator(@Nonnull AccessManager accessManager,
                                          @Nonnull UserId userId,
                                          @Nonnull BuiltInAction actionId) {
        this(accessManager, userId, actionId.getActionId());
    }

    @Override
    public RequestValidationResult validateAction() {
        if(accessManager.hasPermission(forUser(userId),
                                           ApplicationResource.get(),
                                           actionId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("You do not have permission for " + actionId.getId());
        }
    }
}
