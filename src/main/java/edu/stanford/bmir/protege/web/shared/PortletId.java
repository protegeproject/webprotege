package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletId implements IsSerializable {

    private String id;

    private PortletId() {
    }

    public PortletId(String id) {
        this.id = id;
    }

    public String getPortletId() {
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
        if (!(obj instanceof PortletId)) {
            return false;
        }
        PortletId other = (PortletId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("PortletId")
                .addValue(id)
                .toString();
    }


}
