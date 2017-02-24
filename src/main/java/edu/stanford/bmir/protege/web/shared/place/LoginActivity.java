package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.login.LoginPresenter;

import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginActivity extends AbstractActivity {

    private LoginPresenter presenter;

    @Inject
    public LoginActivity(LoginPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start();
        panel.setWidget(presenter.getView());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(presenter);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LoginActivity)) {
            return false;
        }
        LoginActivity other = (LoginActivity) obj;
        return this.presenter.equals(other.presenter);
    }


    @Override
    public String toString() {
        return toStringHelper("LoginActivity")
                .addValue(presenter)
                .toString();
    }
}
