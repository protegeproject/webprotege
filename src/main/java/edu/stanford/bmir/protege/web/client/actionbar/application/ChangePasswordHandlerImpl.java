package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ChangePasswordHandlerImpl implements ChangePasswordHandler {

    @Override
    public void handleChangePassword() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                ChangePasswordPresenter changePasswordPresenter = new ChangePasswordPresenter(Application.get().getUserId());
                changePasswordPresenter.changePassword();
            }
        });
    }
}
