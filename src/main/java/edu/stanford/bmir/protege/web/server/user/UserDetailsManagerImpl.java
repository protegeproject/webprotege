package edu.stanford.bmir.protege.web.server.user;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRecordRepository repository;

    @Inject
    public UserDetailsManagerImpl(UserRecordRepository userRecordRepository) {
        this.repository = userRecordRepository;
    }

    @Override
    public Collection<UserId> getUserIds() {
        return repository.findAll()
                .map(UserRecord::getUserId)
                .collect(toList());
    }

    @Override
    public Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress) {
        if(emailAddress.getEmailAddress().isEmpty()) {
            return Optional.absent();
        }
        java.util.Optional<UserRecord> record = repository.findOneByEmailAddress(emailAddress.getEmailAddress());
        if(!record.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(record.get().getUserId());
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        if(userId.isGuest()) {
            return Optional.of(UserDetails.getGuestUserDetails());
        }
        java.util.Optional<UserRecord> record = repository.findOne(userId);
        if(!record.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(record.get().getEmailAddress())));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if(userId.isGuest()) {
            return Optional.absent();
        }
        java.util.Optional<UserRecord> record = repository.findOne(userId);
        if(record == null) {
            return Optional.absent();
        }
        String emailAddress = record.get().getEmailAddress();
        if(emailAddress.isEmpty()) {
            return Optional.absent();
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
        java.util.Optional<UserRecord> record = repository.findOne(userId);
        if(!record.isPresent()) {
            return;
        }
        // TODO: Check that the email isn't used already
        UserRecord theRecord = record.get();
        UserRecord replacement = new UserRecord(theRecord.getUserId(), theRecord.getRealName(), theRecord.getEmailAddress(), theRecord.getAvatarUrl(), theRecord.getSalt(), theRecord.getSaltedPasswordDigest());
        repository.delete(theRecord);
        repository.save(replacement);
    }

    @Override
    public Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail) {
        java.util.Optional<UserRecord> byUserId = repository.findOne(UserId.getUserId(userNameOrEmail));
        if(byUserId.isPresent()) {
            return Optional.of(byUserId.get().getUserId());
        }
        java.util.Optional<UserRecord> byEmail = repository.findOneByEmailAddress(userNameOrEmail);
        if(byEmail.isPresent()) {
            return Optional.of(byEmail.get().getUserId());
        }
        return Optional.absent();
    }
}
