package edu.stanford.bmir.protege.web.client.ui.openid;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwtext.client.widgets.MessageBox;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AuthenticateServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OpenIdServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.client.ui.login.HashAlgorithm;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;

/**
 *
 * @author z.khan
 */
public class OpenIdUtil {

    public static final String REGISTRATION_RESULT_PROP = "registrationResult";

    protected String imageBaseUrl = null;
    final protected PopupPanel openIdPopup = new PopupPanel();

    public OpenIdUtil() {
        imageBaseUrl = GWT.getModuleBaseURL();
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));
    }

    public void displayUsersOpenIdList(OpenIdData openIdData, final FlexTable editProfileTable,
            final com.gwtext.client.widgets.Window win, boolean toOpenDisclosurePanel//to open list disclosure of not
            , final int windowBaseHt//base height of window
    ) {
        DisclosurePanel openIdListDisclosure = new DisclosurePanel();

        ScrollPanel listPanel = new ScrollPanel();
        FlexTable openIdAssocTable = new FlexTable();
        FlexTable listTable = new FlexTable();

        List<String> openIdList = openIdData.getOpenIdList();
        List<String> openIdAccIdList = openIdData.getOpenIdAccId();
        List<String> openIdProviderList = openIdData.getOpenIdProvider();

        HTML assocHeader;
        if (toOpenDisclosurePanel) {
            assocHeader = new HTML(
                    "Associated OpenId <span style='font-size:100%; margin-top: 3px;'>&nbsp;&#9660;</span>");
        } else {
            assocHeader = new HTML(
                    "Associated OpenId <span style='font-size:100%; margin-top: 3px;'>&nbsp;&#9658;</span>");
        }
        assocHeader.setStyleName("login-welcome-msg");
        openIdListDisclosure.setHeader(assocHeader);

        final FlexTable editProfileTab = editProfileTable;
        int rowIndex = 3;
        if (openIdList != null) {
            if (openIdList.size() == 0) {
                listTable
                        .setWidget(
                                rowIndex,
                                0,
                                new HTML(
                                        "There is no open id associated to your account. You can associate one by clicking on the \"<b>Add new OpenId</b>\" link"));
            }
            for (Iterator iterator = openIdList.iterator(), iterator2 = openIdAccIdList.iterator(), iterator3 = openIdProviderList
                    .iterator(); iterator.hasNext();) {
                String openId = (String) iterator.next();
                String openIdAccId = (String) iterator2.next();
                String openIdProvider = (String) iterator3.next();
                Label openIdLabel = new Label(openIdAccId + " on " + openIdProvider);
                openIdLabel.setTitle(openId);

                Image removeAssocImage = getWidgetRemoveAssoc(win, windowBaseHt, editProfileTab, openId);

                listTable.setWidget(rowIndex, 0, openIdLabel);
                listTable.setWidget(rowIndex, 1, removeAssocImage);
                listTable.getFlexCellFormatter().setAlignment(rowIndex, 1, HasAlignment.ALIGN_RIGHT,
                        HasAlignment.ALIGN_MIDDLE);
                rowIndex++;
            }
        } else {
            listTable
                    .setWidget(
                            rowIndex,
                            0,
                            new HTML(
                                    "There is no open id associated to your account. You can associate one by clicking on the \"<b>Add new OpenId</b>\" link"));
        }

        HorizontalPanel addNewOpenIdPanel = getWidgetAddNewOpenId(editProfileTable, win, windowBaseHt);

        listTable.setWidth("290px");
        listPanel.setSize("315px", "68px");
        listPanel.add(listTable);

        openIdAssocTable.setWidget(1, 0, addNewOpenIdPanel);
        openIdAssocTable.setWidget(2, 0, listPanel);
        openIdAssocTable.getFlexCellFormatter().setColSpan(1, 0, 2);
        openIdAssocTable.getFlexCellFormatter().setAlignment(1, 0, HasAlignment.ALIGN_RIGHT, HasAlignment.ALIGN_MIDDLE);
        editProfileTable.getFlexCellFormatter().setColSpan(8, 0, 2);
        openIdListDisclosure.add(openIdAssocTable);
        editProfileTable.setWidget(8, 0, openIdListDisclosure);
        editProfileTable.getFlexCellFormatter().setColSpan(8, 0, 2);

        openIdListDisclosure.setOpen(toOpenDisclosurePanel);
        setDisclosurePanelHandlers(win, windowBaseHt, openIdListDisclosure);
    }

    /**
     * Adds Open and Close handlers to disclosure panel
     *
     * @param win
     * @param windowBaseHt
     * @param openIdListDisclosure
     */
    protected void setDisclosurePanelHandlers(final com.gwtext.client.widgets.Window win, final int windowBaseHt,
            DisclosurePanel openIdListDisclosure) {
        openIdListDisclosure.addOpenHandler(new OpenHandler<DisclosurePanel>() {

            public void onOpen(OpenEvent<DisclosurePanel> event) {
                DisclosurePanel disPanel = (DisclosurePanel) event.getSource();
                win.setHeight(windowBaseHt + disPanel.getOffsetHeight());
                HTML assocHeader = new HTML(
                        "Associated OpenId <span style='font-size:100%; margin-top: 3px;'>&nbsp;&#9660;</span>");
                assocHeader.setStyleName("login-welcome-msg");
                disPanel.setHeader(assocHeader);
            }
        });

        openIdListDisclosure.addCloseHandler(new CloseHandler<DisclosurePanel>() {

            public void onClose(CloseEvent<DisclosurePanel> event) {
                DisclosurePanel disPanel = (DisclosurePanel) event.getSource();
                win.setHeight(windowBaseHt);
                HTML assocHeader = new HTML(
                        "Associated OpenId <span style='font-size:100%; margin-top: 3px;'>&nbsp;&#9658;</span>");
                assocHeader.setStyleName("login-welcome-msg");
                disPanel.setHeader(assocHeader);

            }
        });
    }

    /**
     * Creates Remove Association widget
     *
     * @param win
     * @param windowBaseHt
     * @param editProfileTab
     * @param openId
     * @return
     */
    protected Image getWidgetRemoveAssoc(final com.gwtext.client.widgets.Window win, final int windowBaseHt,
            final FlexTable editProfileTab, String openId) {
        Image removeAssocImage = new Image(imageBaseUrl + "/images/delete_grey.png");
        removeAssocImage.getElement().setId(openId);
        removeAssocImage.setTitle("Remove OpenId");
        removeAssocImage.setStyleName("menuBar");

        removeAssocImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Image remImage = (Image) event.getSource();
                String openIdStr = remImage.getElement().getId();
                String name = GlobalSettings.getGlobalSettings().getUserName();

                OpenIdServiceManager.getInstance().removeAssocToOpenId(name, openIdStr,
                        new AsyncCallback<OpenIdData>() {

                            public void onSuccess(OpenIdData openIdData) {

                                displayUsersOpenIdList(openIdData, editProfileTab, win, true, windowBaseHt);
                            }

                            public void onFailure(Throwable caught) {

                            }
                        });
            }
        });
        return removeAssocImage;
    }

    /**
     * @param editProfileTable
     * @param win
     * @param windowBaseHt
     * @return
     */
    protected HorizontalPanel getWidgetAddNewOpenId(final FlexTable editProfileTable,
            final com.gwtext.client.widgets.Window win, final int windowBaseHt) {
        Image addNewOpenIdImage = new Image(imageBaseUrl + "/images/add.png");
        addNewOpenIdImage.setStyleName("menuBar");
        addNewOpenIdImage.setTitle("Add New OpenId");
        addNewOpenIdImage.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                OpenIdAllIconPopup popUp = new OpenIdAllIconPopup(editProfileTable, win, windowBaseHt);

                Image addNewOIImage = (Image) event.getSource();
                addNewOIImage.getAbsoluteLeft();
                int left = addNewOIImage.getAbsoluteLeft();
                int top = addNewOIImage.getAbsoluteTop();
                popUp.setSize("100px", "100px");
                popUp.setPopupPosition(left + 100, top - 80);

                popUp.show();
            }
        });

        HTML addNewOpenIdLabel = new HTML("&nbsp<b><span style='font-size:100%;'>Add new OpenId</span></b>");
        addNewOpenIdLabel.setTitle("Add New OpenId");
        addNewOpenIdLabel.setStyleName("links-blue");
        addNewOpenIdLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                OpenIdAllIconPopup popUp = new OpenIdAllIconPopup(editProfileTable, win, windowBaseHt);

                HTML addNewOIHTML = (HTML) event.getSource();
                addNewOIHTML.getAbsoluteLeft();
                int left = addNewOIHTML.getAbsoluteLeft();
                int top = addNewOIHTML.getAbsoluteTop();
                popUp.setSize("100px", "100px");
                popUp.setPopupPosition(left + 100, top - 80);

                popUp.show();
            }
        });

        HorizontalPanel addNewOpenIdPanel = new HorizontalPanel();
        addNewOpenIdPanel.add(addNewOpenIdImage);
        addNewOpenIdPanel.add(addNewOpenIdLabel);
        return addNewOpenIdPanel;
    }

    public void openProviderForOpenIdAuth(final int provId, boolean isLoginWithHttps) {
        detectPopup("A pop-up blocker has hidden the OpenId authentication screen. Please disable your pop-up blocker and try again.");
        String openidUrl = GWT.getHostPageBaseURL() + OpenIdConstants.OPENID_AUTH_SERVLET_URLPATTERN;
        openidUrl = openidUrl + "?";
        if (isLoginWithHttps) {
            openidUrl = openidUrl + AuthenticationConstants.DOMAIN_NAME_AND_PORT + "="
                    + Window.Location.getParameter(AuthenticationConstants.DOMAIN_NAME_AND_PORT) + "&";
            openidUrl = openidUrl + AuthenticationConstants.PROTOCOL + "="
                    + Window.Location.getParameter(AuthenticationConstants.PROTOCOL) + "&";
        }

        switch (provId) {
        case 1:// Google
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=Google&";
            openidUrl = openidUrl + "openId=http://www.google.com/accounts/o8/id";
            break;

        case 2:// Yahoo
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=Yahoo&";
            openidUrl = openidUrl + "openId=http://www.yahoo.com";
            break;
        case 3:// Open id
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=MyOpenId&";
            openidUrl = openidUrl + "openId=http://www.myopenid.com";
            break;
        case 4:// My Space
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=MySpace&";
            openidUrl = openidUrl + "openId=http://myspace.com";
            break;
        case 5:// Flick
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=Flickr&";
            openidUrl = openidUrl + "openId=http://flickr.com";
            break;
        case 6:// myVidoop
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=myVidoop&";
            openidUrl = openidUrl + "openId=http://myvidoop.com";
            break;
        case 7:// Verisign
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=Verisign&";
            openidUrl = openidUrl + "openId=http://pip.verisignlabs.com";
            break;
        case 8:// MyId
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=MyId&";
            openidUrl = openidUrl + "openId=http://www.myid.net";
            break;
        case 10:// Aol
            openidUrl = openidUrl + OpenIdConstants.HTTPSESSION_OPENID_PROVIDER + "=Aol&";
            openidUrl = openidUrl + "openId=http://aol.com";
            break;
        default:

            break;
        }

        openNewWindow(openidUrl, "675", "475", "1");
    }

    public void getTimeoutAndCheckIfUserCreatedToAssocId() {
        final Integer timeout = ClientApplicationPropertiesCache.getServerPollingTimeoutMinutes();
        clearCreateUserToAssocIdCookies();
        checkIfUserCreatedToAssocOpenId(timeout);
    }

    public void checkIfUserCreatedToAssocOpenId(final Integer timeout) {
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

                String createUserRes = Cookies.getCookie(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT);
                if (createUserRes != null
                        && createUserRes.equalsIgnoreCase(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_SUCCESS)) {
                    OpenIdServiceManager.getInstance().checkIfUserCreatedToAssocOpenId(new AsyncCallback<UserData>() {

                        public void onSuccess(UserData userData) {
                            if (userData != null) {
                                GlobalSettings.getGlobalSettings().setUser(userData);
                                MessageBox.alert("New user created successfully and associated with your Open Id.");
                                timer.cancel();
                            }
                        }

                        public void onFailure(Throwable caught) {
                            timer.cancel();
                        }
                    });
                } else if (createUserRes != null
                        && createUserRes.equalsIgnoreCase(AuthenticationConstants.CREATE_USER_WINDOW_CLOSED)) {
                    timer.cancel();
                } else if (createUserRes != null
                        && createUserRes.equalsIgnoreCase(AuthenticationConstants.CREATE_USER_FOR_OPENID_FUN_FAIL)) {
                    MessageBox.alert("There was an error at creating the new user. Please try again later.");
                }
                Cookies.removeCookie(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT);
            }
        };
        checkSessionTimer.scheduleRepeating(2000);
    }

    public void getTimeoutAndCheckIfAuthenToAssocId() {
        final Integer timeout = ClientApplicationPropertiesCache.getServerPollingTimeoutMinutes();
        clearAuthenToAssocIdCookies();
        checkIfUserAuthenToAssocOpenId(timeout);
    }

    public void checkIfUserAuthenToAssocOpenId(final Integer timeout) {
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

                String authenUserRes = Cookies.getCookie(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT);
                if (authenUserRes.equalsIgnoreCase(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_SUCCESS)) {
                    OpenIdServiceManager.getInstance().checkIfUserAuthenticatedToAssocOpenId(
                            new AsyncCallback<UserData>() {

                                public void onSuccess(UserData userData) {
                                    if (userData != null) {
                                        GlobalSettings.getGlobalSettings().setUser(userData);
                                        timer.cancel();
                                        MessageBox.alert("Your user account was successfully associated with your OpenId. <br /><br/>" +
                                        		"For future sessions, you can simply sign in with your OpenId account.");
                                    }
                                }

                                public void onFailure(Throwable caught) {
                                    timer.cancel();

                                }
                            });

                } else if (authenUserRes.equalsIgnoreCase(AuthenticationConstants.AUTHEN_USER_WINDOW_CLOSED)) {
                    timer.cancel();
                } else if (authenUserRes.equalsIgnoreCase(AuthenticationConstants.CREATE_USER_FOR_OPENID_FUN_FAIL)) {
                    MessageBox.alert("There was an error at creating the new user. Please try again later.");
                }
                Cookies.removeCookie(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT);
            }
        };
        checkSessionTimer.scheduleRepeating(2000);

    }

    protected void clearAuthenToAssocIdCookies() {
        Cookies.removeCookie(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT);
    }

    protected void clearCreateUserToAssocIdCookies() {
        Cookies.removeCookie(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT);
    }

    native protected void openNewWindow(String openidUrl, String w, String h, String scrollbar)/*-{
        var sw = screen.width, sh = screen.height;
        var ulx = ((sw-w)/2), uly = ((sh-h)/2);
        var paramz = 'status=0,toolbar=0,menubar=0,location=0,resizable=0,scrollbars='+scrollbar+',width='+w+',height='+h+'';
        var oSubWin = window.open( "", "_blank", paramz );
        oSubWin.moveTo( ulx,uly );
        oSubWin.location.replace(openidUrl);
    }-*/;

    public void signInToAssocOpenIdWithEncryption(String userName, TextBox passwordField,
            com.gwtext.client.widgets.Window win) {
        AdminServiceManager.getInstance().getUserSaltAndChallenge(userName,
                new SignIntoAssocOpenIdWithEncryptHandler(userName, passwordField, win));

    }

    public static native void detectPopup(String message)/*-{
    var windowName = 'userConsole';
    var popUp = window.open('', windowName, 'width=10, height=10, left=24, top=24, scrollbars, resizable');
    if (popUp == null || typeof(popUp)=='undefined') {
        $wnd.Ext.MessageBox.alert("Pop-up Blocker Detected", message);
    }

    else {
        popUp.close();
    }
    }-*/;

    class SignIntoAssocOpenIdWithEncryptHandler extends AbstractAsyncHandler<LoginChallengeData> {
        private String userName;
        private TextBox passField;
        private com.gwtext.client.widgets.Window win;

        public SignIntoAssocOpenIdWithEncryptHandler(final String userName, final TextBox passField,
                final com.gwtext.client.widgets.Window win) {
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
                OpenIdServiceManager.getInstance().validateUserToAssocOpenIdWithEncrypt(userName, response,
                        new AsyncCallback<UserData>() {

                            public void onSuccess(UserData userData) {
                                win.getEl().unmask();
                                if (userData != null) {
                                    GlobalSettings.getGlobalSettings().setUser(userData);
                                    win.close();
                                    MessageBox.alert("Your user account was successfully associated with your OpenId.<br />" +
                                    		"For future sessions, you can simply sign in with your OpenId.");
                                } else {
                                    MessageBox.alert("Invalid user name or password. Please try again.");
                                    passField.setValue("");
                                }
                            }

                            public void onFailure(Throwable caught) {
                                win.getEl().unmask();
                                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

                            }
                        });
            } else {
                win.getEl().unmask();
                MessageBox.alert("Invalid user name or password. Please try again.");
                passField.setValue("");
            }
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
        }
    }

    public void signInToAssocOpenIdWithHttps(String userName, TextBox passField, com.gwtext.client.widgets.Window win) {

        AuthenticateServiceManager.getInstance().validateUserToAssociateOpenId(userName, passField.getText(),
                new SignIntoAssocOpenIdWithHttpsHandler(win, passField));
    }

    class SignIntoAssocOpenIdWithHttpsHandler extends AbstractAsyncHandler<UserData> {
        private com.gwtext.client.widgets.Window win;
        private TextBox passField;

        public SignIntoAssocOpenIdWithHttpsHandler(com.gwtext.client.widgets.Window win, TextBox passField) {
            this.win = win;
            this.passField = passField;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at login", caught);
            win.getEl().unmask();
            MessageBox.alert("Login failed. Please try again.");
            passField.setValue("");
            Date expireDate = new Date();
            long nowLong = expireDate.getTime();
            nowLong = nowLong + (1000 * 60 * 30);//30 minutes
            expireDate.setTime(nowLong);
            Cookies.setCookie(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT,
                    AuthenticationConstants.AUTHEN_USER_FOR_OPENID_FUN_FAIL, expireDate);
            closeWindow();
        }

        @Override
        public void handleSuccess(UserData userData) {
            win.getEl().unmask();
            Date expireDate = new Date();
            long nowLong = expireDate.getTime();
            nowLong = nowLong + (1000 * 60 * 30);//30 minutes
            expireDate.setTime(nowLong);
            if (userData != null) {

                Cookies.setCookie(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT,
                        AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_SUCCESS, expireDate);
                closeWindow();
                win.close();
            } else {
                MessageBox.alert("Invalid user name or password. Please try again.");
                passField.setValue("");
                passField.setFocus(true);
            }
        }
    }

    public void createNewUserToAssocOpenIdWithEncryption(final String userName, PasswordTextBox newUserPasswordField,
            PasswordTextBox newUserPassword2Field, final String emailId, final com.gwtext.client.widgets.Window win) {

        final String newUserPassword = newUserPasswordField.getText();
        String newUserPassword2 = newUserPassword2Field.getText();

        if (newUserPassword.contentEquals(newUserPassword2)) {
            win.getEl().mask("Creating new user...", true);
            AdminServiceManager.getInstance().getNewSalt(new AsyncCallback<String>() {

                public void onSuccess(String salt) {
                    HashAlgorithm hAlgorithm = new HashAlgorithm();
                    String saltedHashedPass = hAlgorithm.md5(salt + newUserPassword);

                    OpenIdServiceManager.getInstance().registerUserToAssocOpenIdWithEncrption(userName, saltedHashedPass,
                            emailId, new AsyncCallback<UserData>() {

                                public void onSuccess(UserData userData) {
                                    win.getEl().unmask();

                                    String result = userData.getProperty(REGISTRATION_RESULT_PROP);

                                    if (userData == null || result == null || result.equals(OpenIdConstants.REGISTER_USER_ERROR)) {
                                        MessageBox.alert("New user registration could not be completed. Please try again.");
                                    } else {
                                        if (result != null && result.equals(OpenIdConstants.USER_ALREADY_EXISTS)) {
                                            MessageBox.alert("User '" + userName + "' already exists.");
                                        } else if (result != null && result.equals(OpenIdConstants.REGISTER_USER_SUCCESS)) {
                                            win.close();
                                            GlobalSettings.getGlobalSettings().setUser(userData);
                                            MessageBox.alert("New user created successfully and associated with your Open Id.");
                                        }
                                    }
                                }

                                public void onFailure(Throwable caught) {
                                    GWT.log("Error at registering new user", caught);
                                    win.getEl().unmask();
                                    MessageBox.alert("There was an error at creating the new user. Please try again later.");
                                }
                            });
                }

                public void onFailure(Throwable caught) {
                    MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
                }
            });
        } else {
            MessageBox.alert("Passwords dont match. Please try again.");
            newUserPasswordField.setValue("");
            newUserPassword2Field.setValue("");
        }

    }

    public void createNewUserToAssociateOpenIdWithHttps(String userName, PasswordTextBox newUserPasswordField,
            PasswordTextBox newUserPassword2Field, String emailId, com.gwtext.client.widgets.Window win) {
        String newUserPassword = newUserPasswordField.getText();
        String newUserPassword2 = newUserPassword2Field.getText();

        if (newUserPassword.contentEquals(newUserPassword2)) {
            win.getEl().mask("Creating new user...", true);
            AuthenticateServiceManager.getInstance().registerUserToAssociateOpenId(userName, newUserPassword, emailId,
                    new createNewUserToAssocOpenIdHandler(userName, win));
        } else {
            MessageBox.alert("Passwords dont match. Please try again.");
            newUserPasswordField.setValue("");
            newUserPassword2Field.setValue("");
        }

    }

    class createNewUserToAssocOpenIdHandler extends AbstractAsyncHandler<UserData> {

        private com.gwtext.client.widgets.Window win;
        private String userName;

        public createNewUserToAssocOpenIdHandler(String userName, com.gwtext.client.widgets.Window win) {
            this.userName = userName;
            this.win = win;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at registering new user", caught);
            win.getEl().unmask();
            Date expireDate = new Date();
            long nowLong = expireDate.getTime();
            nowLong = nowLong + (1000 * 60 * 30);//30 minutes
            expireDate.setTime(nowLong);
            Cookies.setCookie(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT,
                    AuthenticationConstants.CREATE_USER_FOR_OPENID_FUN_FAIL, expireDate);
            closeWindow();
        }

        @Override
        public void handleSuccess(UserData userData) {
            win.getEl().unmask();

            Date expireDate = new Date();
            long nowLong = expireDate.getTime();
            nowLong = nowLong + (1000 * 60 * 30);//30 minutes
            expireDate.setTime(nowLong);

            String result = userData.getProperty(REGISTRATION_RESULT_PROP);

            if (userData == null || result == null || result.equals(OpenIdConstants.REGISTER_USER_ERROR)) {
                MessageBox.alert("New user registration could not be completed. Please try again.");
            } else {
                if (result != null) {
                    if (result.equals(OpenIdConstants.USER_ALREADY_EXISTS)) {
                        MessageBox.alert("User '" + userName + "' already exists.");
                    } else if (result.equals(OpenIdConstants.REGISTER_USER_SUCCESS)) {
                        win.close();
                        Cookies.setCookie(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT, AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_SUCCESS, expireDate);
                        closeWindow();
                    }
                }
            }
        }

    }

    native public void closeWindow()/*-{
        $wnd.close();
    }-*/;
}
