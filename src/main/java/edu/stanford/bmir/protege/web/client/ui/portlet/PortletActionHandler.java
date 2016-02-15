package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public interface PortletActionHandler {

    void handleActionInvoked(PortletAction action, ClickEvent event);
}
