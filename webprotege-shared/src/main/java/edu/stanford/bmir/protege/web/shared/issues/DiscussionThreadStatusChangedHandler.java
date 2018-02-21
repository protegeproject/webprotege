package edu.stanford.bmir.protege.web.shared.issues;

import com.google.gwt.event.shared.EventHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public interface DiscussionThreadStatusChangedHandler extends EventHandler {

    void handleDiscussionThreadStatusChanged(DiscussionThreadStatusChangedEvent event);
}
