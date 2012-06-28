package edu.stanford.bmir.protege.web.server;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.NotificationInterval;
import edu.stanford.bmir.protege.web.client.rpc.NotificationService;
import edu.stanford.bmir.protege.web.client.rpc.NotificationTimestamp;
import edu.stanford.bmir.protege.web.client.rpc.NotificationType;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;

/**
 * Server-side services for notification management.
 */
public class NotificationServiceImpl extends WebProtegeRemoteServiceServlet  implements NotificationService {

    private static final long serialVersionUID = 3242452355007002607L;

    public synchronized void setNotificationDelay(final String user, final NotificationType notificationType, final NotificationInterval notificationInterval) {
        //FIXME: everything on this path can be null
        final User userProperties = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(user);
        String currentUserPropertyValue = userProperties.getPropertyValue(notificationType.getValue());
        while (currentUserPropertyValue != null) {
            userProperties.removePropertyValue(notificationType.getValue(), currentUserPropertyValue);
            currentUserPropertyValue = userProperties.getPropertyValue(notificationType.getValue());
        }
        final NotificationTimestamp notificationTimestamp = NotificationTimestamp.fromType(notificationType);
        String currentUserTimestamp = userProperties.getPropertyValue(notificationTimestamp.getValue());
        if (currentUserTimestamp == null) {
            userProperties.addPropertyValue(notificationTimestamp.getValue(), new Long(new Date().getTime()).toString());
        }
        userProperties.addPropertyValue(notificationType.getValue(), notificationInterval.getValue());
    }

    public Map<NotificationType, NotificationInterval> getNotificationDelays(final String user) {
        //FIXME: everything on this path can be null
        final Collection<PropertyValue> valueCollection = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(user).getPropertyValues();
        final Map<NotificationType, NotificationInterval> returnValue = new HashMap<NotificationType, NotificationInterval>();
        for (PropertyValue propertyValue : valueCollection) {
            final NotificationType type = NotificationType.fromString(propertyValue.getPropertyName());
            if (type != null) {
                returnValue.put(type, NotificationInterval.fromString(propertyValue.getPropertyValue()));
            }
        }
        return returnValue;
    }


    class ChangeDataWithProject extends ChangeData {
        private static final long serialVersionUID = -4572613145929459524L;

        private final ServerProject<Project> project;

        public ChangeDataWithProject(EntityData entityData, String author, String description, Date timestamp, ServerProject<Project> project) {
            super(entityData, author, description, timestamp);
            this.project = project;
        }

        public ChangeDataWithProject(ChangeData changeData, ServerProject<Project> project) {
            this(changeData.getEntityData(), changeData.getDescription(), changeData.getDescription(), changeData.getTimestamp(), project);
        }

        public ServerProject<Project> getProject() {
            return project;
        }
    }

}