package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.Operation;
import edu.stanford.smi.protege.server.metaproject.Policy;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.ServerInstance;
import edu.stanford.smi.protege.server.metaproject.User;

public abstract class AbstractMetaProjectManager implements MetaProjectManager {

    public boolean hasValidCredentials(String userName, String password) {
        if (getMetaProject() == null) {
            return false;
        }
        User user = getMetaProject().getUser(userName);
        if (user == null) {
            return false;
        }
        return user.verifyPassword(password);
    }

    public void changePassword(String userName, String password) {
        final MetaProject metaProject = getMetaProject();
        if (metaProject == null) {
            throw new IllegalStateException("Metaproject is set to null");
        }
        User user = metaProject.getUser(userName);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user name: " + userName);
        }
        user.setPassword(password);
    }

    public String getUserEmail(String userName) {
        final MetaProject metaProject = getMetaProject();
        if (metaProject == null) {
            throw new IllegalStateException("Metaproject is set to null");
        }
        User user = metaProject.getUser(userName);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user name: " + userName);
        }
        return user.getEmail();
    }

    public void setUserEmail(String userName, String email) {
        final MetaProject metaProject = getMetaProject();
        if (metaProject == null) {
            throw new IllegalStateException("Metaproject is set to null");
        }
        User user = metaProject.getUser(userName);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user name: " + userName);
        }
        user.setEmail(email);
    }

    public Collection<Operation> getAllowedOperations(String projectName, String userName) {
        Collection<Operation> allowedOps = new ArrayList<Operation>();
        final MetaProject metaProject = getMetaProject();
        if (metaProject == null) {
            throw new IllegalStateException("Metaproject is set to null");
        }
        Policy policy = metaProject.getPolicy();
        User user = policy.getUserByName(userName);
        ProjectInstance project = getMetaProject().getPolicy().getProjectInstanceByName(projectName);
        if (user == null || project == null) {
            return allowedOps;
        }
        for (Operation op : policy.getKnownOperations()) {
            if (policy.isOperationAuthorized(user, op, project)) {
                allowedOps.add(op);
            }
        }
        return allowedOps;
    }

    public Collection<Operation> getAllowedServerOperations(String userName) {
        Collection<Operation> allowedOps = new ArrayList<Operation>();
        if (userName == null) {
            return allowedOps;
        }
        final MetaProject metaProject = getMetaProject();
        if (metaProject == null) {
            throw new IllegalStateException("Metaproject is set to null");
        }
        Policy policy = metaProject.getPolicy();
        User user = policy.getUserByName(userName);
        ServerInstance firstServerInstance = metaProject.getPolicy().getFirstServerInstance();
        if (user == null || firstServerInstance == null) {
            return allowedOps;
        }
        for (Operation op : policy.getKnownOperations()) {
            if (policy.isOperationAuthorized(user, op, firstServerInstance)) {
                allowedOps.add(op);
            }
        }
        return allowedOps;
    }

    public UserData getUserAssociatedWithOpenId(String userOpenId) {
        UserData userData = null;

        if (userOpenId == null) {
            return null;
        }

        Set<User> users = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject()
                .getUsers();

        boolean gotUser = false;
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            if (gotUser) {
                break;
            }

            User user = iterator.next();

            Collection<PropertyValue> propColl = user.getPropertyValues();

            for (Iterator<PropertyValue> iterator2 = propColl.iterator(); iterator2.hasNext();) {
                PropertyValue propertyValue = iterator2.next();
                if (propertyValue.getPropertyName().startsWith(OpenIdConstants.OPENID_PROPERTY_PREFIX)
                        && propertyValue.getPropertyName().endsWith(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX)) {

                    if (propertyValue.getPropertyValue().trim().equalsIgnoreCase(userOpenId)) {
                        userData = AuthenticationUtil.createUserData(user.getName());
                        gotUser = true;
                        break;
                    }

                }
            }
        }

        return userData;
    }

}
