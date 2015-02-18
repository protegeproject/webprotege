package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Inject;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChapData implements IsSerializable {

    private ChapSessionId id;

    private ChallengeMessage challengeMessage;

    private Salt salt;

    /**
     * For serialization only
     */
    private ChapData() {
    }

    @Inject
    public ChapData(ChapSessionId id, ChallengeMessage challengeMessage, @Assisted Salt salt) {
        this.id = checkNotNull(id);
        this.challengeMessage = checkNotNull(challengeMessage);
        this.salt = checkNotNull(salt);
    }

    public ChapSessionId getId() {
        return id;
    }

    public ChallengeMessage getChallengeMessage() {
        return challengeMessage;
    }

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
        if (!(obj instanceof ChapData)) {
            return false;
        }
        ChapData other = (ChapData) obj;
        return this.id.equals(other.id)
                && challengeMessage.equals(other.challengeMessage)
                && this.salt.equals(other.salt);
    }


    @Override
    public String toString() {
        return toStringHelper("ChallengeMessage")
                .addValue(id)
                .addValue(challengeMessage)
                .addValue(salt)
                .toString();
    }
}
