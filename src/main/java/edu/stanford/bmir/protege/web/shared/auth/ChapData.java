package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Arrays;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChapData implements IsSerializable {

    private ChallengeMessageId id;

    private ChallengeMessage challengeMessage;

    private byte[] salt;

    /**
     * For serialization only
     */
    private ChapData() {
    }

    public ChapData(ChallengeMessageId id, ChallengeMessage challengeMessage, byte[] salt) {
        this.id = checkNotNull(id);
        this.challengeMessage = checkNotNull(challengeMessage);
        this.salt = Arrays.copyOf(checkNotNull(salt), salt.length);
    }

    public ChallengeMessageId getId() {
        return id;
    }

    public ChallengeMessage getChallengeMessage() {
        return challengeMessage;
    }

    public byte[] getSalt() {
        return Arrays.copyOf(salt, salt.length);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, challengeMessage, Arrays.hashCode(salt));
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
                && Arrays.equals(this.salt, other.salt);
    }


    @Override
    public String toString() {
        return toStringHelper("ChallengeMessage")
                .addValue(id)
                .addValue(challengeMessage)
                .add("salt", salt)
                .toString();
    }
}
