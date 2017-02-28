package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public interface LoggedInUserProjectPermissionChecker {

    void hasPermission(ActionId actionId, DispatchServiceCallback<Boolean> callback);

    void hasPermission(ActionId action, Consumer<Boolean> callback);

    void hasPermission(BuiltInAction action, Consumer<Boolean> callback);
}
