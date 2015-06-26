package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
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

    private final MetaProject metaProject;

    private final MetaProjectStore metaProjectStore;

    @Inject
    public UserDetailsManagerImpl(MetaProject metaProject, MetaProjectStore metaProjectStore) {
        this.metaProject = checkNotNull(metaProject);
        this.metaProjectStore = checkNotNull(metaProjectStore);
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
    public Optional<User> getUserByUserIdOrEmail(String userNameOrEmail) {
        // Here for silly legacy reasons
        if (userNameOrEmail == null) {
            return Optional.absent();
        }

        // By user Id first
        final User userById = getMetaProject().getUser(userNameOrEmail);
        if (userById != null) {
            return Optional.of(userById);
        }

        // Not found.  There's no index to email so we have to search through the lot of them.
        for(User user : getMetaProject().getUsers()) {
            if(userNameOrEmail.equals(user.getEmail())) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        if(userId.isGuest()) {
            return Optional.of(UserDetails.getGuestUserDetails());
        }
        final MetaProject metaProject = getMetaProject();
        User user = metaProject.getUser(userId.getUserName());
        if(user == null) {
            return Optional.absent();
        }
        return Optional.of(UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(user.getEmail())));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if(userId.isGuest()) {
            return Optional.absent();
        }
        Optional<User> user = getUserByUserIdOrEmail(userId.getUserName());
        if(user.isPresent()) {
            return Optional.fromNullable(user.get().getEmail());
        }
        return Optional.absent();
    }

    @Override
    public void setEmail(UserId userId, String email) {
        checkNotNull(userId);
        checkNotNull(email);
        if(userId.isGuest()) {
            return;
        }
        Optional<User> user = getUserByUserIdOrEmail(userId.getUserName());
        if(user.isPresent()) {
            user.get().setEmail(email);
        }
        metaProjectStore.saveMetaProject(metaProject);
    }
}
