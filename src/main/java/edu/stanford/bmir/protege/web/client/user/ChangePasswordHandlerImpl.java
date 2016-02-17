package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ChangePasswordHandlerImpl implements ChangePasswordHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ChangePasswordHandlerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void handleChangePassword() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                ChangePasswordPresenter changePasswordPresenter = new ChangePasswordPresenter(loggedInUserProvider.getCurrentUserId(), dispatchServiceManager);
                changePasswordPresenter.changePassword();
            }
        });
    }
}
