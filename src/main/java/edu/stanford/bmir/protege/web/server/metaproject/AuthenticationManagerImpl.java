package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.*;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class AuthenticationManagerImpl implements AuthenticationManager {

    private MetaProject metaProject;

    @Inject
    public AuthenticationManagerImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
    }

    @Override
    public UserData registerUser(UserId userId, EmailAddress email, SaltedPasswordDigest password, Salt salt) throws UserRegistrationException {
        checkNotNull(userId);
        checkNotNull(email);
        checkNotNull(password);
        checkNotNull(salt);
        User existingUser = metaProject.getUser(userId.getUserName());
        if (existingUser != null) {
            throw new UserNameAlreadyExistsException(userId.getUserName());
        }
        for (User user : metaProject.getUsers()) {
            if (email.getEmailAddress().equals(user.getEmail())) {
                throw new UserEmailAlreadyExistsException(email.getEmailAddress());
            }
        }
        User newUser = metaProject.createUser(userId.getUserName(), "");
        newUser.setName(userId.getUserName());
        String encodedPassword = BaseEncoding.base16().lowerCase().encode(password.getBytes());
        String encodedSalt = BaseEncoding.base16().lowerCase().encode(salt.getBytes());
        newUser.setDigestedPassword(encodedPassword, encodedSalt);
        newUser.setEmail(email.getEmailAddress());
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
        UserData userData = new UserData(userId);
        userData.setEmail(email.getEmailAddress());
        return userData;
    }

    @Override
    public void changePassword(String userName, String password) {
        User user = metaProject.getUser(userName);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user name: " + userName);
        }
        user.setPassword(password);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    @Override
    public void setDigestedPassword(UserId userId, SaltedPasswordDigest saltedPasswordDigest, Salt salt) {
        if (userId.isGuest()) {
            return;
        }
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return;
        }
        String encodedDigest = BaseEncoding.base16().lowerCase().encode(saltedPasswordDigest.getBytes());
        user.setDigestedPassword(encodedDigest, BaseEncoding.base16().lowerCase().encode(salt.getBytes()));
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    @Override
    public Optional<Salt> getSalt(UserId userId) {
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return Optional.absent();
        }
        String salt = user.getSalt();
        byte[] saltBytes = BaseEncoding.base16().lowerCase().decode(salt);
        return Optional.fromNullable(new Salt(saltBytes));
    }

    @Override
    public Optional<SaltedPasswordDigest> getSaltedPasswordDigest(UserId userId) {
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return Optional.absent();
        }
        String pwd = user.getDigestedPassword();
        byte[] pwdBytes = BaseEncoding.base16().lowerCase().decode(pwd);
        return Optional.of(new SaltedPasswordDigest(pwdBytes));
    }

}
