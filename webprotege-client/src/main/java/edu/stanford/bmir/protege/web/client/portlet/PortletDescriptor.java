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

    private final String title;

    private final String tooltip;


    public PortletDescriptor(PortletId portletId, String title, String tooltip) {
        this.portletId = checkNotNull(portletId);
        this.title = checkNotNull(title);
        this.tooltip = checkNotNull(tooltip);
    }

    public PortletId getPortletId() {
        return portletId;
    }

    public String getTitle() {
        return title;
    }

    public String getTooltip() {
        return tooltip;
    }
}
