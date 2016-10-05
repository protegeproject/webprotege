package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChapSession implements IsSerializable {

    private ChapSessionId id;

    private ChallengeMessage challengeMessage;

    private Salt salt;

    @Inject
    public ChapSession(@Nonnull ChapSessionId id,
                       @Nonnull ChallengeMessage challengeMessage,
                       @Nonnull Salt salt) {
        this.id = checkNotNull(id);
        this.challengeMessage = checkNotNull(challengeMessage);
        this.salt = checkNotNull(salt);
    }

    @GwtSerializationConstructor
    private ChapSession() {
    }

    @Nonnull
    public ChapSessionId getId() {
        return id;
    }

    @Nonnull
    public ChallengeMessage getChallengeMessage() {
        return challengeMessage;
    }

    @Nonnull
    public Salt getSalt() {
        return salt;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, challengeMessage, salt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChapSession)) {
            return false;
        }
        ChapSession other = (ChapSession) obj;
        return this.id.equals(other.id)
                && challengeMessage.equals(other.challengeMessage)
                && this.salt.equals(other.salt);
    }


    @Override
    public String toString() {
        return toStringHelper("ChapSession")
                .addValue(id)
                .addValue(challengeMessage)
                .addValue(salt)
                .toString();
    }
}
