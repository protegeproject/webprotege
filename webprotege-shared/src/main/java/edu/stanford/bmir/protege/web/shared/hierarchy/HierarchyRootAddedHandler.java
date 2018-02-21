package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.gwt.event.shared.EventHandler;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public interface HierarchyRootAddedHandler<T extends OWLEntity> extends EventHandler {

    void handleHierarchyRootAdded(HierarchyRootAddedEvent<T> event);
}
