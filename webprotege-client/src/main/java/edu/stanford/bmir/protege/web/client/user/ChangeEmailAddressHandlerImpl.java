package edu.stanford.bmir.protege.web.client.user;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.mail.ChangeEmailAddressPresenter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class ChangeEmailAddressHandlerImpl implements ChangeEmailAddressHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Provider<ChangeEmailAddressPresenter> changeEmailAddressPresenterProvider;

    @Inject
    public ChangeEmailAddressHandlerImpl(DispatchServiceManager dispatchServiceManager,
                                         LoggedInUserProvider loggedInUserProvider,
                                         Provider<ChangeEmailAddressPresenter> changeEmailAddressPresenterProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.changeEmailAddressPresenterProvider = changeEmailAddressPresenterProvider;
    }

    @Override
    public void handleChangeEmailAddress() {
        ChangeEmailAddressPresenter presenter = changeEmailAddressPresenterProvider.get();
        presenter.changeEmail();
    }
}
