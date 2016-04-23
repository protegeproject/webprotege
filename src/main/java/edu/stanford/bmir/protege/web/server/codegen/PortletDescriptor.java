package edu.stanford.bmir.protege.web.server.codegen;

import edu.stanford.bmir.protege.web.shared.portlet.PortletPluginId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public class PortletDescriptor {

    private PortletPluginId portletPluginId;

    private String packageName;

    private String simpleName;

    public PortletDescriptor(PortletPluginId portletPluginId, String packageName, String simpleName) {
        this.portletPluginId = checkNotNull(portletPluginId);
        this.packageName = checkNotNull(packageName);
        this.simpleName = checkNotNull(simpleName);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public PortletPluginId getPortletPluginId() {
        return portletPluginId;
    }

    @Override
    public int hashCode() {
        return "PortletDescriptor" .hashCode() +
                portletPluginId.hashCode() + packageName.hashCode() + simpleName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PortletDescriptor)) {
            return false;
        }
        PortletDescriptor other = (PortletDescriptor) o;
        return this.portletPluginId.equals(other.portletPluginId)
                && this.packageName.equals(other.packageName)
                && this.simpleName.equals(other.simpleName);
    }
}

