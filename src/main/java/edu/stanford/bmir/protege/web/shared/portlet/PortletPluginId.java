package edu.stanford.bmir.protege.web.shared.portlet;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public class PortletPluginId {

    private String id;

    public PortletPluginId(String id) {
        this.id = checkNotNull(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return "PortletPluginId".hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PortletPluginId)) {
            return false;
        }
        PortletPluginId other = (PortletPluginId) o;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("PortletPluginId")
                .addValue(id).toString();
    }
}
