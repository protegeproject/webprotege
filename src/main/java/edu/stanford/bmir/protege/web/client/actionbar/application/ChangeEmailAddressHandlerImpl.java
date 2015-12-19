package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.mail.ChangeEmailAddressPresenter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class ChangeEmailAddressHandlerImpl implements ChangeEmailAddressHandler {

    private final DispatchServiceManager dispatchServiceManager;

    public ChangeEmailAddressHandlerImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleChangeEmailAddress() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                ChangeEmailAddressPresenter presenter = new ChangeEmailAddressPresenter(dispatchServiceManager);
                presenter.changeEmail();
            }
        });
    }
}
