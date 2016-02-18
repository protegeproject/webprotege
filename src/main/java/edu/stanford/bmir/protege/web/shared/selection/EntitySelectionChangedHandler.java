package edu.stanford.bmir.protege.web.shared.selection;

import com.google.gwt.event.shared.EventHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public interface EntitySelectionChangedHandler extends EventHandler {

    void handleSelectionChanged(EntitySelectionChangedEvent event);

}
