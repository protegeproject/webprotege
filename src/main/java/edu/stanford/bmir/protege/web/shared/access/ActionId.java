package edu.stanford.bmir.protege.web.shared.access;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

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

    public ActionId(String id) {
        this.id = checkNotNull(id);
    }


    public String getId() {
        return id;
    }

    @Override
    public int compare(ActionId o1, ActionId o2) {
        return this.id.compareTo(o2.id);
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