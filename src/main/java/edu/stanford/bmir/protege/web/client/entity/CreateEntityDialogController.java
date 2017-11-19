package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogController;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogValidator;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CREATE;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialogController extends WebProtegeDialogController<CreateEntityInfo> {

    private final CreateEntityForm form;



    public CreateEntityDialogController(EntityType<?> type, final CreateEntityHandler handler, Messages messages) {
        super(messages.create() + " " + type.getName(), asList(CANCEL, CREATE), CREATE, CANCEL);
        form = new CreateEntityForm(type);
        for(WebProtegeDialogValidator validator : form.getDialogValidators()) {
            addDialogValidator(validator);
        }
        setDialogButtonHandler(DialogButton.get(messages.create()), (data, closer) -> {
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
    public Optional<HasRequestFocus> getInitialFocusable() {
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
