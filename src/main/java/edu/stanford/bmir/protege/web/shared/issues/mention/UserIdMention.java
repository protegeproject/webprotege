package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
@Persistent
public class UserIdMention extends Mention {

    @Nonnull
    private UserId userId;

    @PersistenceConstructor
    public UserIdMention(@Nonnull UserId userId) {
        this.userId = userId;
    }

    @GwtSerializationConstructor
    private UserIdMention() {
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    /**
     * If this mention mentions a UserId then this method returns the UserId.
     *
     * @return The UserId.
     */
    @Nonnull
    @Override
    public Optional<UserId> getMentionedUserId() {
        return Optional.of(userId);
    }

    @Override
    public String toString() {
        return toStringHelper("UserIdMention")
                .addValue(userId)
                .toString();
    }
}
