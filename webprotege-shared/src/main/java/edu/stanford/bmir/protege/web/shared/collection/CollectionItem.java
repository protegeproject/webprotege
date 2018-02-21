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
public class CollectionItem implements IsSerializable {

    private String name;

    private CollectionItem(@Nonnull String name) {
        this.name = checkNotNull(name);
    }

    @GwtSerializationConstructor
    private CollectionItem() {
    }

    @Nonnull
    public static CollectionItem get(@Nonnull String itemName) {
        return new CollectionItem(itemName);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollectionItem)) {
            return false;
        }
        CollectionItem other = (CollectionItem) obj;
        return this.name.equals(other.name);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionItem")
                .addValue(name)
                .toString();
    }
}
