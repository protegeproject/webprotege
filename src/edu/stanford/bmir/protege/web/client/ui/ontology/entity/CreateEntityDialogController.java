package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialogController extends WebProtegeOKCancelDialogController<EntityData> {

    private final CreateEntityForm form = new CreateEntityForm();

    public CreateEntityDialogController() {
        super("Create entity");
    }

    @Override
    public List<WebProtegeDialogValidator> getDialogValidators() {
        return form.getDialogValidators();
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
    public EntityData getData() {
        return new EntityData("TODO", form.getEntityBrowserText());
    }
}
