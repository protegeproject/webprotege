package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class UserDetailsManagerImpl implements UserDetailsManager {

    private MetaProject metaProject;

    @Inject
    public UserDetailsManagerImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
    }

    public MetaProject getMetaProject() {
        return metaProject;
    }

    @Override
    public Collection<UserId> getUserIds() {
        List<UserId> userIds = new ArrayList<>();
        for(User user : getMetaProject().getUsers()) {
            String userName = user.getName();
            if(userName != null) {
                userIds.add(UserId.getUserId(userName));
            }
        }
        return userIds;
    }

    @Override
    public User getUserByUserIdOrEmail(String userNameOrEmail) {
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

    @Override
    public UserDetails getUserDetails(UserId userId) {
        if(userId.isGuest()) {
            return UserDetails.getGuestUserDetails();
        }
        final MetaProject metaProject = getMetaProject();
        User user = metaProject.getUser(userId.getUserName());
        return UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(user.getEmail()));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if(userId.isGuest()) {
            return Optional.absent();
        }
        User user = getUserByUserIdOrEmail(userId.getUserName());
        if(user == null) {
            return Optional.absent();
        }
        return Optional.fromNullable(user.getEmail());
    }

    @Override
    public void setEmail(UserId userId, String email) {
        checkNotNull(userId);
        checkNotNull(email);
        if(userId.isGuest()) {
            return;
        }
        User user = getUserByUserIdOrEmail(userId.getUserName());
        if(user != null) {
            user.setEmail(email);
        }
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }
}
