package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class LoggedInUserViewImpl extends Composite implements LoggedInUserView {

    interface LoggedInUserViewImplUiBinder extends UiBinder<HTMLPanel, LoggedInUserViewImpl> {

    }

    private static LoggedInUserViewImplUiBinder ourUiBinder = GWT.create(LoggedInUserViewImplUiBinder.class);


    private SignOutRequestHandler signOutRequestHandler = new SignOutRequestHandler() {
        @Override
        public void handleSignOutRequest() {

        }
    };

    private ChangeEmailAddressHandler changeEmailAddressHandler = new ChangeEmailAddressHandler() {
        @Override
        public void handleChangeEmailAddress() {

        }
    };

    private ChangePasswordHandler changePasswordHandler = new ChangePasswordHandler() {
        @Override
        public void handleChangePassword() {

        }
    };




    @UiField
    protected Button loggedInUserButton;

    private final PopupMenu popupMenu = new PopupMenu();

    @Inject
    public LoggedInUserViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        popupMenu.addItem("Sign Out", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                signOutRequestHandler.handleSignOutRequest();
            }
        });
        popupMenu.addItem("Change email address", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                changeEmailAddressHandler.handleChangeEmailAddress();
            }
        });
        popupMenu.addItem("Change password", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                changePasswordHandler.handleChangePassword();
            }
        });
    }

    @UiHandler("loggedInUserButton")
    protected void handleLoggedInUserButtonClicked(ClickEvent clickEvent) {
        popupMenu.showRelativeTo(loggedInUserButton);
    }



    @Override
    public void setLoggedInUserName(String displayName) {
        loggedInUserButton.setText(displayName + " \u25be");
    }

    @Override
    public void clearLoggedInUserName() {
        loggedInUserButton.setText("");
    }

    @Override
    public void setSignOutRequestHandler(SignOutRequestHandler handler) {
        this.signOutRequestHandler = checkNotNull(handler);
    }

    @Override
    public void setChangeEmailAddressHandler(ChangeEmailAddressHandler handler) {
        changeEmailAddressHandler = checkNotNull(handler);
    }

    @Override
    public void setChangePasswordHandler(ChangePasswordHandler handler) {
        changePasswordHandler = checkNotNull(handler);
    }
}