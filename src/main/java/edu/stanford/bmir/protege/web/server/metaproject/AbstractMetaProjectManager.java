package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.user.UnrecognizedUserNameException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractMetaProjectManager extends MetaProjectManager {

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
            throw new UnrecognizedUserNameException(userName);
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
        OWLAPIMetaProjectStore.getStore().saveMetaProject(this);
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

    public Optional<UserId> getUserAssociatedWithOpenId(String userOpenId) {
        if (userOpenId == null) {
            return Optional.absent();
        }
        Set<User> users = MetaProjectManager.getManager().getMetaProject().getUsers();
        for (User u : users) {
            Collection<PropertyValue> propertyValues = u.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues) {
                if (propertyValue.getPropertyName().startsWith(OpenIdConstants.OPENID_PROPERTY_PREFIX) && propertyValue.getPropertyName().endsWith(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX)) {
                    if (propertyValue.getPropertyValue().trim().equalsIgnoreCase(userOpenId)) {
                        return Optional.of(UserId.getUserId(u.getName()));
                    }

                }
            }
        }
        return Optional.absent();
    }
    
    public User getUser(String userNameOrEmail) {
        if (userNameOrEmail == null) {
            return null;
        }

        //try to get it by name first
        User user = getMetaProject().getUser(userNameOrEmail);
        if (user != null) {
            return user;
        }

        //get user by email
        Set<User> users = getMetaProject().getUsers();
        Iterator<User> it = users.iterator();

        while (it.hasNext() && user == null) {
            User u = it.next();
            if (userNameOrEmail.equals(u.getEmail())) {
                user = u;
            }
        }

        return user;
    }

}
