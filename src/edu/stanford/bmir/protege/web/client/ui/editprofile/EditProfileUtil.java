/**
 * 
 */
package edu.stanford.bmir.protege.web.client.ui.editprofile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Map;

/**
 * @author z.khan
 * 
 */
public class EditProfileUtil {

    /**
     * Creates Edit profile Popup
     */
    public void editProfile() {
        final Window win = new Window();
        FormPanel editProfileFormPanel = new FormPanel();

        Label label = new Label("Welcome. Please edit your profile information.");
        label.setStyleName("login-welcome-msg");

        FlexTable editProfileTable = new FlexTable();
        editProfileTable.setWidget(0, 0, label);
        editProfileTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        editProfileTable.getFlexCellFormatter().setHeight(1, 0, "15px");
        editProfileTable.getFlexCellFormatter().setHeight(2, 0, "25px");
        editProfileTable.getFlexCellFormatter().setHeight(3, 0, "30px");
        editProfileTable.getFlexCellFormatter().setHeight(4, 0, "25px");
        editProfileTable.getFlexCellFormatter().setHeight(5, 0, "25px");
        editProfileTable.getFlexCellFormatter().setHeight(6, 0, "25px");
        editProfileTable.getFlexCellFormatter().setHeight(7, 0, "50px");

        editProfileFormPanel.add(editProfileTable);

        final UserId userId = Application.get().getUserId();
        final TextBox userNameTextBox = new TextBox();
        userNameTextBox.setWidth("250px");
        userNameTextBox.setEnabled(false);
        Label userNameLabel = new Label("User name:");
        userNameLabel.setStyleName("label");

        editProfileTable.setWidget(2, 0, userNameLabel);
        editProfileTable.setWidget(2, 1, userNameTextBox);

        if (!userId.isGuest()) {
            userNameTextBox.setText(userId.getUserName());
        }

        final HTML changePasswordHTML = new HTML(
                "&nbsp<b><span style='font-size:100%;text-decoration:underline;'>Click here to change your password</span></b>");
        changePasswordHTML.setStyleName("links-blue");
        //if else https
//        addChangePasswordHTMLClickHandler(changePasswordHTML);

        editProfileTable.setWidget(3, 1, changePasswordHTML);

        final TextBox userEmailTextBox = new TextBox();
        userEmailTextBox.setWidth("250px");
        Label emailIdLabel = new Label("Email:");
        emailIdLabel.setStyleName("label");
        editProfileTable.setWidget(4, 0, emailIdLabel);
        editProfileTable.setWidget(4, 1, userEmailTextBox);


        Label ontologyNotificationIdLabel = new Label("Notify me of ont. changes:");
        ontologyNotificationIdLabel.setStyleName("label");
        final ListBox ontologyNotificationListBox = new ListBox(false);
        ontologyNotificationListBox.setWidth("250px");
        ontologyNotificationListBox.addItem(NotificationInterval.NEVER.getValue());
        ontologyNotificationListBox.addItem(NotificationInterval.IMMEDIATELY.getValue());
        ontologyNotificationListBox.addItem(NotificationInterval.HOURLY.getValue());
        ontologyNotificationListBox.addItem(NotificationInterval.DAILY.getValue());
        ontologyNotificationListBox.setSelectedIndex(1);
        editProfileTable.setWidget(5, 0, ontologyNotificationIdLabel);
        editProfileTable.setWidget(5, 1, ontologyNotificationListBox);

        Label commentsNotificationIdLabel = new Label("Notify me of discussions:");
        commentsNotificationIdLabel.setStyleName("label");
        final ListBox commentsNotificationListBox = new ListBox(false);
        commentsNotificationListBox .setWidth("250px");
        commentsNotificationListBox.addItem(NotificationInterval.NEVER.getValue());
        commentsNotificationListBox.addItem(NotificationInterval.IMMEDIATELY.getValue());
        commentsNotificationListBox.addItem(NotificationInterval.HOURLY.getValue());
        commentsNotificationListBox.addItem(NotificationInterval.DAILY.getValue());
        commentsNotificationListBox.setSelectedIndex(1);
        editProfileTable.setWidget(6, 0, commentsNotificationIdLabel );
        editProfileTable.setWidget(6, 1, commentsNotificationListBox );

        Button okButton = new Button("Ok", new OkButtonListenerAdapter(win, userEmailTextBox, userNameTextBox, commentsNotificationListBox, ontologyNotificationListBox));

        Button cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                win.close();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(20);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        editProfileTable.setWidget(7, 1, buttonPanel);
        editProfileTable.getFlexCellFormatter()
                .setAlignment(7, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

        FlexTable topEditProfileTable = new FlexTable();
        topEditProfileTable.setWidget(0, 0, editProfileFormPanel);
        topEditProfileTable.getFlexCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_CENTER,
                HasAlignment.ALIGN_MIDDLE);
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setPaddings(15);
        panel.setCls("loginpanel");
        panel.setLayout(new FitLayout());
        win.setLayout(new FitLayout());

        panel.add(topEditProfileTable, new AnchorLayoutData("-100 30%"));

        win.setTitle("Edit Profile");
        win.setClosable(true);
        win.setWidth(408);
        win.setHeight(400);
        win.setClosable(true);
        win.setPaddings(7);
        win.setCloseAction(Window.HIDE);
        win.add(panel);
        if (!userId.isGuest()) {
            win.show();

            win.getEl().mask("Retrieving user email...");
            AdminServiceManager.getInstance().getUserEmail(userId, new RetrieveUserEmailHandler(win, userEmailTextBox));

            NotificationServiceManager.getInstance().getNotificationDelay(userId, new AsyncCallback<Map<NotificationType, NotificationInterval>>() {

                public void onSuccess(Map<NotificationType, NotificationInterval> notificationPreferences) {
                    win.getEl().unmask();
                    for (NotificationType type : notificationPreferences.keySet()) {
                        if (type.equals(NotificationType.ONTOLOGY)){
                            int i = 0;
                            while (i < ontologyNotificationListBox.getItemCount()){
                                final String s = ontologyNotificationListBox.getItemText(i);
                                if (notificationPreferences.get(type).getValue().equals(s)){
                                    ontologyNotificationListBox.setItemSelected(i, true);
                                }
                                i ++;
                            }
                        }
                        if (type.equals(NotificationType.COMMENT)){
                            int i = 0;
                            while (i < commentsNotificationListBox.getItemCount()){
                                final String s = commentsNotificationListBox.getItemText(i);
                                if (notificationPreferences.get(type).getValue().equals(s)){
                                    commentsNotificationListBox.setItemSelected(i, true);
                                }
                                i ++;
                            }
                        }
                    }
                }

                public void onFailure(Throwable caught) {
                    GWT.log("Error at Getting User Notification Preferences:", caught);
                    win.getEl().unmask();
                    MessageBox.alert("failed. Please try again. Message: " + caught.getMessage());
                    win.close();
                }
            });

//            final FlexTable editProfTable = editProfileTable;
            OpenIdServiceManager.getInstance().getUsersOpenId(userId.getUserName(), new GetUsersOpenIdHandler(win, editProfileTable));
        } else {
            MessageBox.alert("Error at Getting User Name, Please try again");
        }
    }

//    /**
//     * @param changePasswordHTML
//     */
//    protected void addChangePasswordHTMLClickHandler(final HTML changePasswordHTML) {
//        Boolean isLoginWithHttps = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_ENABLED, false);
//        if (isLoginWithHttps) {
//            changePasswordHTML.addClickHandler(changePasswordWithHTTPSClickHandler);
//        } else {
//            addChangePasswordWithEncryptionHandler(changePasswordHTML, isLoginWithHttps);
//        }
//    }


    /**
     * CallBack for Edit Profile process.
     *
     */
    static class EditProfileHandler extends AbstractAsyncHandler<Void> {
        private Window win;
        private int completions;

        public EditProfileHandler(Window win) {
            this.win = win;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at Editing Profile Info", caught);
            win.getEl().unmask();
            MessageBox.alert("Error",
            "There was an error at changing the user profile.<br />Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            synchronized (this){
            completions ++;
            if (completions > 2){
            win.getEl().unmask();
            win.close();
            completions = 0;
            }
            }
        }
    }

    protected native boolean isValidEmail(String email) /*-{
        var reg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
        var reg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/; // valid
        return !reg1.test(email) && reg2.test(email);
    }-*/;

    class RetrieveUserEmailHandler extends AbstractAsyncHandler<String> {
        private Window win;
        private TextBox userEmailTextBox;

        public RetrieveUserEmailHandler(Window win, TextBox userEmailTextBox) {
            this.win = win;
            this.userEmailTextBox = userEmailTextBox;
        }

        @Override
        public void handleSuccess(String emailId) {
            win.getEl().unmask();
            if (emailId != null) {
                userEmailTextBox.setText(emailId);
            }
        }

        @Override
        public void handleFailure(Throwable caught) {
            win.getEl().unmask();
            GWT.log("Error at getting user email:", caught);
            win.close();
        }
    }

    class GetUsersOpenIdHandler extends AbstractAsyncHandler<OpenIdData> {
        private Window win;
        private FlexTable editProfTable;

        public GetUsersOpenIdHandler(Window win, FlexTable editProfTable) {
            this.win = win;
            this.editProfTable = editProfTable;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert("Error in retrieving OpenId list");

        }

        @Override
        public void handleSuccess(OpenIdData openIdData) {
            OpenIdUtil opIdUtil = new OpenIdUtil();
            opIdUtil.displayUsersOpenIdList(openIdData, editProfTable, win, false, win.getHeight());

        }

    }

    class OkButtonListenerAdapter extends ButtonListenerAdapter {
        private Window win;
        private TextBox userEmailTextBox;
        private TextBox userNameTextBox;
        private final ListBox commentsNotification;
        private final ListBox ontologyNotification;

        public OkButtonListenerAdapter(Window win, TextBox userEmailTextBox, TextBox userNameTextBox, ListBox commentsNotification, ListBox ontologyNotification) {
            this.win = win;
            this.userEmailTextBox = userEmailTextBox;
            this.userNameTextBox = userNameTextBox;
            this.commentsNotification = commentsNotification;
            this.ontologyNotification = ontologyNotification;
        }

        @Override
        public void onClick(Button button, EventObject e) {
            boolean isEmailValid = false;
            isEmailValid = isValidEmail(userEmailTextBox.getText().trim());
            if (userEmailTextBox.getText().trim().isEmpty() || isEmailValid) {
                win.getEl().mask("Saving email ...");
                final EditProfileHandler callback = new EditProfileHandler(win);
                final String userName = userNameTextBox.getText().trim();
                final UserId userId = UserId.getUserId(userName);
                DispatchServiceManager.get().execute(new SetEmailAddressAction(userId, userEmailTextBox.getText().trim()), new EmptySuccessWebProtegeCallback<SetEmailAddressResult>());
                NotificationServiceManager.getInstance().setNotificationDelay(userId,
                            NotificationType.COMMENT, NotificationInterval.fromString(commentsNotification.getItemText(commentsNotification.getSelectedIndex())), callback);
                    NotificationServiceManager.getInstance().setNotificationDelay(userId,
                            NotificationType.ONTOLOGY, NotificationInterval.fromString(ontologyNotification.getItemText(ontologyNotification.getSelectedIndex())), callback);
            } else {
                MessageBox.alert("Email is invalid. Please enter correct email");
            }
        }
    }

//    ClickHandler changePasswordWithHTTPSClickHandler = new ClickHandler() {
//
//        public void onClick(ClickEvent event) {
//            final LoginUtil loginUtil = new LoginUtil();
//            String httpsPort = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_PORT).orNull();
//            Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//            notifyIfPasswordChanged();
//            String authUrl = loginUtil.getAuthenticateWindowUrl(
//                    AuthenticationConstants.AUTHEN_TYPE_CHANGE_PASSWORD, httpsPort);
//            authUrl = authUrl + "&" + AuthenticationConstants.USERNAME + "="
//                    + Application.get().getUserId().getUserName();
//            loginUtil.openNewWindow(authUrl, "440", "260", "0");
//        }
//    };

//    protected void addChangePasswordWithEncryptionHandler(HTML changePasswordHTML, final boolean isLoginWithHttps) {
//        changePasswordHTML.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                LoginUtil loginUtil = new LoginUtil();
//                loginUtil.changePassword(Application.get().getUserId(), isLoginWithHttps);
//
//            }
//        });
//    }

//    protected void notifyIfPasswordChanged() {
//        final Integer timeout = 5;//
//        final long initTime = System.currentTimeMillis();
//        final Timer checkSessionTimer = new Timer() {
//            @Override
//            public void run() {
//                final Timer timer = this;
//                long curTime = System.currentTimeMillis();
//                long maxTime = 1000 * 60 * timeout;
//                if (curTime - initTime > maxTime) {
//                    timer.cancel();
//                }
//                String passwordChangedCookie = Cookies
//                        .getCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//                if (passwordChangedCookie != null) {
//                    timer.cancel();
//
//                    if (passwordChangedCookie.equalsIgnoreCase(AuthenticationConstants.CHANGE_PASSWORD_SUCCESS)) {
//                        MessageBox.alert("Password changed successfully");
//                    }
//                    Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//                }
//            }
//        };
//        checkSessionTimer.scheduleRepeating(2000);
//    }

}
