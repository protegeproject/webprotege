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
 */
public class Salt implements IsSerializable {

    private byte [] bytes;

    private Salt() {
    }

    public Salt(byte[] bytes) {
        this.bytes = checkNotNull(bytes);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Salt)) {
            return false;
        }
        Salt other = (Salt) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("Salt")
                .addValue(BaseEncoding.base16().encode(bytes))
                .toString();
    }
}
