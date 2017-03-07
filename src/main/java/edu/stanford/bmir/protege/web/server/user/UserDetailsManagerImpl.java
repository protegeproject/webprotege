package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRecordRepository repository;

    @Inject
    public UserDetailsManagerImpl(UserRecordRepository userRecordRepository) {
        this.repository = checkNotNull(userRecordRepository);
    }

    @Override
    public List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit) {
        return repository.findByUserIdContainingIgnoreCase(userName, limit);
    }

    @Override
    public Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress) {
        if(emailAddress.getEmailAddress().isEmpty()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOneByEmailAddress(emailAddress.getEmailAddress());
        if(!record.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(record.get().getUserId());
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        if(userId.isGuest()) {
            return Optional.of(UserDetails.getGuestUserDetails());
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if(!record.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(UserDetails.getUserDetails(userId, userId.getUserName(),
                                                      Optional.ofNullable(record.get().getEmailAddress())));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if(userId.isGuest()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if(record == null) {
            return Optional.empty();
        }
        String emailAddress = record.get().getEmailAddress();
        if(emailAddress.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(emailAddress);
    }

    @Override
    public void setEmail(UserId userId, String email) {
        checkNotNull(userId);
        checkNotNull(email);
        if(userId.isGuest()) {
            return;
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if(!record.isPresent()) {
            return;
        }
        Optional<UserRecord> recordByEmail = repository.findOneByEmailAddress(email);
        if(!record.equals(recordByEmail)) {
            // TODO: Log failure
            return;
        }
        UserRecord theRecord = record.get();
        UserRecord replacement = new UserRecord(
                theRecord.getUserId(),
                theRecord.getRealName(),
                theRecord.getEmailAddress(),
                theRecord.getAvatarUrl(),
                theRecord.getSalt(),
                theRecord.getSaltedPasswordDigest()
        );
        repository.delete(userId);
        repository.save(replacement);
    }

    @Override
    public Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail) {
        Optional<UserRecord> byUserId = repository.findOne(UserId.getUserId(userNameOrEmail));
        if(byUserId.isPresent()) {
            return Optional.of(byUserId.get().getUserId());
        }
        Optional<UserRecord> byEmail = repository.findOneByEmailAddress(userNameOrEmail);
        if(byEmail.isPresent()) {
            return Optional.of(byEmail.get().getUserId());
        }
        return Optional.empty();
    }
}
