package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/03/16
 */
public class PermissionScreener {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Provider<ForbiddenView> forbiddenViewProvider;

    public static interface Callback {
        void onPermissionGranted();
    }

    @Inject
    public PermissionScreener(DispatchServiceManager dispatchServiceManager,
                              LoggedInUserProvider loggedInUserProvider,
                              Provider<ForbiddenView> forbiddenViewProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.forbiddenViewProvider = forbiddenViewProvider;
    }

    public void checkPermission(final ProjectId projectId,
                                final Permission expectedPermission,
                                final AcceptsOneWidget acceptsOneWidget,
                                final Callback callback) {
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, loggedInUserProvider.getCurrentUserId()), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult result) {
                if(result.getPermissionsSet().contains(expectedPermission)) {
                    callback.onPermissionGranted();
                }
                else {
                    acceptsOneWidget.setWidget(forbiddenViewProvider.get());
                }
            }
        });
    }
}
