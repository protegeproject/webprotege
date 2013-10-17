package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.NotificationInterval;
import edu.stanford.bmir.protege.web.client.rpc.NotificationService;
import edu.stanford.bmir.protege.web.client.rpc.NotificationTimestamp;
import edu.stanford.bmir.protege.web.client.rpc.NotificationType;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Server-side services for notification management.
 */
public class NotificationServiceImpl extends WebProtegeRemoteServiceServlet  implements NotificationService {

    private static final long serialVersionUID = 3242452355007002607L;

    public synchronized void setNotificationDelay(final String user, final NotificationType notificationType, final NotificationInterval notificationInterval) {
        //FIXME: everything on this path can be null
        final User userProperties = MetaProjectManager.getManager().getMetaProject().getUser(user);
        String currentUserPropertyValue = userProperties.getPropertyValue(notificationType.getValue());
        while (currentUserPropertyValue != null) {
            userProperties.removePropertyValue(notificationType.getValue(), currentUserPropertyValue);
            currentUserPropertyValue = userProperties.getPropertyValue(notificationType.getValue());
        }
        final NotificationTimestamp notificationTimestamp = NotificationTimestamp.fromType(notificationType);
        String currentUserTimestamp = userProperties.getPropertyValue(notificationTimestamp.getValue());
        if (currentUserTimestamp == null) {
            userProperties.addPropertyValue(notificationTimestamp.getValue(), Long.toString(new Date().getTime()));
        }
        userProperties.addPropertyValue(notificationType.getValue(), notificationInterval.getValue());
    }

    public Map<NotificationType, NotificationInterval> getNotificationDelays(final String user) {
        //FIXME: everything on this path can be null
        final Collection<PropertyValue> valueCollection = MetaProjectManager.getManager().getMetaProject().getUser(user).getPropertyValues();
        final Map<NotificationType, NotificationInterval> returnValue = new HashMap<NotificationType, NotificationInterval>();
        for (PropertyValue propertyValue : valueCollection) {
            final NotificationType type = NotificationType.fromString(propertyValue.getPropertyName());
            if (type != null) {
                returnValue.put(type, NotificationInterval.fromString(propertyValue.getPropertyValue()));
            }
        }
        return returnValue;
    }




}