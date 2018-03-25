package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;

import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2018
 */
public class TagId implements IsSerializable {

    private String id;

    private TagId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @GwtSerializationConstructor
    private TagId() {
    }

    @JsonCreator
    @Nonnull
    public static TagId getId(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(checkNotNull(id))) {
            throw new IllegalArgumentException("Malformed tag id: " + id);
        }
        return new TagId(id);
    }

    /**
     * Creates a fresh tag with a new UUID.  Note that this only works on the server.
     */
    @GwtIncompatible
    public static TagId createTagId() {
        return getId(UUID.randomUUID().toString());
    }

    @JsonValue
    @Nonnull
    public String getId() {
        return id;
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
        if (!(obj instanceof TagId)) {
            return false;
        }
        TagId other = (TagId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("TagId")
                .addValue(id)
                .toString();
    }
}
