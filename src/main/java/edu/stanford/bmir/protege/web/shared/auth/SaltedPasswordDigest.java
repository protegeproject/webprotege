package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 *
 * Represents the MD5 digest of a salted password.
 */
public class SaltedPasswordDigest implements IsSerializable {

    private byte [] digest;

    /**
     * For serialization purposes only
     */
    private SaltedPasswordDigest() {
    }

    public SaltedPasswordDigest(byte[] digest) {
        this.digest = checkNotNull(digest);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(digest, digest.length);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digest);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaltedPasswordDigest)) {
            return false;
        }
        SaltedPasswordDigest other = (SaltedPasswordDigest) obj;
        return Arrays.equals(this.digest, other.digest);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("SaltedPasswordDigest")
                .addValue(BaseEncoding.base16().lowerCase().encode(digest))
                .toString();
    }
}
