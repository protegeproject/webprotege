package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.gwt.event.shared.EventHandler;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 1 Dec 2017
 */
public interface EntityHierarchyChangedHandler extends EventHandler {
    void handleHierarchyChanged(EntityHierarchyChangedEvent event);
}
