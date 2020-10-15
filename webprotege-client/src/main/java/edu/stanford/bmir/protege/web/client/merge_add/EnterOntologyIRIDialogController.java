package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;

import javax.annotation.Nonnull;

public class EnterOntologyIRIDialogController extends WebProtegeOKCancelDialogController<String> {
    private EnterOntologyIRIView view;

    public EnterOntologyIRIDialogController(EnterOntologyIRIView view) {
        super("Ontology IRI");
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
    public String getData() {
        return view.getOntologyIRI();
    }
}
