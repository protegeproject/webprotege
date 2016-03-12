package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListEntry;
import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.StreamSupport;

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
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(UserRecord::getUserId)
                .collect(toList());
    }

    @Override
    public Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress) {
        if(emailAddress.getEmailAddress().isEmpty()) {
            return Optional.absent();
        }
        UserRecord record = repository.findOneByEmailAddress(emailAddress.getEmailAddress());
        if(record == null) {
            return Optional.absent();
        }
        return Optional.of(record.getUserId());
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        if(userId.isGuest()) {
            return Optional.of(UserDetails.getGuestUserDetails());
        }
        UserRecord record = repository.findOne(userId);
        if(record == null) {
            return Optional.absent();
        }
        return Optional.of(UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(record.getEmailAddress())));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if(userId.isGuest()) {
            return Optional.absent();
        }
        UserRecord record = repository.findOne(userId);
        if(record == null) {
            return Optional.absent();
        }
        String emailAddress = record.getEmailAddress();
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
        UserRecord record = repository.findOne(userId);
        if(record == null) {
            return;
        }
        // TODO: Check that the email isn't used already
        UserRecord replacement = new UserRecord(record.getUserId(), record.getRealName(), record.getEmailAddress(), record.getAvatarUrl(), record.getSalt(), record.getSaltedPasswordDigest());
        repository.delete(record);
        repository.save(replacement);
    }

    @Override
    public Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail) {
        UserRecord byUserId = repository.findOne(UserId.getUserId(userNameOrEmail));
        if(byUserId != null) {
            return Optional.of(byUserId.getUserId());
        }
        UserRecord byEmail = repository.findOneByEmailAddress(userNameOrEmail);
        if(byEmail != null) {
            return Optional.of(byEmail.getUserId());
        }
        return Optional.absent();
    }
}
