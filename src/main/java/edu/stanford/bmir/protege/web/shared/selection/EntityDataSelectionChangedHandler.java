package edu.stanford.bmir.protege.web.shared.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public interface EntityDataSelectionChangedHandler extends EventHandler {

    void handleSelectionChanged(EntityDataSelectionChangedEvent event);

}
