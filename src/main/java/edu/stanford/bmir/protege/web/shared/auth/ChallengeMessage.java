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
public class ChallengeMessage implements IsSerializable {

    private ChallengeMessageId id;

    private byte [] challenge;

    private byte[] salt;

    /**
     * For serialization only
     */
    private ChallengeMessage() {
    }

    public ChallengeMessage(ChallengeMessageId id, byte[] challenge, byte [] salt) {
        this.id = checkNotNull(id);
        this.challenge = Arrays.copyOf(checkNotNull(challenge), challenge.length);
        this.salt = Arrays.copyOf(checkNotNull(salt), salt.length);
    }

    public ChallengeMessageId getId() {
        return id;
    }

    public byte[] getChallenge() {
        return Arrays.copyOf(challenge, challenge.length);
    }

    public byte[] getSalt() {
        return Arrays.copyOf(salt, salt.length);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, Arrays.hashCode(challenge), Arrays.hashCode(salt));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChallengeMessage)) {
            return false;
        }
        ChallengeMessage other = (ChallengeMessage) obj;
        return this.id.equals(other.id)
                && Arrays.equals(this.challenge, other.challenge)
                && Arrays.equals(this.salt, other.salt);
    }


    @Override
    public String toString() {
        return toStringHelper("ChallengeMessage")
                .add("id", id)
                .add("challenge", Arrays.toString(challenge))
                .add("salt", salt)
                .toString();
    }
}
