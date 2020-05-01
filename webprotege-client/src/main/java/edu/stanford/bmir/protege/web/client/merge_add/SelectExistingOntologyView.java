package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import org.semanticweb.owlapi.model.OWLOntologyID;

public interface SelectExistingOntologyView extends IsWidget, HasInitialFocusable {
    OWLOntologyID getOntology();
}
