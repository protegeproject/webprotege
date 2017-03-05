package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;

/**
 * A WebProtegePortlet is basically a presenter (as in Model View Presenter) that contains the logic for displaying
 * some part of a project in the WebProtege user interface.  The displayed content is typically tied to the selection
 * with different content being displayed for different selected entities.
 */
public interface WebProtegePortletPresenter extends HasDispose {

    void start(PortletUi portletUi, WebProtegeEventBus eventBus);
}
