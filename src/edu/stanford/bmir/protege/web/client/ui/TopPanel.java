package edu.stanford.bmir.protege.web.client.ui;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.events.*;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedHandler;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.client.ui.editprofile.EditProfileUtil;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.sharing.SharingSettingsDialog;
import edu.stanford.bmir.protege.web.client.ui.projectconfig.ProjectConfigurationDialog;
import edu.stanford.bmir.protege.web.client.ui.res.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.client.ui.signup.WebProtegeSignupDialog;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionName;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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

    private HorizontalPanel optionsLinks;

    private HorizontalPanel shareLinkPanel;

    private HorizontalPanel publishLinkPanel;

    private HorizontalPanel configureLinkPanel;

    private HorizontalPanel signupPanel;

    private MenuBar verticalOptionsMenu;

    private final Image logoImage;

    public TopPanel() {
        setLayout(new FitLayout());
        setAutoWidth(true);
        setCls("top-panel");

        // Outer panel to house logo and inner panel
        HorizontalPanel outer = new HorizontalPanel();
        outer.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

        ImageResource logo = WebProtegeClientBundle.BUNDLE.webProtegeLogo();
        logoImage = new Image(logo);
        final Style style = logoImage.getElement().getStyle();
        style.setPaddingLeft(5, Style.Unit.PX);
        outer.add(logoImage);
        outer.setCellHorizontalAlignment(logoImage, HorizontalPanel.ALIGN_LEFT);



        // Inner panel to house links panel
        HorizontalPanel inner = new HorizontalPanel();
        inner.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

        inner.add(getLinksPanel());


        inner.add(getSignupPanel());

        outer.add(inner);
        add(outer);

        adjustOptionPanel();


        EventBusManager.getManager().registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateState();
            }
        });

        EventBusManager.getManager().registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updateState();
            }
        });

        EventBusManager.getManager().registerHandler(ActiveProjectChangedEvent.TYPE, new ActiveProjectChangedHandler() {
            @Override
            public void handleActiveProjectChanged(ActiveProjectChangedEvent event) {
                updateState();
            }
        });

        updateState();

    }

    private boolean isCurrentUserCurrentProjectOwner() {
        Optional<ProjectId> activeProject = Application.get().getActiveProject();
        if(!activeProject.isPresent()) {
            return false;
        }
        UserId userId = Application.get().getUserId();
        if(userId.isGuest()) {
            return false;
        }
        Optional<Project> opProject = ProjectManager.get().getProject(activeProject.get());
        if(!opProject.isPresent()) {
            return false;
        }
        Project project = opProject.get();
        return userId.equals(project.getProjectDetails().getOwner());
    }

    private void updateState() {

        boolean currentUserCanAdmin = isCurrentUserCurrentProjectOwner();

        adjustUserNameText();
        adjustOptionPanel();

        shareLinkPanel.setVisible(currentUserCanAdmin);
        // TODO: This is a temporary hack.  If a person can share then they can configure
        configureLinkPanel.setVisible(currentUserCanAdmin);

        logoImage.setVisible(!Application.get().getActiveProject().isPresent());

//        publishLinkPanel.setVisible(currentUserCanAdmin);
//        updateShareLink(projectChangedEvent.isShowShareLink());
    }



    /**
     * Method for to displaying the Option link. After SignIn "Options" link
     * should be visible otherwise disable
     */
    public void adjustOptionPanel() {
        UserId userId = Application.get().getUserId();
        if (!userId.isGuest()) { // login
            signupPanel.setVisible(false);
            optionsLinks.setVisible(true);
            configureLinkPanel.setVisible(true);
        }
        else { // logout
            signupPanel.setVisible(true);
            optionsLinks.setVisible(false);
            configureLinkPanel.setVisible(false);
        }
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

        // TODO: Disabled for now.  Needs more testing.
        createPublishLinkPanel();
//        links.add(createPublishLinkPanel());
        
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
                Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
                if(!activeProjectId.isPresent()) {
                    MessageBox.alert("No project is selected");
                    return;
                }
                SharingSettingsDialog dlg = new SharingSettingsDialog(activeProjectId.get());
                dlg.setVisible(true);
            }
        });
        shareLinkPanel = new HorizontalPanel();
        shareLinkPanel.add(shareHtml);
        shareLinkPanel.add(new HTML("<span style='font-size:80%;'>&nbsp;|&nbsp;</span>"));
        shareLinkPanel.setVisible(true);
        return shareLinkPanel;
    }

    protected HorizontalPanel createPublishLinkPanel() {
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
                Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
                if(!activeProjectId.isPresent()) {
                    MessageBox.alert("No project is selected");
                    return;
                }
                ProjectConfigurationDialog dlg = new ProjectConfigurationDialog(activeProjectId.get());
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
                    loginUtil.changePassword(Application.get().getUserId(), isLoginWithHttps);
                }
            }
        });
        verticalOptionsMenu.addItem(changePassword);
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
        UserId userId = Application.get().getUserId();
        if(userId.isGuest()) {
            return "You&nbsp;are&nbsp;not&nbsp;signed&nbsp;in.";
        }
        else {
            return userId.getUserName();
        }
    }

    /*
     * Sign in and Sign out handling
     */

    protected String getSignInOutText() {
        return Application.get().isGuestUser() ? "Sign&nbsp;In" : "Sign&nbsp;Out";
    }

    protected void onSignInOut() {
        final LoginUtil loginUtil = new LoginUtil();
        UserId userId = Application.get().getUserId();
        if (userId.isGuest()) {
            Boolean isLoginWithHttps = ClientApplicationPropertiesCache.getLoginWithHttps();
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
            Application.get().doLogOut();
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
        return "<br /> Thank you for using WebProtege " + "<br /><br /> Your feedback is very important to us. " + "Please send your comments, questions, feature requests, bugs, etc. " + "to the webprotege-feedback mailing list.  If you are not a member of this list you may subscribe at <a href=\"https://mailman.stanford.edu/mailman/listinfo/webprotege-feedback\"target=\"_blank\">https://mailman.stanford.edu/mailman/listinfo/protege-discussion</a>. <br /><br />";
    }

    /**
     * @param loginUtil
     */
    private void changePasswordWithHttps(final LoginUtil loginUtil) {
        String httsPort = ClientApplicationPropertiesCache.getApplicationHttpsPort();
        Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
        notifyIfPasswordChanged();
        String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_CHANGE_PASSWORD, httsPort);
        authUrl = authUrl + "&" + AuthenticationConstants.USERNAME + "=" + Application.get().getUserId().getUserName();
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
        Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
        if(!activeProjectId.isPresent()) {
            MessageBox.alert("No project is selected");
            return;
        }
//        ProjectManagerServiceAsync service = GWT.create(ProjectManagerService.class);
//        service.getProjectData(activeProjectId.get(), new AsyncCallback<ProjectData>() {
//            public void onFailure(Throwable caught) {
//                MessageBox.alert("There was a problem getting the project data from the server.  Please try again.");
//            }
//
//            public void onSuccess(ProjectData result) {
//                showBioPortalUploadDialog(result);
//            }
//        });
    }
    
//    private void showBioPortalUploadDialog(ProjectData projectData) {
////        UserData userData = Application.get().getUser();
////        if(userData != null && projectData != null) {
////            PublishToBioPortalDialog dlg = new PublishToBioPortalDialog(projectData, userData);
////            dlg.show();
////        }
//    }
    
    

    private void handleSignup() {
        WebProtegeSignupDialog dlg = new WebProtegeSignupDialog();
        dlg.setVisible(true);
    }

}
