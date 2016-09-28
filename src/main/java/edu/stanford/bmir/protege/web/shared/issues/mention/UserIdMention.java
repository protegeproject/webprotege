package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
@TypeAlias("UserIdMention")
public class UserIdMention implements Mention {

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


    @Override
    public String toString() {
        return toStringHelper("UserIdMention")
                .addValue(userId)
                .toString();
    }
}
