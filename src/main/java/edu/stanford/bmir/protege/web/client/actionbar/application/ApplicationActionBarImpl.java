package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class ApplicationActionBarImpl extends Composite implements ApplicationActionBar {

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

//    private ShowAccountSettingsHandler showAccountSettingsHandler = new ShowAccountSettingsHandler() {
//        @Override
//        public void handleShowAccountSettings() {
//        }
//    };


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

    private ChangeEmailAddressHandler changeEmailAddressHandler = new ChangeEmailAddressHandler() {
        @Override
        public void handleChangeEmailAddress() {
        }
    };

    private ShowAboutBoxHandler showAboutBoxHandler = new ShowAboutBoxHandler() {
        @Override
        public void handleShowAboutBox() {
        }
    };

    private ShowUserGuideHandler showUserGuideHandler = new ShowUserGuideHandler() {
        @Override
        public void handleShowUserGuide() {
        }
    };

    public void setSignUpForAccountHandler(SignUpForAccountHandler signUpForAccountHandler) {
        this.signUpForAccountHandler = signUpForAccountHandler;
    }

    @Override
    public void setSignUpForAccountVisible(boolean visible) {
        signUpForAccountItem.setVisible(visible);
    }

    interface ApplicationBarImplUiBinder extends UiBinder<HTMLPanel, ApplicationActionBarImpl> {

    }

    private static ApplicationBarImplUiBinder ourUiBinder = GWT.create(ApplicationBarImplUiBinder.class);

    @UiField
    protected ButtonBase userNameItem;

    @UiField
    protected ButtonBase signUpForAccountItem;

    @UiField
    protected ButtonBase helpItem;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ApplicationActionBarImpl(LoggedInUserProvider loggedInUserProvider) {
        this.loggedInUserProvider = loggedInUserProvider;
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
        if (!loggedInUserProvider.getCurrentUserId().isGuest()) {
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
                showUserGuideHandler.handleShowUserGuide();
            }
        });
        popupMenu.addItem("Send feedback", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MessageBox.showMessage("Send us feedback", WebProtegeClientBundle.BUNDLE.feedbackBoxText().getText());
            }
        });
        popupMenu.addItem("About", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAboutBoxHandler.handleShowAboutBox();
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
        popupMenu.addItem("Change email address", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                changeEmailAddressHandler.handleChangeEmailAddress();
            }
        });
        popupMenu.showRelativeTo(userNameItem);
    }


    @Override
    public void setSignedInUser(UserId userId) {
        if (userId.isGuest()) {
            userNameItem.setText("Sign in");
        } else {
            SafeHtmlBuilder builder = getSignedInUserNameAsSafeHtml(userId);
            userNameItem.setHTML(builder.toSafeHtml());
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
        this.signOutRequestHandler = checkNotNull(signOutRequestHandler);
    }

    @Override
    public void setChangePasswordHandler(ChangePasswordHandler changePasswordHandler) {
        this.changePasswordHandler = checkNotNull(changePasswordHandler);
    }

    @Override
    public void setChangeEmailAddressHandler(ChangeEmailAddressHandler changeEmailAddressHandler) {
        this.changeEmailAddressHandler = checkNotNull(changeEmailAddressHandler);
    }

    @Override
    public void setShowAboutBoxHandler(ShowAboutBoxHandler showAboutBoxHandler) {
        this.showAboutBoxHandler = checkNotNull(showAboutBoxHandler);
    }

    @Override
    public void setShowUserGuideHandler(ShowUserGuideHandler showUserGuideHandler) {
        this.showUserGuideHandler = checkNotNull(showUserGuideHandler);
    }
}