package edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.login.HashAlgorithm;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages invitation process.
 *
 * @author z.khan
 *
 */
public class InviteUserUtil {

    protected Window invitationWindow;
    private int inviteeCount = 1;
    private FlexTable inviteeListFlexTable;
    private HTML inviteMoreHTML;
    private Button sendInvitationButton;

    private String currentSelectedProject;

    /**
     * Initializes the variables
     */
    public void init() {

        inviteeListFlexTable = new FlexTable();

        invitationWindow = new Window();

        sendInvitationButton = new Button("Invite");
    }

    /**
     * Adds listeners to widgets
     */
    public void addListeners() {
        sendInvitationButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                List<Invitation> invitationList = getInvitationList();
                if(invitationList.size()!=0){
                    createTemporaryAccountsOnServer(invitationList);
                }else {
                    MessageBox.alert("Enter correct invitees details.");
                }

            }
        });
    }

    /**
     *
     * Invokes server
     * @param invitationList
     */
    private void createTemporaryAccountsOnServer(List<Invitation> invitationList) {
        String invitationBaseURL = getInvitationBaseURL();
        invitationWindow.getEl().mask("Sending invitation...");
        AccessPolicyServiceManager.getInstance().createTemporaryAccountForInvitation(currentSelectedProject,invitationBaseURL, invitationList,
                new AsyncCallback<Void>() {

                    public void onSuccess(Void result) {
                        invitationWindow.getEl().unmask();
                        MessageBox.alert("Invitation send successfully.");
                        invitationWindow.close();
                    }

                    public void onFailure(Throwable caught) {
                        invitationWindow.getEl().unmask();
                        GWT.log("Error on creating temporary account on server", caught);
                        com.google.gwt.user.client.Window.alert("failure createTemporaryAccountsOnServer");//TODO remove this

                    }
                });
    }

    /**
     * Creates and display window to invite
     * @param currentSelectedProject
     */
    public void displayInvitationWindow(String currentSelectedProject) {
        this.currentSelectedProject = currentSelectedProject;
        init();
        addListeners();

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setHeight("100px");
        Element scrollPanelElement = scrollPanel.getElement();
        scrollPanelElement.getStyle().setBorderColor("black");
        scrollPanelElement.getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        scrollPanelElement.getStyle().setBorderWidth(new Double("2"), Style.Unit.PX);
        scrollPanelElement.getStyle().setBackgroundColor("white");

        appendInviteeForm();
        addInviteMoreLink();

        scrollPanel.add(inviteeListFlexTable);

        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setCls("loginpanel");
        panel.setLayout(new FitLayout());
        invitationWindow.setLayout(new FitLayout());

        FlexTable inviteePanelFlexTable = new FlexTable();
        inviteePanelFlexTable.setWidget(0, 0, scrollPanel);
        inviteePanelFlexTable.setWidget(1, 0, inviteMoreHTML);
        inviteePanelFlexTable.setWidget(2, 0, sendInvitationButton);
        inviteePanelFlexTable.getFlexCellFormatter().setAlignment(2, 0, HasAlignment.ALIGN_CENTER,
                HasAlignment.ALIGN_MIDDLE);

        panel.add(inviteePanelFlexTable, new AnchorLayoutData("-100 30%"));

        invitationWindow.setWidth("380px");
        invitationWindow.setHeight("250px");
        invitationWindow.add(panel);
        invitationWindow.setPaddings(10);
        invitationWindow.setTitle("Send invitation");
        invitationWindow.show();
        invitationWindow.center();

    }

    /**
     * Appends 'Add more' link to invitation window which will create fields for
     * inviting more users.
     */
    private void addInviteMoreLink() {
        String inviteMoreLinkImage = "<img src='" + GWT.getHostPageBaseURL() + "images/shareaccess/user_add.png'/>";

        inviteMoreHTML = new HTML("<br><a href='javascript:;'>" + inviteMoreLinkImage
                + "<b><span style='font-size:100%; text-decoration:underline;'>" + " Add more"
                + "</span></b></a><br><br>");

        inviteMoreHTML.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                appendInviteeForm();
            }
        });
    }

    /**
     * Creates a new form to send invitation.
     */
    private void appendInviteeForm() {
        TextBox inviteeEmailIdTextBox = new TextBox();

        RadioButton readerRButton = new RadioButton(inviteeCount + "", " Reader");
        readerRButton.setValue(true);
        RadioButton writerRButton = new RadioButton(inviteeCount + "", " Writer");

        inviteeListFlexTable.setWidget(inviteeCount - 1, 0, inviteeEmailIdTextBox);
        inviteeListFlexTable.setWidget(inviteeCount - 1, 1, readerRButton);
        inviteeListFlexTable.setWidget(inviteeCount - 1, 2, writerRButton);
        inviteeCount++;
    }

    /**
     * Returns the invitation list provided by user.
     * @return
     */
    private List<Invitation> getInvitationList() {
        List<Invitation> invitations = new ArrayList<Invitation>();
        for (int i = 0; i < inviteeListFlexTable.getRowCount(); i++) {
            Widget inviteeEmailIdTextBox = inviteeListFlexTable.getWidget(i, 0);
            if (inviteeEmailIdTextBox instanceof TextBox
                    && isValidEmail(((TextBox) inviteeEmailIdTextBox).getText().trim())) {
                try {
                    Invitation invitation = new Invitation();
                    invitation.setEmailId(((TextBox) inviteeEmailIdTextBox).getText().trim());
                    Widget readerRButton = inviteeListFlexTable.getWidget(i, 1);
                    Widget writerRButton = inviteeListFlexTable.getWidget(i, 2);
                    if (readerRButton instanceof RadioButton && writerRButton instanceof RadioButton) {
                        invitation.setWriter(((RadioButton) writerRButton).getValue());
                        invitations.add(invitation);
                    }
                } catch (Exception e) {
                    GWT.log("Exception in reading invitation list", e);
                }
            }

        }
        return invitations;
    }

    /**
     * Creates and displays invitation window when user clicks invitation URL.
     * @param invitationId
     */
    public void updateInvitationAccount(final String invitationId) {
        final Window win = new Window();
        FormPanel newUserformPanel = new FormPanel();

        Label label = new Label("Welcome! Please enter your Name and Password");
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
        newUserEmailID.setEnabled(false);
        newUserEmailID.setText(invitationId);
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
                    if (newUserEmailID.getText().trim().equals("") || newUserPassword.getText().trim().equals("")
                            || confirmPassword.getText().trim().equals("")) {
                        MessageBox.alert("User ID and Password both are required.");
                    } else {

                        updateInvitationTemporaryAccount(newUserID.getText(), newUserPassword, confirmPassword,
                                invitationId, win);

                    }
                }
            }
        });

        com.gwtext.client.widgets.Button register = new com.gwtext.client.widgets.Button("Register", new ButtonListenerAdapter() {
            @Override
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
                if (newUserEmailID.getText().trim().equals("") || newUserPassword.getText().trim().equals("")
                        || confirmPassword.getText().trim().equals("")) {
                    MessageBox.alert("User ID and Password both are required.");
                } else {

                    updateInvitationTemporaryAccount(newUserID.getText(), newUserPassword, confirmPassword,
                            invitationId, win);

                }
            }
        });

        com.gwtext.client.widgets.Button cancel = new com.gwtext.client.widgets.Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
                win.close();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(register);
        HorizontalPanel spacingPanel = new HorizontalPanel();
        spacingPanel.setWidth("20px");
        buttonPanel.add(spacingPanel);
        buttonPanel.add(cancel);

        newUserTable.setWidget(6, 1, buttonPanel);
        newUserTable.getFlexCellFormatter().setAlignment(6, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        win.setTitle("Invitation Account Registration");
        win.setClosable(true);
        win.setWidth(428);
        win.setHeight(240);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(newUserformPanel);
        win.show();
    }

    public void updateInvitationTemporaryAccount(final String userName, PasswordTextBox newUserPasswordField,
                                                 PasswordTextBox newUserPassword2Field, final String emailId, final com.gwtext.client.widgets.Window win) {
        final String newUserPassword = newUserPasswordField.getText();
        String newUserPassword2 = newUserPassword2Field.getText();

        if (newUserPassword.contentEquals(newUserPassword2)) {
            win.getEl().mask("Creating new user...", true);
            AdminServiceManager.getInstance().getNewSalt(new AsyncCallback<String>() {

                public void onSuccess(String salt) {
                    HashAlgorithm hAlgorithm = new HashAlgorithm();
                    String saltedHashedPass = hAlgorithm.md5(salt + newUserPassword);
                    AccessPolicyServiceManager.getInstance().updateInvitedTemporaryAccount(userName, saltedHashedPass, emailId,  new UpdateInvitedTemporaryAccountHandler(win));
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

    /**
     * Callback for updateInvitedTemporaryAccount method
     * @author z.khan
     *
     */
    private final class UpdateInvitedTemporaryAccountHandler implements AsyncCallback<AccessPolicyUserData> {
        private final Window win;

        private UpdateInvitedTemporaryAccountHandler(Window win) {
            this.win = win;
        }

        public void onSuccess(AccessPolicyUserData userData) {
            win.getEl().unmask();
            if (userData != null) {
                win.close();

//                GlobalSettings.getGlobalSettings().getGlobalSession().setUserName(
//                        userData.getName());
                MessageBox.alert("New user created successfully");
                throw new RuntimeException("TODO: Uncomment setUserName.");
            } else {
                MessageBox.alert("New user registration could not be completed. Please try again.");
            }
        }

        public void onFailure(Throwable caught) {
            GWT.log("Error at registering new user", caught);
            win.getEl().unmask();
            MessageBox.alert("There was an error at creating the new user. Please try again later.");
        }
    }


    /**
     * Gets the URL to be used as base for creating invitation URL
     * @return
     */
    public String getInvitationBaseURL() {
        String queryString = com.google.gwt.user.client.Window.Location.getQueryString();
        if (queryString.trim().equals("")) {
            queryString = "?";

        } else {
            queryString = queryString + "&";
        }
        String invitationBaseURL = GWT.getHostPageBaseURL() + InvitationConstants.WEBPROTEGE_MODULE_HTML_FILE;

        invitationBaseURL = invitationBaseURL + queryString;
        return invitationBaseURL;
    }

    /**
     * Checks if the temporary account exists with the provided invitation id.
     * If the temporary account exists then its checked whether the invitation
     * URL is authorised or not. If URL is authorised then account is updated by
     * the credentials provided by invitee.
     *
     * @return
     */
    public void ProcessInvitation() {

        final String invitationId = com.google.gwt.user.client.Window.Location
                .getParameter(InvitationConstants.INVITATION_URL_PARAMETER_INVITATION_ID);
        final String invitationRandomNo = com.google.gwt.user.client.Window.Location
                .getParameter(InvitationConstants.INVITATION_URL_PARAMETER_RANDOM_NO);
        AccessPolicyServiceManager.getInstance().isInvitedAccountPresent(invitationId, new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean isPresent) {
                if (isPresent) {
                    AccessPolicyServiceManager.getInstance().isAccountTemporary(invitationId, invitationRandomNo,
                            new AsyncCallback<Boolean>() {

                                public void onSuccess(Boolean isTemporary) {
                                    if (isTemporary) {
                                        checkInvitationExpiration(invitationId);
                                    } else {
                                        MessageBox
                                                .alert("Cannot create a user account for email address: "
                                                        + invitationId
                                                        + ". The account already exists. Please contact the administrator for more information.");
                                    }

                                }

                                public void onFailure(Throwable caught) {
                                    MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

                                }
                            });

                } else {
                    MessageBox
                            .alert("This email invitation seems to be invalid. Please contact the administrator for more information.");
                }

            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

            }
        });

    }

    /**
     * Checks whether the invitation has expired or not. And proceeds to create
     * the account only if the invitaion has not expired
     *
     * @param invitationId
     */
    private void checkInvitationExpiration(final String invitationId) {

        AccessPolicyServiceManager.getInstance().isInvitationValid(invitationId, new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean isInvitationValid) {
                if (isInvitationValid) {
                    InviteUserUtil iUserUtil = new InviteUserUtil();
                    iUserUtil.updateInvitationAccount(invitationId);
                } else {
                    MessageBox
                            .alert("This email invitation has expired. Please contact the administrator for more information.");
                }

            }

            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

            }
        });
    }


    /**Checks whether the provided email is valid
     * @param email
     * @return
     */
    protected native boolean isValidEmail(String email) /*-{
        var reg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
        var reg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/; // valid
        return !reg1.test(email) && reg2.test(email);
    }-*/;
}