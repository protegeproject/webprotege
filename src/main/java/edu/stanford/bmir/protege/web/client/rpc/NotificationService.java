package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;

/**
 *
 */
@RemoteServiceRelativePath("notification")
public interface NotificationService extends RemoteService {
    void setNotificationDelay(String user, NotificationType notificationType, NotificationInterval notificationInterval);


    Map<NotificationType, NotificationInterval> getNotificationDelays(String user);
}
