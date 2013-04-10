package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialogController extends WebProtegeOKCancelDialogController<CreateEntityInfo> {

    private final CreateEntityForm form;

    public CreateEntityDialogController(EntityType<?> type) {
        super("Create " + type.getName());
        form = new CreateEntityForm(type);
        for(WebProtegeDialogValidator validator : form.getDialogValidators()) {
            addDialogValidator(validator);
        }
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Override
    public Focusable getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public CreateEntityInfo getData() {
        return new CreateEntityInfo(form.getEntityBrowserText());
    }
}
