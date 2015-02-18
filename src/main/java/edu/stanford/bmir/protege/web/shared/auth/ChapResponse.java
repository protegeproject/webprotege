package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Arrays;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChapResponse implements IsSerializable {

    private byte [] bytes;

    private ChapResponse() {
    }

    public ChapResponse(byte[] bytes) {
        this.bytes = Arrays.copyOf(checkNotNull(bytes), bytes.length);
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
        if (!(obj instanceof ChapResponse)) {
            return false;
        }
        ChapResponse other = (ChapResponse) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }


    @Override
    public String toString() {
        return toStringHelper("ChapResponse")
                .addValue(BaseEncoding.base16().lowerCase().encode(bytes))
                .toString();
    }
}
