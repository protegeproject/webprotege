package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChapSessionId implements IsSerializable {

    private String id;

    private ChapSessionId() {
    }

    public ChapSessionId(String id) {
        this.id = checkNotNull(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChapSessionId)) {
            return false;
        }
        ChapSessionId other = (ChapSessionId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("ChapSessionId")
                .addValue(id)
                .toString();
    }
}
