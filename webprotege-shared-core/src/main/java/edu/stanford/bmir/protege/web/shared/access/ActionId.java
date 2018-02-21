package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Jan 2017
 */
public class ActionId implements IsSerializable, Comparator<ActionId> {

    private String id;

    @GwtSerializationConstructor
    private ActionId() {
    }

    @JsonCreator
    public ActionId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @Nonnull
    @JsonValue
    public String getId() {
        return id;
    }

    @Override
    public int compare(ActionId o1, ActionId o2) {
        return o1.id.compareTo(o2.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ActionId)) {
            return false;
        }
        ActionId other = (ActionId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return toStringHelper("ActionId")
                .addValue(id)
                .toString();
    }
}