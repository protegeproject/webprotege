package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserRecordRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagerImpl.class);

    @Inject
    public AuthenticationManagerImpl(UserRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails registerUser(UserId userId, EmailAddress email, SaltedPasswordDigest password, Salt salt) throws UserRegistrationException {
        checkNotNull(userId);
        checkNotNull(email);
        checkNotNull(password);
        checkNotNull(salt);
        Optional<UserRecord> existingRecord = repository.findOne(userId);
        if(existingRecord.isPresent()) {
            throw new UserNameAlreadyExistsException(userId.getUserName());
        }
        Optional<UserRecord> existingRecordByEmail = repository.findOneByEmailAddress(email.getEmailAddress());
        if(existingRecordByEmail.isPresent()) {
            throw new UserEmailAlreadyExistsException(email.getEmailAddress());
        }
        logger.info("Created new user account for {} with email address {}", userId, email.getEmailAddress());
        UserRecord newUserRecord = new UserRecord(
                userId,
                userId.getUserName(),
                email.getEmailAddress(),
                "",
                salt,
                password
        );
        repository.save(newUserRecord);
        return UserDetails.getUserDetails(userId, userId.getUserName(), email.getEmailAddress());
    }

    @Override
    public void setDigestedPassword(UserId userId, SaltedPasswordDigest saltedPasswordDigest, Salt salt) {
        if (userId.isGuest()) {
            return;
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if (!record.isPresent()) {
            return;
        }
        UserRecord replacementRecord = new UserRecord(
                record.get().getUserId(),
                record.get().getRealName(),
                record.get().getEmailAddress(),
                record.get().getAvatarUrl(),
                salt,
                saltedPasswordDigest
        );
        repository.save(replacementRecord);
    }

    @Override
    public Optional<Salt> getSalt(UserId userId) {
        if(userId.isGuest()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if(!record.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(record.get().getSalt());
    }

    @Override
    public Optional<SaltedPasswordDigest> getSaltedPasswordDigest(UserId userId) {
        if(userId.isGuest()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if(record == null) {
            return Optional.empty();
        }
        return Optional.of(record.get().getSaltedPasswordDigest());
    }

}
