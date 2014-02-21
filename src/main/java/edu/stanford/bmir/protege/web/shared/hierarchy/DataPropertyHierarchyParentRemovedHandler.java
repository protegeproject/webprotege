package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public interface DataPropertyHierarchyParentRemovedHandler extends EventHandler {

    void handleDataPropertyHierarchyParentRemoved(DataPropertyHierarchyParentRemovedEvent event);
}
