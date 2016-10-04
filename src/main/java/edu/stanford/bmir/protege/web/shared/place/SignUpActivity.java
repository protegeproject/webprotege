package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.signup.SignUpPresenter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class SignUpActivity extends AbstractActivity {

    private SignUpPresenter signUpPresenter;

    public SignUpActivity(SignUpPresenter signUpPresenter) {
        this.signUpPresenter = signUpPresenter;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        signUpPresenter.start(panel);
    }
}
