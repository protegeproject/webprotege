package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

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
        setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            handler.handleCreateEntity(data);
            closer.hide();
        });
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public CreateEntityInfo getData() {
        return new CreateEntityInfo(form.getEntityBrowserText());
    }


    public interface CreateEntityHandler {

        void handleCreateEntity(CreateEntityInfo createEntityInfo);
    }
}
