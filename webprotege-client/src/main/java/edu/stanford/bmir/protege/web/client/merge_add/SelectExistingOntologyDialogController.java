package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

public class SelectExistingOntologyDialogController extends WebProtegeOKCancelDialogController<OntologyDocumentId> {
    final private SelectExistingOntologyView view;

    public SelectExistingOntologyDialogController(SelectExistingOntologyView view) {
        super("Select Ontology");
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public OntologyDocumentId getData(){
        return view.getOntology();
    }
}
