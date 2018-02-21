package edu.stanford.bmir.protege.web.shared.access;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Jan 2017
 */
public class RoleId implements IsSerializable {

    private String id;

    @GwtSerializationConstructor
    private RoleId() {
    }

    public RoleId(String id) {
        this.id = checkNotNull(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RoleId)) {
            return false;
        }
        RoleId other = (RoleId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return toStringHelper("RoleId")
                .addValue(id)
                .toString();
    }
}