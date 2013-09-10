package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.menu.*;
import com.gwtext.client.widgets.menu.MenuItem;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.about.AboutBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.ui.res.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class ApplicationBarImpl extends Composite implements ApplicationActionBar {

    private SignInRequestHandler signInRequestHandler = new SignInRequestHandler() {
        @Override
        public void handleSignInRequest() {
        }
    };

    private SignOutRequestHandler signOutRequestHandler = new SignOutRequestHandler() {
        @Override
        public void handleSignOutRequest() {
        }
    };

    private ShowAccountSettingsHandler showAccountSettingsHandler = new ShowAccountSettingsHandler() {
        @Override
        public void handleShowAccountSettings() {
        }
    };

    private ShowHelpInformationHandler showHelpInformationHandler = new ShowHelpInformationHandler() {
        @Override
        public void handleShowHelpInformation() {
        }
    };

    private SignUpForAccountHandler signUpForAccountHandler = new SignUpForAccountHandler() {
        @Override
        public void handleSignUpForAccount() {
        }
    };

    private ChangePasswordHandler changePasswordHandler = new ChangePasswordHandler() {
        @Override
        public void handleChangePassword() {
        }
    };

    public void setSignUpForAccountHandler(SignUpForAccountHandler signUpForAccountHandler) {
        this.signUpForAccountHandler = signUpForAccountHandler;
    }

    interface ApplicationBarImplUiBinder extends UiBinder<HTMLPanel, ApplicationBarImpl> {

    }

    private static ApplicationBarImplUiBinder ourUiBinder = GWT.create(ApplicationBarImplUiBinder.class);

    @UiField
    protected ButtonBase userNameItem;

    @UiField
    protected ButtonBase signUpForAccountItem;

    @UiField
    protected ButtonBase helpItem;

    public ApplicationBarImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        helpItem.setHTML("Help&nbsp;&nbsp;&#x25BE");
    }

    @UiHandler("signUpForAccountItem")
    protected void handleSignUpForAccountItemClicked(ClickEvent clickEvent) {
        signUpForAccountHandler.handleSignUpForAccount();
    }

    @UiHandler("userNameItem")
    protected void handleSignInItemClicked(ClickEvent clickEvent) {
        if (!Application.get().isGuestUser()) {
            showSignedInPopup();
        } else {
            signInRequestHandler.handleSignInRequest();
        }
    }

    @UiHandler("helpItem")
    protected void handleHelpItemClicked(ClickEvent clickEvent) {
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem("User guide", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open("http://protegewiki.stanford.edu/wiki/WebProtegeUsersGuide", "_blank", "");
            }
        });
        popupMenu.addItem("Send feeback", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MessageBox.showMessage("Send us feedback", WebProtegeClientBundle.BUNDLE.feedbackBoxText().getText());
            }
        });
        popupMenu.addItem("About", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AboutBox().show();
            }
        });
        popupMenu.showRelativeTo(helpItem);
    }

    private void showSignedInPopup() {
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem("Sign out", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                signOutRequestHandler.handleSignOutRequest();
            }
        });
//        popupMenu.addItem("Account settings", new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                showAccountSettingsHandler.handleShowAccountSettings();
//            }
//        });
        popupMenu.addItem("Change password", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                changePasswordHandler.handleChangePassword();
            }
        });
        popupMenu.showRelativeTo(userNameItem);
    }

    @Override
    public void setSignedInUser(UserId userId) {
        if (userId.isGuest()) {
            userNameItem.setText("Sign in");
            signUpForAccountItem.setVisible(true);
        } else {
            SafeHtmlBuilder builder = getSignedInUserNameAsSafeHtml(userId);
            userNameItem.setHTML(builder.toSafeHtml());
            signUpForAccountItem.setVisible(false);
        }
    }

    private static SafeHtmlBuilder getSignedInUserNameAsSafeHtml(UserId userId) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendEscaped(userId.getUserName());
        // Append a small down arrow
        builder.appendHtmlConstant("&nbsp;&nbsp;&#x25BE");
        return builder;
    }

    @Override
    public void setSignInRequestHandler(SignInRequestHandler signInRequestHandler) {
        this.signInRequestHandler = checkNotNull(signInRequestHandler);
    }

    @Override
    public void setSignOutRequestHandler(SignOutRequestHandler signOutRequestHandler) {
        this.signOutRequestHandler = signOutRequestHandler;
    }

    @Override
    public void setChangePasswordHandler(ChangePasswordHandler changePasswordHandler) {
        this.changePasswordHandler = changePasswordHandler;
    }
}