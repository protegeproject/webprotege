package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
public interface LargeNumberOfChangesHandler extends EventHandler {

    void handleLargeNumberOfChanges(LargeNumberOfChangesEvent event);
}
