package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.List;

public class SelectOntologiesForMergeDialogController extends WebProtegeOKCancelDialogController<List<OWLOntologyID>> {
    final private SelectOntologiesForMergeView view;

    public SelectOntologiesForMergeDialogController(SelectOntologiesForMergeView view) {
        super("Select Ontologies");
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
    public List<OWLOntologyID> getData() {
        return view.getSelectedOntologies();
    }
}
