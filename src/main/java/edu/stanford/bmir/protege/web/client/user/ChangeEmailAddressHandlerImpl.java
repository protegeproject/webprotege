package edu.stanford.bmir.protege.web.client.user;

import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.mail.ChangeEmailAddressPresenter;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class ChangeEmailAddressHandlerImpl implements ChangeEmailAddressHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ChangeEmailAddressHandlerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void handleChangeEmailAddress() {
        ChangeEmailAddressPresenter presenter = new ChangeEmailAddressPresenter(dispatchServiceManager, loggedInUserProvider);
        presenter.changeEmail();
    }
}
