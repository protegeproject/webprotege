package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.util.Map;

/**
 */
public class NotificationServiceManager {

    private static NotificationServiceAsync proxy;
    static NotificationServiceManager instance;

    private NotificationServiceManager() {
        proxy = (NotificationServiceAsync) GWT.create(NotificationService.class);
    }

    public static NotificationServiceManager getInstance() {
        if (instance == null) {
            instance = new NotificationServiceManager();
        }
        return instance;
    }

    public void setNotificationDelay(UserId user, NotificationType notificationType, NotificationInterval notificationInterval, AsyncCallback<Void> async){
        proxy.setNotificationDelay(user.getUserName(), notificationType, notificationInterval, async);
    }

    public void getNotificationDelay(UserId user, AsyncCallback<Map<NotificationType, NotificationInterval>> async){
        proxy.getNotificationDelays(user.getUserName(), async);
    }
}