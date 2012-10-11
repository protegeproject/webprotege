package edu.stanford.bmir.protege.web.client.ui;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.PermissionConstants;
import edu.stanford.bmir.protege.web.client.model.ShareOntologyAccessEventManager;
import edu.stanford.bmir.protege.web.client.model.SystemEventManager;
import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.UpdateShareLinkEvent;
import edu.stanford.bmir.protege.web.client.model.listener.ShareOntologyAccessListenerAdapter;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListenerAdapter;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.bioportal.upload.BioPortalUploadDialog;
import edu.stanford.bmir.protege.web.client.ui.editprofile.EditProfileUtil;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.AccessPolicyUtil;
import edu.stanford.bmir.protege.web.client.ui.ontology.sharing.SharingSettingsDialog;
import edu.stanford.bmir.protege.web.client.ui.projectconfig.ProjectConfigurationDialog;
import edu.stanford.bmir.protege.web.client.ui.signup.WebProtegeSignupDialog;

/**
 * The panel shown at the top of the display. It contains the documentation
 * links, the sign in/out links, and may contain other menus, etc.
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
@SuppressWarnings("deprecation")
public class TopPanel extends Panel {

    public static final String SIGN_UP_BUTTON_TEXT = "Sign Up for Account";

    private HTML signInOutHtml;

    private HTML userNameHtml;

    private HTML shareHtml;

    private HTML publishHtml;

    private HTML configureHtml;

    //    private final Images images = (Images) GWT.create(Images.class);
    private HorizontalPanel optionsLinks;

    private HorizontalPanel shareLinkPanel;

    private HorizontalPanel publishLinkPanel;

    private HorizontalPanel configureLinkPanel;

    private HorizontalPanel signupPanel;

    private MenuBar verticalOptionsMenu;

    private MenuItem addUser;

    private String currentSelectedProject = null; // Set only if user is owner of the project

    private AccessPolicyUtil accessPolicyUtil = null;

    public interface Images extends ImageBundle {

        public AbstractImagePrototype iCatLogo();
    }

    public TopPanel() {
        setLayout(new FitLayout());
        setAutoWidth(true);
        setCls("top-panel");

        // Outer panel to house logo and inner panel
        HorizontalPanel outer = new HorizontalPanel();
        outer.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        final Image logo = getImage();
        if (logo != null) {
            outer.add(logo);
            outer.setCellHorizontalAlignment(logo, HorizontalPanel.ALIGN_LEFT);
        }


        // Inner panel to house links panel
        VerticalPanel inner = new VerticalPanel();
        inner.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

        inner.add(getSignupPanel());

        inner.add(getLinksPanel());

        outer.add(inner);
        add(outer);

        adjustOptionPanel();

        SystemEventManager.getSystemEventManager().addLoginListener(new SystemListenerAdapter() {
            @Override
            public void onLogin(LoginEvent loginEvent) {
                adjustUserNameText();
                adjustOptionPanel();
                AccessPolicyUtil.updateShareLink(currentSelectedProject);

            }

            @Override
            public void onLogout(LoginEvent loginEvent) {
                adjustUserNameText();
                adjustOptionPanel();
                AccessPolicyUtil.updateShareLink(currentSelectedProject);
                AdminServiceManager.getInstance().logout(new AsyncCallback<Void>() {

                    public void onSuccess(Void result) {
                    }

                    public void onFailure(Throwable caught) {
                        GWT.log("Could not logout from server", caught);
                    }
                });
            }
        });

        ShareOntologyAccessEventManager.getShareOntologyAccessManager().addShareLinkVisibilityListener(new ShareOntologyAccessListenerAdapter() {

            /*
            * (non-Javadoc)
            *
            * @see
            * edu.stanford.bmir.protege.web.client.model.listener
            * .
            * SystemListenerAdapter#onProjectChanged(edu.stanford
            * .bmir.protege.web.client.model.event.
            * ProjectChangedEvent)
            */
            @Override
            public void updateShareLink(UpdateShareLinkEvent projectChangedEvent) {
                currentSelectedProject = projectChangedEvent.getCurrentSelectedProject();
                shareLinkPanel.setVisible(projectChangedEvent.isShowShareLink());
                publishLinkPanel.setVisible(projectChangedEvent.isShowShareLink());
                updateShareLink(projectChangedEvent.isShowShareLink());
            }

            /*
            * (non-Javadoc)
            *
            * @see
            * edu.stanford.bmir.protege.web.client.model.listener
            * .SystemListenerAdapter#updateShareLink(boolean)
            */
            @Override
            public void updateShareLink(boolean showShareLink) {
                shareLinkPanel.setVisible(showShareLink);
                // TODO: This is a temporary hack.  If a person can share then they can configure
                configureLinkPanel.setVisible(showShareLink);
            }

        });

        AdminServiceManager.getInstance().getCurrentUserInSession(new AsyncCallback<UserData>() {
            public void onSuccess(UserData userData) {
                GlobalSettings.getGlobalSettings().setUser(userData);
            }

            public void onFailure(Throwable caught) {
                GWT.log("Could not get server permission from server", caught);
            }
        });
    }

    protected Image getImage() {
        return null;//images.iCatLogo().createImage();
    }

    /**
     * Method for to displaying the Option link. After SignIn "Options" link
     * should be visible otherwise disable
     */
    public void adjustOptionPanel() {
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        if (userName != null) { // login
            signupPanel.setVisible(false);
            optionsLinks.setVisible(true);
            AdminServiceManager.getInstance().getAllowedServerOperations(userName, new AsyncCallback<Collection<String>>() {
                public void onSuccess(Collection<String> operations) {
                    if (operations.contains(PermissionConstants.CREATE_USERS)) {
                        addUserMenuItem();
                    }
                }

                public void onFailure(Throwable caught) {
                    GWT.log("Could not get server permission from server", caught);
                }
            });
            configureLinkPanel.setVisible(true);
        }
        else { // logout
            signupPanel.setVisible(true);
            verticalOptionsMenu.removeItem(addUser);
            optionsLinks.setVisible(false);
            configureLinkPanel.setVisible(false);
        }
//        addUserMenuItem();
    }

    protected HorizontalPanel getLinksPanel() {
        HorizontalPanel links = new HorizontalPanel();
        links.setSpacing(2);

        // User name text and/or sign in/out message
        links.add(getUserNameHtml());

        links.add(new HTML("<span style='font-size:75%;'>&nbsp;|&nbsp;</span>"));

        // Sign In and/or Sign Out link
        links.add(getSignInOutHtml());

        links.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));

        // Adding Options menu link
        links.add(getOptionsPanel());

        optionsLinks.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));

        // Adding Share Link
        links.add(getShareLinkPanel());
        
        links.add(getPublishLinkPanel());
        
        links.add(getProjectConfigLinkPanel());

        // Feedback link
        links.add(getFeedbackHTML());

        return links;
    }

    protected HorizontalPanel getOptionsPanel() {
        MenuBar horizontalOptionsMenu = new MenuBar();
        verticalOptionsMenu = new MenuBar(true);

        horizontalOptionsMenu.addItem("" + new HTML("<a id='login' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>Options</span>" + "<span style='font-size:75%; margin-top: 3px;'>&nbsp;&#9660;</span></a>"), true, verticalOptionsMenu);
        horizontalOptionsMenu.setStyleName("menuBar");
        verticalOptionsMenu.setStyleName("subMenuBar");

        addChangePasswordMenuItem();

        addEditProfileMenuItem();

        optionsLinks = new HorizontalPanel();
        optionsLinks.add(horizontalOptionsMenu);

        optionsLinks.setVisible(false);

        return optionsLinks;
    }

    protected HTML getUserNameHtml() {
        return userNameHtml = new HTML("<span style='font-size:75%; font-weight:bold;'>" + getUserNameText() + "</span>");
    }

    protected HTML getSignInOutHtml() {
        signInOutHtml = new HTML("<a id='login' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>" + getSignInOutText() + "</span></a>");
        signInOutHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onSignInOut();
            }
        });
        return signInOutHtml;

    }

    protected HorizontalPanel getShareLinkPanel() {
        shareHtml = new HTML("<a id='share' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>" + "Share" + "</span></a>");
        shareHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                SharingSettingsDialog dlg = new SharingSettingsDialog(new ProjectId(currentSelectedProject));
                dlg.setVisible(true);
            }
        });
        shareLinkPanel = new HorizontalPanel();
        shareLinkPanel.add(shareHtml);
        shareLinkPanel.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));
        shareLinkPanel.setVisible(true);
        return shareLinkPanel;
    }

    protected HorizontalPanel getPublishLinkPanel() {
        publishHtml = new HTML("<a id='publish' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>" + "Publish" + "</span></a>");
        publishHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                handlePublish();
            }
        });
        publishLinkPanel = new HorizontalPanel();
        publishLinkPanel.add(publishHtml);
        publishLinkPanel.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));
        publishLinkPanel.setVisible(true);
        return publishLinkPanel;
    }

    protected HorizontalPanel getProjectConfigLinkPanel() {
        configureHtml = new HTML("<a id='projectconfig' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>" + "Configure" + "</span></a>");
        configureHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                ProjectConfigurationDialog dlg = new ProjectConfigurationDialog(new ProjectId(currentSelectedProject));
                dlg.setVisible(true);
            }
        });
        configureLinkPanel = new HorizontalPanel();
        configureLinkPanel.add(configureHtml);
        configureLinkPanel.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));
        configureLinkPanel.setVisible(true);
        return configureLinkPanel;
    }
    
    protected HorizontalPanel getSignupPanel() {
        signupPanel = new HorizontalPanel();
        Button signupButton = new Button(SIGN_UP_BUTTON_TEXT);
        signupButton.addStyleName("web-protege-button-red");
        signupButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                handleSignup();
            }
        });
        signupPanel.add(signupButton);
        return signupPanel;
    }

    protected HTML getFeedbackHTML() {
        HTML feedbackHtml = new HTML("<a id='feedback' href='javascript:;'><span style='font-size:75%; text-decoration:underline; padding-right:5px;'>" + "Send&nbsp;feedback</span></a>");
        feedbackHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                final Window window = new Window();
                window.setTitle("Send feedback");
                window.setClosable(true);
                window.setWidth(400);
                window.setHeight(150);
                window.setHtml(getFeebackText());
                window.setPaddings(7);
                window.setCloseAction(Window.HIDE);
                window.show("feedback");
            }
        });
        return feedbackHtml;
    }

    protected void addChangePasswordMenuItem() {
        MenuItem changePassword = new MenuItem("Change Password", new Command() {
            public void execute() {
                final LoginUtil loginUtil = new LoginUtil();
                Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
                if (isLoginWithHttps) {
                    changePasswordWithHttps(loginUtil);
                }
                else {
                    loginUtil.changePassword(GlobalSettings.getGlobalSettings().getUserName(), isLoginWithHttps);
                }
            }
        });
        verticalOptionsMenu.addItem(changePassword);
    }


    protected void addUserMenuItem() {
        addUser = new MenuItem("Add User", new Command() {
            public void execute() {

                Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
                LoginUtil loginUtil = new LoginUtil();
                if (isLoginWithHttps) {
                    createUserViaHttps(loginUtil);
                }
                else {
                    loginUtil.createNewUser(isLoginWithHttps);
                }
            }
        });
        verticalOptionsMenu.addItem(addUser);
    }

    protected void addEditProfileMenuItem() {
        MenuItem editProfile = new MenuItem("Edit Profile", new Command() {
            public void execute() {
                EditProfileUtil eProfileUtil = new EditProfileUtil();
                eProfileUtil.editProfile();
            }
        });
        verticalOptionsMenu.addItem(editProfile);
    }

    protected String getUserNameText() {
        String name = GlobalSettings.getGlobalSettings().getUserName();
        return name == null ? "You&nbsp;are&nbsp;not&nbsp;signed&nbsp;in." : name;
    }

    /*
     * Sign in and Sign out handling
     */

    protected String getSignInOutText() {
        return GlobalSettings.getGlobalSettings().isLoggedIn() ? "Sign&nbsp;Out" : "Sign&nbsp;In";
    }

    protected void onSignInOut() {
        final LoginUtil loginUtil = new LoginUtil();
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        if (userName == null) {

            Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
//            FacebookLoginUtil.loginMethod = AuthenticationConstants.LOGIN_METHOD_FACEBOOK_ACCOUNT;
            if (isLoginWithHttps) {
                String httpsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
                String authenUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_LOGIN, httpsPort);
                authenUrl = authenUrl + "&" + AuthenticationConstants.PROTOCOL + "=" + com.google.gwt.user.client.Window.Location.getProtocol();
                authenUrl = authenUrl + "&" + AuthenticationConstants.DOMAIN_NAME_AND_PORT + "=" + com.google.gwt.user.client.Window.Location.getHost();
                int randomNumber = Random.nextInt(10000);
                authenUrl = authenUrl + "&" + AuthenticationConstants.RANDOM_NUMBER + "=" + randomNumber;
                AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(new clearLoginAuthDataHandler(authenUrl, loginUtil, randomNumber));
            }
            else {
                loginUtil.login(isLoginWithHttps);
            }
        }
        else {
            loginUtil.logout();
        }
    }

    private void adjustUserNameText() {
        signInOutHtml.setHTML("<a id='login' href='javascript:;'><span style='font-size:75%; text-decoration:underline;'>" + getSignInOutText() + "</span>");
        userNameHtml.setHTML("<span style='font-size:75%; font-weight:bold;'>" + getUserNameText() + "</span>");
    }

    /*
     * Text for links
     */

    protected String getFeebackText() {
        return "<br /> Thank you for using WebProtege " + "<br /><br /> Your feedback is very important to us. " + "Please send your comments, questions, feature requests, bugs, etc. " + "to the protege-discussion mailing list.  If you are not a member of this list you may subscribe at <a href=\"https://mailman.stanford.edu/mailman/listinfo/protege-discussion\"target=\"_blank\">https://mailman.stanford.edu/mailman/listinfo/protege-discussion</a>. <br /><br />";
    }

    /**
     * @param loginUtil
     */
    private void changePasswordWithHttps(final LoginUtil loginUtil) {
        String httsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
        Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
        notifyIfPasswordChanged();
        String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_CHANGE_PASSWORD, httsPort);
        authUrl = authUrl + "&" + AuthenticationConstants.USERNAME + "=" + GlobalSettings.getGlobalSettings().getUserName();
        loginUtil.openNewWindow(authUrl, "440", "260", "0");
    }

    protected void notifyIfPasswordChanged() {
        final Integer timeout = ClientApplicationPropertiesCache.getServerPollingTimeoutMinutes();
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
                String passwordChangedCookie = Cookies.getCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
                if (passwordChangedCookie != null) {
                    timer.cancel();

                    if (passwordChangedCookie.equalsIgnoreCase(AuthenticationConstants.CHANGE_PASSWORD_SUCCESS)) {
                        MessageBox.alert("Password Changed successfully");
                    }
                    Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
                }
            }
        };
        checkSessionTimer.scheduleRepeating(2000);
    }

    class clearLoginAuthDataHandler extends AbstractAsyncHandler<Void> {

        private final String athnUrl;

        private final LoginUtil loginUtil;

        private final int randomNumber;

        public clearLoginAuthDataHandler(String athnUrl, LoginUtil loginUtil, int randomNumber) {
            this.athnUrl = athnUrl;
            this.loginUtil = loginUtil;
            this.randomNumber = randomNumber;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
        }

        @Override
        public void handleSuccess(Void result) {
            loginUtil.openNewWindow(athnUrl, "390", "325", "0");
            loginUtil.getTimeoutAndCheckUserLoggedInMethod(loginUtil, "" + randomNumber);
        }

    }

    /**
     * Opens a new window to login user Via https
     * @param loginUtil
     */
    private void createUserViaHttps(final LoginUtil loginUtil) {
        String httsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
        notifyIfPasswordChanged();
        String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_CREATE_USER, httsPort);
        loginUtil.openNewWindow(authUrl, "440", "295", "0");
    }

    public void handlePublish() {
        ProjectManagerServiceAsync service = GWT.create(ProjectManagerService.class);
        service.getProjectData(new ProjectId(currentSelectedProject), new AsyncCallback<ProjectData>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert("There was a problem getting the project data from the server.  Please try again.");
            }

            public void onSuccess(ProjectData result) {
                showBioPortalUploadDialog(result);
            }
        });
    }
    
    private void showBioPortalUploadDialog(ProjectData projectData) {
        UserData userData = GlobalSettings.getGlobalSettings().getUser();
        if(userData != null && projectData != null) {
            BioPortalUploadDialog dlg = new BioPortalUploadDialog(projectData, userData);
            dlg.show();
        }
    }
    
    

    private void handleSignup() {
        WebProtegeSignupDialog dlg = new WebProtegeSignupDialog();
        dlg.setVisible(true);
//        LoginUtil loginUtil = new LoginUtil();
//        Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
//        loginUtil.createNewUser(isLoginWithHttps);
    }

}
