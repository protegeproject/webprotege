package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.login.LoginPresenter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginActivity extends AbstractActivity {

    private LoginPresenter presenter;

    @Inject
    public LoginActivity(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(presenter.getView());
    }
}
