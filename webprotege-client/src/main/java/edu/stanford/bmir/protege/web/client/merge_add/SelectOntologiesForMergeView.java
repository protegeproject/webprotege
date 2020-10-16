package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

public interface SelectOntologiesForMergeView extends IsWidget, HasInitialFocusable {
    List<OWLOntologyID> getSelectedOntologies();
}
