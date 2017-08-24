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
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;

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


    private SignOutRequestHandler signOutRequestHandler = () -> {};

    private ChangeEmailAddressHandler changeEmailAddressHandler = () -> {};

    private ChangePasswordHandler changePasswordHandler = () -> {};




    @UiField
    protected Button loggedInUserButton;

    private final PopupMenu popupMenu = new PopupMenu();

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @Inject
    public LoggedInUserViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        popupMenu.addItem(MESSAGES.signOut(), event -> signOutRequestHandler.handleSignOutRequest());
        popupMenu.addItem(MESSAGES.changeEmailAddress(), event -> changeEmailAddressHandler.handleChangeEmailAddress());
        popupMenu.addItem(MESSAGES.changePassword(), event -> changePasswordHandler.handleChangePassword());
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