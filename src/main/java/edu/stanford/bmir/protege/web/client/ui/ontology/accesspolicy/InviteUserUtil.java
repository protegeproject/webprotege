package edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.login.HashAlgorithm;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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

    private ProjectId currentSelectedProject;

    /**
     * Initializes the variables
     */
    public void init() {

        inviteeListFlexTable = new FlexTable();

        invitationWindow = new Window();

        sendInvitationButton = new Button("Invite");
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
                        MessageBox.showAlert("User ID and Password both are required.");
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
                    MessageBox.showAlert("User ID and Password both are required.");
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
                    MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
                }
            });
        } else {
            MessageBox.showAlert("Passwords dont match. Please try again.");
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

//                GlobalSettings.get().getGlobalSession().setUserName(
//                        userData.getName());
                MessageBox.showAlert("New user created successfully");
                throw new RuntimeException("TODO: Uncomment setUserName.");
            } else {
                MessageBox.showAlert("New user registration could not be completed. Please try again.");
            }
        }

        public void onFailure(Throwable caught) {
            GWT.log("Error at registering new user", caught);
            win.getEl().unmask();
            MessageBox.showAlert("There was an error at creating the new user. Please try again later.");
        }
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
                                        MessageBox.showAlert("Cannot create a user account for email address "
                                                        + invitationId
                                                        ,"The account already exists. Please contact the administrator for more information.");
                                    }

                                }

                                public void onFailure(Throwable caught) {
                                    MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

                                }
                            });

                } else {
                    MessageBox.showAlert("This email invitation seems to be invalid. Please contact the administrator for more information.");
                }

            }

            public void onFailure(Throwable caught) {
                MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

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
                    MessageBox.showAlert("This email invitation has expired. Please contact the administrator for more information.");
                }

            }

            public void onFailure(Throwable caught) {
                MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

            }
        });
    }

}