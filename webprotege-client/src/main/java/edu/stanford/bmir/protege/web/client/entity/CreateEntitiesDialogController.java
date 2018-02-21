package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogController;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CREATE;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 08/06/2012
 */
public class CreateEntitiesDialogController extends WebProtegeDialogController<String> {

    @Nonnull
    private final CreateEntityDialogView view;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private CreateEntityHandler createEntityHandler = createEntityInfo -> {};

    @Inject
    public CreateEntitiesDialogController(@Nonnull Messages messages,
                                          @Nonnull CreateEntityDialogView view) {
        super(messages.create(), asList(CANCEL, CREATE), CREATE, CANCEL);
        this.messages = checkNotNull(messages);
        this.view = checkNotNull(view);
        setDialogButtonHandler(CREATE, (data, closer) -> {
            createEntityHandler.handleCreateEntity(data);
            closer.hide();
        });
    }

    public void clear() {
        view.clear();
    }

    public void setCreateEntityHandler(@Nonnull CreateEntityHandler handler) {
        this.createEntityHandler = checkNotNull(handler);
    }

    public void setEntityType(@Nonnull EntityType<?> entityType) {
        setTitle(messages.create() + " " + entityType.getPrintName());
    }


    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public String getData() {
        return view.getText();
    }


    public interface CreateEntityHandler {

        void handleCreateEntity(@Nonnull String createFromText);
    }
}
