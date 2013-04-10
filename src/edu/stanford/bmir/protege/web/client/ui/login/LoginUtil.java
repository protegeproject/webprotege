package edu.stanford.bmir.protege.web.client.ui.login;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AuthenticateServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OpenIdServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdIconPanel;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class LoginUtil {

    protected HandlerRegistration windowCloseHandlerRegistration; // handlerRegistration for removing window close handler

    public void login(final boolean isLoginWithHttps) {
        if (isLoginWithHttps) {
            OpenIdUtil.detectPopup("Your pop-up blocker has hidden the login window. Please disable the pop-up blocker and click on 'Sign In' again.");
        }
        final Window win = new Window();

        final FormPanel loginFormPanel = new FormPanel();
        loginFormPanel.setWidth("350px");

        Label label = new Label();
        label.setText("Please enter your username and password:");
        label.setStyleName("login-welcome-msg");

        final Label openIdlabel = new Label();

        openIdlabel.getElement().setInnerHTML("Login with your OpenId " + getHintHtml());
        openIdlabel.setStyleName("login-welcome-msg");

        final FlexTable loginTable = new FlexTable();
        loginTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        loginTable.getFlexCellFormatter().setHeight(0, 0, "15px");
        loginTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        loginTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        loginTable.getFlexCellFormatter().setHeight(3, 0, "25px");
        loginTable.getFlexCellFormatter().setHeight(4, 0, "70px");
        loginTable.getFlexCellFormatter().setHeight(5, 0, "40px");
        loginTable.getFlexCellFormatter().setHeight(6, 0, "25px");
        loginTable.setWidget(0, 0, label);

        loginFormPanel.add(loginTable);

        final TextBox userNameField = new TextBox();
        userNameField.setWidth("250px");
        Label userIdLabel = new Label("User name:");
        userIdLabel.setStyleName("label");
        loginTable.setWidget(2, 0, userIdLabel);
        loginTable.setWidget(2, 1, userNameField);

        final TextBox passwordField = new PasswordTextBox();
        passwordField.setWidth("250px");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyleName("label");
        loginTable.setWidget(3, 0, passwordLabel);
        loginTable.setWidget(3, 1, passwordField);

        userNameField.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSignIn(isLoginWithHttps, win, userNameField, passwordField);
                }
            }
        });

        passwordField.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSignIn(isLoginWithHttps, win, userNameField, passwordField);
                }
            }
        });

        Button signInButton = new Button("Sign In", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                performSignIn(isLoginWithHttps, win, userNameField, passwordField);
            }
        });

        ClickHandler forgotPassClickListener = forgotPasswordClickListener(win, userNameField, isLoginWithHttps);
        Anchor forgotPasswordLink = new Anchor("Forgot username or password");
        forgotPasswordLink.addClickHandler(forgotPassClickListener);

        VerticalPanel loginAndForgot = new VerticalPanel();
        loginAndForgot.add(signInButton);
        loginAndForgot.setCellHorizontalAlignment(signInButton, HasAlignment.ALIGN_CENTER);
        loginAndForgot.add(new HTML("<br>"));
        loginAndForgot.add(forgotPasswordLink);
        loginTable.setWidget(4, 1, loginAndForgot);
        loginTable.getFlexCellFormatter().setAlignment(4, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
        if (ClientApplicationPropertiesCache.getWebProtegeAuthenticateWithOpenId()) {
            win.setHeight(320);
            loginTable.getFlexCellFormatter().setColSpan(5, 0, 2);
            loginTable.setWidget(5, 0, openIdlabel);
            OpenIdIconPanel openIdIconPanel = new OpenIdIconPanel(win);
            openIdIconPanel.setWindowCloseHandlerRegistration(windowCloseHandlerRegistration);
            openIdIconPanel.setLoginWithHttps(isLoginWithHttps);
            loginTable.setWidget(6, 0, openIdIconPanel);
            loginTable.getFlexCellFormatter().setColSpan(6, 0, 3);
        }
        else {
            win.setHeight(230);
        }

        FlexTable topLoginTable = new FlexTable();
        topLoginTable.setWidget(0, 0, loginTable);
        topLoginTable.getFlexCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setPaddings(15);
        panel.setCls("loginpanel");
        panel.setLayout(new FitLayout());

        win.setLayout(new FitLayout());

        panel.add(topLoginTable, new AnchorLayoutData("-100 30%"));
        win.setTitle("Sign in");
        win.setClosable(true);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);


        win.addListener(new WindowListenerAdapter() {

            @Override
            public void onAfterLayout(Container self) {
                win.center();
            }
        });

        win.add(panel);
        win.setWidth(390);
        win.show();

        if (isLoginWithHttps) {
            win.setPosition(0, 0);
        }

        //NB: this is done like this because no other method I can find works. See http://cnxforum.com/showthread.php?t=226 for more details.
        Timer timer = new Timer() {
            @Override
            public void run() {
                userNameField.setFocus(true);
            }
        };
        timer.schedule(100);
    }

//    public void logout() {
//        MessageBox.confirm("Log out", "Are you sure you want to log out?", new MessageBox.ConfirmCallback() {
//            public void execute(String btnID) {
//                if (btnID.equalsIgnoreCase("yes")) {
//                    Application.get().setCurrentUser(null);
//                    AdminServiceManager.getInstance().logout(new AsyncCallback<Void>() {
//                        public void onFailure(Throwable caught) {
//                            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//                        }
//
//                        public void onSuccess(Void result) {
//                        }
//                    });
//                }
//            }
//        });
//    }

    private String getHintHtml() {
        return UIUtil.getHelpImageHtml("http://protegewiki.stanford.edu/wiki/WebProtegeOpenId", "Click here to learn how to log in with Open ID");
    }

    /**
     * @param win
     * @param userNameField
     * @param isLoginWithHttps
     * @return
     */
    private ClickHandler forgotPasswordClickListener(final Window win, final TextBox userNameField, final boolean isLoginWithHttps) {
        ClickHandler forgotPassClickListener = new ClickHandler() {
            public void onClick(ClickEvent event) {
                String user = userNameField.getText();
                if (user == null || user.length() == 0) {
                    MessageBox.setMaxWidth(350);
                    MessageBox.alert("Warning", "If you forgot your username, please send an email to the administrator.<br /><br />" + "If you forgot your password, please enter a user name in the user name field first and we will reset the password.");
                }
                else {
                    UIUtil.mask(win.getEl(), "Please wait until we reset your password and send you an email", true, 1);
                    if (isLoginWithHttps) {
                        AuthenticateServiceManager.getInstance().sendPasswordReminder(UserId.getUserId(userNameField.getText().trim()), new ForgotPassHandler(win));
                    }
                    else {
                        AdminServiceManager.getInstance().sendPasswordReminder(UserId.getUserId(userNameField.getText()), new ForgotPassHandler(win));
                    }
                }
            }
        };
        return forgotPassClickListener;
    }

    private void performSignIn(final boolean isLoginWithHttps, final Window win, final TextBox userNameField, final TextBox passwordField) {
        if (userNameField.getText().trim().equals("")) {
            MessageBox.alert("Please enter a user name.");
        }
        else if (isLoginWithHttps) {
            performSignInUsingHttps(userNameField.getText(), passwordField, win);
        }
        else {
            performSignInUsingEncryption(UserId.getUserId(userNameField.getText()), passwordField, win);
        }
    }

    private void checkIfOpenIdInSessionForLogin() {

        OpenIdServiceManager.getInstance().checkIfOpenIdInSessionForLogin(new AsyncCallback<UserData>() {

            public void onSuccess(UserData userId) {
                if (userId == null) {
                    enquireOpenIdAssociation();
                }
                else {
                    Application.get().setCurrentUser(UserId.getUserId(userId.getUserId().getUserName()));
                }
            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }


    public void getTimeoutAndCheckUserLoggedInMethod(final LoginUtil loginUtil, final String randomNumber) {
        final Integer timeout = ClientApplicationPropertiesCache.getServerPollingTimeoutMinutes();
        loginUtil.checkUserLoggedInMethod("" + randomNumber, timeout);
    }

    private void checkUserLoggedInMethod(final String randomNumber, final Integer timeout) {
        final long initTime = System.currentTimeMillis();
        final Timer checkSessionTimer = new Timer() {
            @Override
            public void run() {
                final Timer timer = this;
                long curTime = System.currentTimeMillis();
                long maxTime = 1000 * 60 * timeout;
                if (curTime - initTime > maxTime) {
                    timer.cancel();
                }
                AdminServiceManager.getInstance().checkUserLoggedInMethod(new AsyncCallback<String>() {

                    public void onSuccess(String loggedInMethod) {
                        if (loggedInMethod != null) {
                            timer.cancel();
                            if (loggedInMethod.equals(AuthenticationConstants.LOGIN_METHOD_WEBPROTEGE_ACCOUNT)) {
                                checkUserNameInSession();
                            }
                            else if (loggedInMethod.equals(AuthenticationConstants.LOGIN_METHOD_OPEN_ID_ACCOUNT)) {
                                checkIfOpenIdInSessionForLogin();
                            }

                        }
                    }

                    public void onFailure(Throwable caught) {
                        timer.cancel();
                    }
                });
                String loginSynId = Cookies.getCookie(AuthenticationConstants.HTTPS_WINDOW_CLOSED_COOKIE + "." + randomNumber);
                if (randomNumber != null && loginSynId != null) {
                    timer.cancel();
                    Cookies.removeCookie(AuthenticationConstants.HTTPS_WINDOW_CLOSED_COOKIE + "." + randomNumber);
                }
            }
        };
        checkSessionTimer.scheduleRepeating(2000);

    }

    /**
     * Method to create change password window.
     */
    public void changePassword(final UserId userName, boolean isLoginWithHttps) {
        if (userName.isGuest()) {
            throw new IllegalArgumentException("Cannot change the password of the guest user");
        }

        final Window win = new Window();

        FormPanel passwordFormPanel = new FormPanel();

        Label label = new Label("Welcome! Please enter your old password and new password:");
        label.setStyleName("login-welcome-msg");

        FlexTable changePassTable = new FlexTable();
        changePassTable.setWidget(0, 0, label);
        changePassTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        changePassTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        changePassTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        changePassTable.getFlexCellFormatter().setHeight(3, 0, "25px");
        changePassTable.getFlexCellFormatter().setHeight(4, 0, "25px");
        changePassTable.getFlexCellFormatter().setHeight(5, 0, "50px");
        passwordFormPanel.add(changePassTable);

        final PasswordTextBox oldPasswordField = new PasswordTextBox();
        oldPasswordField.setWidth("250px");
        Label oldPasswordLabel = new Label("Old Password:");
        oldPasswordLabel.setStyleName("label");
        changePassTable.setWidget(2, 0, oldPasswordLabel);
        changePassTable.setWidget(2, 1, oldPasswordField);

        final PasswordTextBox newPasswordField = new PasswordTextBox();
        newPasswordField.setWidth("250px");
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyleName("label");
        changePassTable.setWidget(3, 0, newPasswordLabel);
        changePassTable.setWidget(3, 1, newPasswordField);

        final PasswordTextBox newConfirmPassword = new PasswordTextBox();
        newConfirmPassword.setWidth("250px");
        Label newConfPasLabel = new Label("Confirm Password:");
        newConfPasLabel.setStyleName("label");
        changePassTable.setWidget(4, 0, newConfPasLabel);
        changePassTable.setWidget(4, 1, newConfirmPassword);

        Button changePasswordButton = new Button("Change Password");

        if (isLoginWithHttps) {
            changePasswordButton.addListener(new ChangePasswordWithHttpsListener(win, newConfirmPassword, userName, oldPasswordField, newPasswordField));
        }
        else {
            changePasswordButton.addListener(new ChangePasswordWithEncryptionListener(win, newConfirmPassword, userName, oldPasswordField, newPasswordField));
        }

        changePassTable.setWidget(5, 1, changePasswordButton);
        changePassTable.getFlexCellFormatter().setAlignment(5, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        win.addListener(new PanelListenerAdapter() {
            @Override
            public void onAfterLayout(Container self) {
                oldPasswordField.setFocus(true);
            }
        });

        win.setTitle("Change Password");
        win.setClosable(true);
        win.setWidth(428);
        win.setHeight(250);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(passwordFormPanel);
        win.show();
    }

    public void createNewUser(final boolean isLoginWithHttps) {
        final Window win = new Window();
        FormPanel newUserformPanel = new FormPanel();

        Label label = new Label("Please enter your Name, Password and other fields");
        label.setStyleName("login-welcome-msg");

        FlexTable newUserTable = new FlexTable();
        newUserTable.setWidget(0, 0, label);
        newUserTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        newUserTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        newUserTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(3, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(4, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(5, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(6, 0, "50px");
        newUserformPanel.add(newUserTable);

        final TextBox newUserID = new TextBox();
        newUserID.setWidth("250px");
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setStyleName("label");
        newUserTable.setWidget(2, 0, userIdLabel);
        newUserTable.setWidget(2, 1, newUserID);

        final TextBox newUserEmailID = new TextBox();
        newUserEmailID.setWidth("250px");
        Label emailIDLabel = new Label("Email:");
        emailIDLabel.setStyleName("label");
        newUserTable.setWidget(3, 0, emailIDLabel);
        newUserTable.setWidget(3, 1, newUserEmailID);

        final PasswordTextBox newUserPassword = new PasswordTextBox();
        newUserPassword.setWidth("250px");
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyleName("label");
        newUserTable.setWidget(4, 0, newPasswordLabel);
        newUserTable.setWidget(4, 1, newUserPassword);

        final PasswordTextBox confirmPassword = new PasswordTextBox();
        confirmPassword.setWidth("250px");
        Label newConfirmPassLabel = new Label("Confirm Password:");
        newConfirmPassLabel.setStyleName("label");
        newUserTable.setWidget(5, 0, newConfirmPassLabel);
        newUserTable.setWidget(5, 1, confirmPassword);

        confirmPassword.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (newUserEmailID.getText().trim().equals("") || newUserPassword.getText().trim().equals("") || confirmPassword.getText().trim().equals("")) {
                        MessageBox.alert("User ID and Password cannot be empty.");
                    }
                    else {
                        if (isLoginWithHttps) {
                            createNewUserViaHttps(newUserID.getText(), newUserPassword, confirmPassword, win);
                        }
                        else {
                            createNewUserViaEncryption(newUserID.getText(), newUserPassword, confirmPassword, newUserEmailID.getText(), win);
                        }
                    }
                }
            }
        });

        Button register = new Button("Register", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (newUserEmailID.getText().trim().equals("") || newUserPassword.getText().trim().equals("") || confirmPassword.getText().trim().equals("")) {
                    MessageBox.alert("Please fill in both the user name and the password.");
                }
                else {
                    if (isLoginWithHttps) {
                        createNewUserViaHttps(newUserID.getText(), newUserPassword, confirmPassword, win);
                    }
                    else {
                        createNewUserViaEncryption(newUserID.getText(), newUserPassword, confirmPassword, newUserEmailID.getText(), win);
                    }
                }
            }
        });

        Button cancel = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                win.close();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(20);
        buttonPanel.add(register);
        buttonPanel.add(cancel);

        newUserTable.setWidget(6, 1, buttonPanel);
        newUserTable.getFlexCellFormatter().setAlignment(6, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        win.setTitle("New User Registration");
        win.setClosable(true);
        win.setWidth(428);
        win.setHeight(240);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(newUserformPanel);
        win.show();
    }

    /**
     * Creates Edit profile Popup
     * @param isLoginWithHttps
     */

    public void createNewUserToAssociateOpenId(final Boolean isLoginWithHttps) {
        final Window win = new Window();
        FormPanel newUserformToAssocOpenIdPanel = new FormPanel();

        Label label = new Label("Please enter your name, password and email to create a new user account that will be associated with OpenId.");
        label.setStyleName("login-welcome-msg");

        FlexTable newUserTable = new FlexTable();
        newUserTable.setWidget(0, 0, label);
        newUserTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        newUserTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        newUserTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(3, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(4, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(5, 0, "25px");
        newUserTable.getFlexCellFormatter().setHeight(6, 0, "50px");
        newUserformToAssocOpenIdPanel.add(newUserTable);

        final TextBox newUserID = new TextBox();
        newUserID.setWidth("250px");
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setStyleName("label");
        newUserTable.setWidget(2, 0, userIdLabel);
        newUserTable.setWidget(2, 1, newUserID);

        final TextBox newUserEmailID = new TextBox();
        newUserEmailID.setWidth("250px");
        Label emailIDLabel = new Label("Email:");
        emailIDLabel.setStyleName("label");
        newUserTable.setWidget(3, 0, emailIDLabel);
        newUserTable.setWidget(3, 1, newUserEmailID);

        final PasswordTextBox newUserPassword = new PasswordTextBox();
        newUserPassword.setWidth("250px");
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyleName("label");
        newUserTable.setWidget(4, 0, newPasswordLabel);
        newUserTable.setWidget(4, 1, newUserPassword);

        final PasswordTextBox confirmPassword = new PasswordTextBox();
        confirmPassword.setWidth("250px");
        Label newConfirmPassLabel = new Label("Confirm Password:");
        newConfirmPassLabel.setStyleName("label");
        newUserTable.setWidget(5, 0, newConfirmPassLabel);
        newUserTable.setWidget(5, 1, confirmPassword);

        confirmPassword.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    OpenIdUtil openIdUtil = new OpenIdUtil();
                    if (newUserID.getText().trim().equals("") || newUserPassword.getText().trim().equals("") || confirmPassword.getText().trim().equals("")) {
                        MessageBox.alert("Please enter both a user name and a password.");
                    }
                    else if (isLoginWithHttps) {
                        openIdUtil.createNewUserToAssociateOpenIdWithHttps(newUserID.getText().trim(), newUserPassword, confirmPassword, newUserEmailID.getText().trim(), win);
                    }
                    else {
                        openIdUtil.createNewUserToAssocOpenIdWithEncryption(newUserID.getText().trim(), newUserPassword, confirmPassword, newUserEmailID.getText().trim(), win);
                    }
                }
            }
        });

        Button register = new Button("Register", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                OpenIdUtil openIdUtil = new OpenIdUtil();
                if (newUserID.getText().trim().equals("") || newUserPassword.getText().trim().equals("") || confirmPassword.getText().trim().equals("")) {
                    MessageBox.alert("Please enter both a user name and a password.");
                }
                else if (isLoginWithHttps) {
                    openIdUtil.createNewUserToAssociateOpenIdWithHttps(newUserID.getText().trim(), newUserPassword, confirmPassword, newUserEmailID.getText().trim(), win);
                }
                else {
                    openIdUtil.createNewUserToAssocOpenIdWithEncryption(newUserID.getText().trim(), newUserPassword, confirmPassword, newUserEmailID.getText().trim(), win);
                }
            }
        });

        Button cancel = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                win.close();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(20);
        buttonPanel.add(register);
        buttonPanel.add(cancel);

        newUserTable.setWidget(6, 1, buttonPanel);
        newUserTable.getFlexCellFormatter().setAlignment(6, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        win.setTitle("New User Registration");
        win.setClosable(true);
        win.setWidth(428);
        win.setHeight(240);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(newUserformToAssocOpenIdPanel);
        win.addListener(new PanelListenerAdapter() {
            @Override
            public void onAfterLayout(Container self) {
                newUserID.setFocus(true);
            }
        });
        win.show();
    }

    private void performSignInUsingEncryption(final UserId userName, final TextBox passField, final Window win) {
        AdminServiceManager.getInstance().getUserSaltAndChallenge(userName, new GetSaltAndChallengeForLoginHandler(userName, passField, win));
    }

    private void performSignInUsingHttps(final String userName, final TextBox passField, final Window win) {
        AuthenticateServiceManager.getInstance().validateUserAndAddInSession(userName, passField.getText(), new AsyncCallback<UserData>() {

            public void onSuccess(UserData userData) {
                if (!userData.getUserId().isGuest()) {
                    closeWindow();
                }
                else {
                    passField.setText("");
                    MessageBox.alert("Invalid user name or password. Please try again.");
                }
            }

            public void onFailure(Throwable caught) {
                passField.setText("");
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }

    /**
     * @param authType, httpsPort
     * @return
     */
    public String getAuthenticateWindowUrl(String authType, String httpsPort) {
        String queryString = com.google.gwt.user.client.Window.Location.getQueryString();
        if (queryString.trim().equals("")) {
            queryString = "?";

        }
        else {
            queryString = queryString + "&";
        }
        queryString = queryString + AuthenticationConstants.AUTHEN_TYPE + "=" + authType;

        String loginWindowUrl = GWT.getHostPageBaseURL() + AuthenticationConstants.AUTHENICATE_MODULE_HTML_FILE;
        loginWindowUrl = loginWindowUrl.replace(com.google.gwt.user.client.Window.Location.getProtocol(), "https:");

        String host = com.google.gwt.user.client.Window.Location.getHostName();
        if (httpsPort != null && httpsPort.trim().equals("")) {

            host = com.google.gwt.user.client.Window.Location.getHostName();
        }
        else if (httpsPort != null && !httpsPort.trim().equals("")) {
            host = com.google.gwt.user.client.Window.Location.getHostName() + ":" + httpsPort;
        }
        loginWindowUrl = loginWindowUrl.replace(com.google.gwt.user.client.Window.Location.getHost(), host);
        loginWindowUrl = loginWindowUrl + queryString;
        return loginWindowUrl;
    }

    private static void checkUserNameInSession() {
        AdminServiceManager.getInstance().getCurrentUserInSession(new AsyncCallback<UserId>() {

            public void onSuccess(UserId userId) {
                if (!userId.isGuest()) {
                    Application.get().setCurrentUser(userId);
                }
                else {
                    MessageBox.alert("Invalid user name or password. Please try again.");
                }
            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }

    private void createNewUserViaHttps(String userName, PasswordTextBox newUserPasswordField, PasswordTextBox newUserPassword2Field, Window win) {
        String newUserPassword = newUserPasswordField.getText();
        String newUserPassword2 = newUserPassword2Field.getText();

        if (newUserPassword.contentEquals(newUserPassword2)) {
            win.getEl().mask("Creating new user...", true);
            AuthenticateServiceManager.getInstance().registerUser(UserId.getUserId(userName), newUserPassword, new CreateNewUserHandlerViaHttps(win));
        }
        else {
            MessageBox.alert("Passwords do not match. Please try again.");
            newUserPasswordField.setValue("");
            newUserPassword2Field.setValue("");
        }

    }

    public void createNewUserViaEncryption(final String userName, PasswordTextBox newUserPasswordField, PasswordTextBox newUserPassword2Field, final String emailId, final com.gwtext.client.widgets.Window win) {
        final String newUserPassword = newUserPasswordField.getText();
        String newUserPassword2 = newUserPassword2Field.getText();

        if (newUserPassword.contentEquals(newUserPassword2)) {
            win.getEl().mask("Creating new user...", true);
            AdminServiceManager.getInstance().getNewSalt(new AsyncCallback<String>() {

                public void onSuccess(String salt) {
                    HashAlgorithm hAlgorithm = new HashAlgorithm();
                    String saltedHashedPass = hAlgorithm.md5(salt + newUserPassword);
                    AdminServiceManager.getInstance().registerUserViaEncrption(userName, saltedHashedPass, emailId, new CreateNewUserViaEncryptHandler(win));
                }

                public void onFailure(Throwable caught) {
                    MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
                }
            });
        }
        else {
            MessageBox.alert("Passwords do not match. Please try again.");
            newUserPasswordField.setValue("");
            newUserPassword2Field.setValue("");
        }

    }

    /*
     * Remote calls for Forgot password HyperLink
     */

    private final class CreateNewUserViaEncryptHandler implements AsyncCallback<UserData> {

        private final Window win;

        private CreateNewUserViaEncryptHandler(Window win) {
            this.win = win;
        }

        public void onSuccess(UserData userData) {
            win.getEl().unmask();
            if (userData != null) {
                win.close();
                MessageBox.alert("New user created successfully.");
            }
            else {
                MessageBox.alert("New user registration could not be completed. Please try again.");
            }
        }

        public void onFailure(Throwable caught) {
            GWT.log("Error at registering new user", caught);
            win.getEl().unmask();
            MessageBox.alert("There was an error at creating the new user. Please try again later.");
        }
    }

    private class ChangePasswordWithHttpsListener extends ButtonListenerAdapter {

        private final Window win;

        private final PasswordTextBox newConfirmPassword;

        private final UserId userName;

        private final PasswordTextBox oldPasswordField;

        private final PasswordTextBox newPasswordField;

        protected ChangePasswordWithHttpsListener(Window win, PasswordTextBox newConfirmPassword, UserId userName, PasswordTextBox oldPasswordField, PasswordTextBox newPasswordField) {
            this.win = win;
            this.newConfirmPassword = newConfirmPassword;
            this.userName = userName;
            this.oldPasswordField = oldPasswordField;
            this.newPasswordField = newPasswordField;
        }

        @Override
        public void onClick(Button button, EventObject e) {
            if (oldPasswordField.getText().trim().equals("") || newPasswordField.getText().trim().equals("") || newConfirmPassword.getText().trim().equals("")) {
                MessageBox.alert("Please enter both the old password and new password.");
            }
            else {
                changePassword();
            }
        }

        /**
         *
         */
        protected void changePassword() {
            if (newPasswordField.getText().equals(newConfirmPassword.getText())) {
                win.getEl().mask("Changing password...");
                AuthenticateServiceManager.getInstance().validateUser(userName, oldPasswordField.getText(), new AbstractAsyncHandler<UserData>() {
                    @Override
                    public void handleFailure(Throwable caught) {
                        GWT.log("Error at login", caught);
                        win.getEl().unmask();
                        MessageBox.alert("Changing the password failed. Please try again");
                        clearChangePasswordFields(oldPasswordField, newPasswordField, newConfirmPassword);
                    }

                    @Override
                    public void handleSuccess(UserData userData) {
                        win.getEl().unmask();
                        if (userData != null) {
                            AuthenticateServiceManager.getInstance().changePassword(userName, newPasswordField.getText(), new ChangePasswordHandler(win));
                        }
                        else {
                            MessageBox.alert("Invalid password. Please try again.");
                            clearChangePasswordFields(oldPasswordField, newPasswordField, newConfirmPassword);
                        }

                    }

                    private void clearChangePasswordFields(final PasswordTextBox oldPasswordField, final PasswordTextBox newPasswordField, final PasswordTextBox newConfirmPassword) {
                        oldPasswordField.setValue("");
                        newConfirmPassword.setValue("");
                        newPasswordField.setValue("");
                    }

                });
            }
            else {
                MessageBox.alert("Passwords do not match. Please enter them again.");
            }
        }
    }

    private class ChangePasswordWithEncryptionListener extends ButtonListenerAdapter {

        private final Window win;

        private final PasswordTextBox newConfirmPassword;

        private final UserId userName;

        private final PasswordTextBox oldPasswordField;

        private final PasswordTextBox newPasswordField;

        protected ChangePasswordWithEncryptionListener(Window win, PasswordTextBox newConfirmPassword, UserId userName, PasswordTextBox oldPasswordField, PasswordTextBox newPasswordField) {
            this.win = win;
            this.newConfirmPassword = newConfirmPassword;
            this.userName = userName;
            this.oldPasswordField = oldPasswordField;
            this.newPasswordField = newPasswordField;
        }

        @Override
        public void onClick(Button button, EventObject e) {
            if (oldPasswordField.getText().trim().equals("") || newPasswordField.getText().trim().equals("") || newConfirmPassword.getText().trim().equals("")) {
                MessageBox.alert("Please enter both the old password and the new password.");
            }
            else {
                changePassword();
            }
        }

        /**
         *
         */
        protected void changePassword() {
            if (newPasswordField.getText().trim().equals(newConfirmPassword.getText().trim())) {
                win.getEl().mask("Changing password...");
                AdminServiceManager.getInstance().getUserSaltAndChallenge(userName, new AsyncCallback<LoginChallengeData>() {

                    public void onSuccess(LoginChallengeData result) {
                        if (result != null) {
                            validateEncryptPassAndChangePass(result, win, newConfirmPassword, userName, oldPasswordField, newPasswordField);
                        }
                        else {
                            MessageBox.alert("Invalid password. Please try again.");
                            oldPasswordField.setValue("");
                            newPasswordField.setValue("");
                            newConfirmPassword.setValue("");
                        }

                    }

                    public void onFailure(Throwable caught) {
                        MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
                    }
                });
            }
            else {

                MessageBox.alert("Passwords do not match. Please enter them again.");
            }
        }
    }

    private class ForgotPassHandler extends AbstractAsyncHandler<Void> {

        private Window win;

        public ForgotPassHandler(Window win) {
            this.win = win;
        }

        @Override
        public void handleFailure(Throwable caught) {
            win.getEl().unmask();
            GWT.log("Error at forgot password callback : ", caught);
            MessageBox.alert("Error", "There was an error at sending the password reminder.<br />" + "Most likely your user account does not have an email account configured,<br />" + "or the email is invalid.");
        }

        @Override
        public void handleSuccess(Void nothing) {
            win.getEl().unmask();
            MessageBox.alert("Sent password", "Your password has been reset. You should receive an email with the new password.<br /> " + "Please change password the next time you log into the system.");
        }
    }

    /**
     * CallBack for change password process.
     */
    private class ChangePasswordHandler extends AbstractAsyncHandler<Void> {

        private Window win;

        public ChangePasswordHandler(Window win) {
            this.win = win;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at login", caught);
            win.getEl().unmask();
            MessageBox.alert("Error", "There was an error at changing the password.<br />Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            win.getEl().unmask();
            win.close();
            Date expireDate = new Date();
            long nowLong = expireDate.getTime();
            nowLong = nowLong + (1000 * 60 * 30);//30 minutes
            expireDate.setTime(nowLong);
            Cookies.setCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT, AuthenticationConstants.CHANGE_PASSWORD_SUCCESS, expireDate);
            closeWindow();
        }
    }

    private class CreateNewUserHandlerViaHttps extends AbstractAsyncHandler<UserData> {

        private Window win;

        public CreateNewUserHandlerViaHttps(Window win) {
            this.win = win;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at registering new user", caught);
            win.getEl().unmask();
            MessageBox.alert("There was an error at creating the new user. Please try again later.");
        }

        @Override
        public void handleSuccess(UserData userData) {
            win.getEl().unmask();
            if (userData != null) {

                MessageBox.alert("", "New user created successfully", new AlertCallback() {

                    public void execute() {
                        win.close();
                        closeWindow();
                    }
                });

            }
            else {
                MessageBox.alert("New user registration could not be completed. Please try again.");
            }
        }
    }

    native public void closeWindow()/*-{
        $wnd.close();
    }-*/;

    public void enquireOpenIdAssociation() {

        MessageBox.confirm("Associate OpenId with an existing user account", "Welcome! " +
                "The first time you sign in with your OpenId, we need to <b>associate it with an existing user account</b>. <br /><br />" +
                "Do you have an existing user account?", new ConfirmCallback() {

            public void execute(final String btnID) {

                final Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
                if (btnID.equalsIgnoreCase("yes")) {
                    if (isLoginWithHttps) {
                        associateCurrentWebProtegeAccount();
                    }
                    else {
                        loginToAssociateOpenId(isLoginWithHttps);
                    }
                }
                else if (btnID.equalsIgnoreCase("no")) {

                    AdminServiceManager.getInstance().allowsCreateUsers(new AsyncCallback<Boolean>() {
                        public void onFailure(Throwable caught) {
                            GWT.log("Error at allowsCreateUsers callback : ", caught);
                            MessageBox.alert("Error", "There was an error finding out if the server allows user creation.");
                        }

                        public void onSuccess(Boolean result) {
                            if (result) {
                                if (isLoginWithHttps) {
                                    createAndAssociateWebProtegeAccount();
                                }
                                else {
                                    createNewUserToAssociateOpenId(isLoginWithHttps);
                                }
                            }
                            else {
                                MessageBox.alert("The creation of new user accounts and linking the user accounts to OpenId is disabled." +
                                        "<br/><br/>" +
                                        "Please contact to the administrator to create a user account for you.");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * If User already has a protege account then after login the account is
     * associated with protege account.
     * @param isLoginWithHttps If true then the login popup is opened in a new browser popup
     */
    public void loginToAssociateOpenId(final Boolean isLoginWithHttps) {
        final Window win = new Window();

        final FormPanel loginFormPanel = new FormPanel();

        Label label = new Label();
        label.setText("Please enter below your existing account credentials to associate them with your OpenId:");
        label.setStyleName("login-welcome-msg");

        FlexTable loginTable = new FlexTable();
        loginTable.setWidget(0, 0, label);
        loginTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        loginTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        loginTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        loginTable.getFlexCellFormatter().setHeight(3, 0, "25px");
        loginTable.getFlexCellFormatter().setHeight(4, 0, "70px");

        loginFormPanel.add(loginTable);

        final TextBox userNameField = new TextBox();
        userNameField.setWidth("250px");
        Label userIdLabel = new Label("User name:");
        userIdLabel.setStyleName("label");
        loginTable.setWidget(2, 0, userIdLabel);
        loginTable.getFlexCellFormatter().setWidth(2, 0, "75px");
        loginTable.setWidget(2, 1, userNameField);

        final TextBox passwordField = new PasswordTextBox();
        passwordField.setWidth("250px");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyleName("label");
        loginTable.setWidget(3, 0, passwordLabel);
        loginTable.setWidget(3, 1, passwordField);

        userNameField.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    callSignAndAssocOpenId(isLoginWithHttps, win, userNameField, passwordField);
                }
            }
        });

        passwordField.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    callSignAndAssocOpenId(isLoginWithHttps, win, userNameField, passwordField);
                }
            }
        });

        Button signInButton = new Button("Sign In", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                callSignAndAssocOpenId(isLoginWithHttps, win, userNameField, passwordField);
            }
        });

        VerticalPanel loginPanel = new VerticalPanel();
        loginPanel.add(signInButton);
        loginPanel.setCellHorizontalAlignment(signInButton, HasAlignment.ALIGN_CENTER);

        loginTable.setWidget(4, 0, loginPanel);
        loginTable.getFlexCellFormatter().setColSpan(4, 0, 2);
        loginTable.getFlexCellFormatter().setAlignment(4, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        win.setTitle("Existing Account");
        win.setClosable(true);
        win.setWidth(360);
        win.setHeight(205);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(loginFormPanel);
        win.addListener(new PanelListenerAdapter() {
            @Override
            public void onAfterLayout(Container self) {
                userNameField.setFocus(true);

            }
        });
        win.show();
    }

    public native void openNewWindow(String url, String w, String h, String scrollbar)/*-{
        var sw = screen.width, sh = screen.height;
        var ulx = ((sw - w) / 2), uly = ((sh - h) / 2);
        var paramz = 'status=0,toolbar=0,menubar=0,location=0,resizable=0,scrollbars=' + scrollbar + ',width=' + w + ',height=' + h + '';
        var oSubWin = $wnd.open("", "_blank", paramz);
        oSubWin.moveTo(ulx, uly);
        oSubWin.location.replace(url);
    }-*/;


    private class GetSaltAndChallengeForLoginHandler extends AbstractAsyncHandler<LoginChallengeData> {

        private UserId userName;

        private TextBox passField;

        private Window win;

        public GetSaltAndChallengeForLoginHandler(final UserId userName, final TextBox passField, final Window win) {
            this.userName = userName;
            this.passField = passField;
            this.win = win;
        }

        @Override
        public void handleSuccess(LoginChallengeData result) {

            if (result != null) {
                HashAlgorithm hAlgorithm = new HashAlgorithm();
                String saltedHashedPass = hAlgorithm.md5(result.getSalt() + passField.getText());
                String response = hAlgorithm.md5(result.getChallenge() + saltedHashedPass);
                AdminServiceManager.getInstance().authenticateToLogin(userName, response, new AsyncCallback<UserId>() {

                    public void onSuccess(UserId userId) {
                        win.getEl().unmask();
                        if (!userId.isGuest()) {
                            Application.get().setCurrentUser(userId);
                            win.close();
                        }
                        else {
                            MessageBox.alert("Invalid user name or password. Please try again.");
                            passField.setValue("");
                        }

                    }

                    public void onFailure(Throwable caught) {
                        MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
                    }
                });
            }
            else {
                MessageBox.alert("Invalid user name or password. Please try again.");
                passField.setValue("");
            }
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
        }
    }

    /**
     * @param windowCloseHandlerRegistration the windowCloseHandlerRegistration to set
     */
    public void setWindowCloseHandlerRegistration(HandlerRegistration windowCloseHandlerRegistration) {
        this.windowCloseHandlerRegistration = windowCloseHandlerRegistration;
    }

    /**
     * @param result
     */
    protected void validateEncryptPassAndChangePass(LoginChallengeData result, final Window win, final PasswordTextBox newConfirmPassword, final UserId userName, final PasswordTextBox oldPasswordField, final PasswordTextBox newPasswordField) {
        final HashAlgorithm hAlgorithm = new HashAlgorithm();
        String saltedHashedPass = hAlgorithm.md5(result.getSalt() + oldPasswordField.getText());
        String response = hAlgorithm.md5(result.getChallenge() + saltedHashedPass);
        AdminServiceManager.getInstance().authenticateToLogin(userName, response, new AsyncCallback<UserId>() {

            public void onSuccess(UserId userData) {
                if (!userData.isGuest()) {
                    AdminServiceManager.getInstance().getNewSalt(new AsyncCallback<String>() {

                        public void onSuccess(String salt) {
                            String saltedHashedNewPass = hAlgorithm.md5(salt + newPasswordField.getText());
                            AdminServiceManager.getInstance().changePasswordEncrypted(userName, saltedHashedNewPass, salt, new AsyncCallback<Boolean>() {

                                public void onSuccess(Boolean result) {
                                    win.getEl().unmask();
                                    if (result) {
                                        MessageBox.alert("Password changed successfully");
                                    }
                                    else {
                                        MessageBox.alert("Changing the password failed. Please try again");
                                    }
                                    win.close();
                                }

                                public void onFailure(Throwable caught) {
                                    win.getEl().unmask();
                                    MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

                                }
                            });
                        }

                        public void onFailure(Throwable caught) {
                            win.getEl().unmask();
                            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

                        }
                    });
                }
                else {
                    win.getEl().unmask();
                    MessageBox.alert("Invalid password. Please try again.");
                    oldPasswordField.setValue("");
                    newPasswordField.setValue("");
                    newConfirmPassword.setValue("");
                }

            }

            public void onFailure(Throwable caught) {
                win.getEl().unmask();
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }

    /**
     *
     */
    private void associateCurrentWebProtegeAccount() {
        final String httsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
        OpenIdServiceManager.getInstance().clearAuthUserToAssocOpenIdSessData(new AsyncCallback<Void>() {

            public void onSuccess(Void result) {
                OpenIdUtil openIdUtil = new OpenIdUtil();
                openIdUtil.getTimeoutAndCheckIfAuthenToAssocId();
                LoginUtil loginUtil = new LoginUtil();
                String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_LOGIN_TO_ASSOC_OPEN_ID, httsPort);

                loginUtil.openNewWindow(authUrl, "375", "220", "0");

            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

            }
        });
    }

    /**
     *
     */
    private void createAndAssociateWebProtegeAccount() {
        final String httsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
        OpenIdServiceManager.getInstance().clearCreateUserToAssocOpenIdSessData(new AsyncCallback<Void>() {

            public void onSuccess(Void result) {
                OpenIdUtil openIdUtil = new OpenIdUtil();
                openIdUtil.getTimeoutAndCheckIfUserCreatedToAssocId();
                LoginUtil loginUtil = new LoginUtil();
                String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_CREATE_USER_TO_ASSOC_OPEN_ID, httsPort);

                loginUtil.openNewWindow(authUrl, "440", "260", "0");

            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

            }
        });
    }

    /**
     * @param isLoginWithHttps
     * @param win
     * @param userNameField
     * @param passwordField
     */
    private void callSignAndAssocOpenId(final Boolean isLoginWithHttps, final Window win, final TextBox userNameField, final TextBox passwordField) {
        win.getEl().mask("Signing in...");
        OpenIdUtil openIdUtil = new OpenIdUtil();
        if (userNameField.getText().trim().equals("")) {
            win.getEl().unmask();
            MessageBox.alert("Please enter a user name.");
        }
        else if (isLoginWithHttps) {
            openIdUtil.signInToAssocOpenIdWithHttps(userNameField.getText().trim(), passwordField, win);
        }
        else {
            openIdUtil.signInToAssocOpenIdWithEncryption(userNameField.getText().trim(), passwordField, win);
        }
    }

}
