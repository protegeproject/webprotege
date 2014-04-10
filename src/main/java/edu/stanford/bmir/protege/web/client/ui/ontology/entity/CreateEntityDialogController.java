package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialogController extends WebProtegeOKCancelDialogController<CreateEntityInfo> {

    private final CreateEntityForm form;

    public CreateEntityDialogController(EntityType<?> type, final CreateEntityHandler handler) {
        super("Create " + type.getName());
        form = new CreateEntityForm(type);
        for(WebProtegeDialogValidator validator : form.getDialogValidators()) {
            addDialogValidator(validator);
        }
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<CreateEntityInfo>() {
            @Override
            public void handleHide(CreateEntityInfo data, WebProtegeDialogCloser closer) {
                handler.handleCreateEntity(data);
                closer.hide();

            }
        });
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public CreateEntityInfo getData() {
        return new CreateEntityInfo(form.getEntityBrowserText());
    }


    public static interface CreateEntityHandler {

        void handleCreateEntity(CreateEntityInfo createEntityInfo);
    }
}
