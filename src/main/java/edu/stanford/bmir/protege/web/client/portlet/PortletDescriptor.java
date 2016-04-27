package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.shared.PortletId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletDescriptor {

    private final PortletId portletId;

    private final String displayName;

    private final String tooltip;


    public PortletDescriptor(PortletId portletId, String displayName, String tooltip) {
        this.portletId = checkNotNull(portletId);
        this.displayName = checkNotNull(displayName);
        this.tooltip = checkNotNull(tooltip);
    }

    public PortletId getPortletId() {
        return portletId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTooltip() {
        return tooltip;
    }
}
