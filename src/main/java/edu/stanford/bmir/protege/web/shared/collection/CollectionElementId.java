package edu.stanford.bmir.protege.web.shared.collection;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2017
 */
public class CollectionElementId implements IsSerializable {

    private String id;

    private CollectionElementId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @GwtSerializationConstructor
    private CollectionElementId() {
    }

    @Nonnull
    public static CollectionElementId get(@Nonnull String id) {
        return new CollectionElementId(id);
    }

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
        if (!(obj instanceof CollectionElementId)) {
            return false;
        }
        CollectionElementId other = (CollectionElementId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionElementId")
                .addValue(id)
                .toString();
    }
}
