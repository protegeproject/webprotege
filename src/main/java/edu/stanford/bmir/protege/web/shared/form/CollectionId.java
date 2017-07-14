package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jul 2017
 *
 * Represents an id for a WebProtege collection.  A collection id is a human readable name for a collection e.g.
 * "Boeing Aircraft" or "Amino Acids" or "747 Models".  Collections contain data about domain objects that typically
 * get transformed into OWL axioms.
 */
public class CollectionId implements IsSerializable {

    private String id;

    private CollectionId() {
    }

    private CollectionId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    public static CollectionId get(@Nonnull String id) {
        return new CollectionId(id);
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
        if (!(obj instanceof CollectionId)) {
            return false;
        }
        CollectionId other = (CollectionId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionId")
                .addValue(id)
                .toString();
    }
}
