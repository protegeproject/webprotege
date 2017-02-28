package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.access.ActionId;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/03/16
 */
public class PermissionScreener {

    private final Provider<ForbiddenView> forbiddenViewProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    public interface Callback {
        void onPermissionGranted();
    }

    @Inject
    public PermissionScreener(Provider<ForbiddenView> forbiddenViewProvider,
                              LoggedInUserProjectPermissionChecker permissionChecker) {
        this.forbiddenViewProvider = forbiddenViewProvider;
        this.permissionChecker = permissionChecker;
    }

    public void checkPermission(final ActionId expectedPermission,
                                final AcceptsOneWidget acceptsOneWidget,
                                final Callback callback) {

        permissionChecker.hasPermission(expectedPermission, hasPermission -> {
            if(hasPermission) {
                callback.onPermissionGranted();
            }
            else {
                acceptsOneWidget.setWidget(forbiddenViewProvider.get());
            }
        });
    }
}
