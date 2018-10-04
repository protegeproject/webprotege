package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenter;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenterFactory;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ChangePasswordHandlerImpl implements ChangePasswordHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangePasswordPresenterFactory presenterFactory;

    @Inject
    public ChangePasswordHandlerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, ChangePasswordPresenterFactory presenterFactory) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.presenterFactory = presenterFactory;
    }

    @Override
    public void handleChangePassword() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                ChangePasswordPresenter changePasswordPresenter = presenterFactory.create(loggedInUserProvider.getCurrentUserId());
                changePasswordPresenter.changePassword();
            }
        });
    }
}
