package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentId implements IsSerializable {

    private String id;

    private CommentId(String id) {
        this.id = checkNotNull(id);
    }

    @GwtIncompatible
    public static CommentId create() {
        //noinspection NonJREEmulationClassesInClientCode
        return new CommentId(UUID.randomUUID().toString());
    }

    @GwtSerializationConstructor
    private CommentId() {
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CommentId)) {
            return false;
        }
        CommentId other = (CommentId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("CommentId")
                .addValue(id)
                .toString();
    }
}
