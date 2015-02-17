package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.server.AuthenticationUtil;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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
    public UserData registerUser(String userName, String email, String password) throws UserRegistrationException {
        checkNotNull(userName);
        checkNotNull(email);
        checkNotNull(password);
        User existingUser = metaProject.getUser(userName);
        if (existingUser != null) {
            throw new UserNameAlreadyExistsException(userName);
        }
        for (User user : metaProject.getUsers()) {
            if (email.equals(user.getEmail())) {
                throw new UserEmailAlreadyExistsException(email);
            }
        }
        User newUser = metaProject.createUser(userName, password);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
        return AuthenticationUtil.createUserData(UserId.getUserId(newUser.getName()));
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
    public void setDigestedPassword(UserId userId, String encryptedPassword, String salt) {
        if (userId.isGuest()) {
            return;
        }
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return;
        }
        user.setDigestedPassword(encryptedPassword, salt);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    @Override
    public Optional<Salt> getSalt(UserId userId) {
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return Optional.absent();
        }
        String salt = user.getSalt();
        byte[] saltBytes = BaseEncoding.base16().decode(salt);
        return Optional.fromNullable(new Salt(saltBytes));
    }

    @Override
    public Optional<byte[]> getDigestOfSaltedPassword(UserId userId) {
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return Optional.absent();
        }
        String pwd = user.getDigestedPassword();
        byte[] pwdBytes = BaseEncoding.base16().decode(pwd);
        return Optional.of(pwdBytes);
    }

}
