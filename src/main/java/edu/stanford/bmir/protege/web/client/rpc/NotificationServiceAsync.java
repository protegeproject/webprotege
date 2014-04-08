package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public interface NotificationServiceAsync {
    void setNotificationDelay(String user, NotificationType notificationType, NotificationInterval notificationInterval, AsyncCallback<Void> async);

    void getNotificationDelays(String user, AsyncCallback<Map<NotificationType, NotificationInterval>> async);
}
