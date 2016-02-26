package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.shared.PortletId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletDescriptor {

    private final PortletId portletId;

    private final String displayName;

    public PortletDescriptor(PortletId portletId, String displayName) {
        this.portletId = portletId;
        this.displayName = displayName;
    }

    public PortletId getPortletId() {
        return portletId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
