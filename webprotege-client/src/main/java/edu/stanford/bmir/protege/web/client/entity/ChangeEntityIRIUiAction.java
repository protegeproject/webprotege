package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.shared.entity.ChangeEntityIRIAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A UI action that lets the user change (rename) the IRI of the selected entity.  It prompts for the
 * new IRI, pre-filled with the entity's current IRI, and dispatches a {@link ChangeEntityIRIAction}.
 * The server side is gated on the EDIT_ONTOLOGY permission.
 */
public class ChangeEntityIRIUiAction extends AbstractUiAction {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final InputBox inputBox;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private Supplier<Optional<OWLEntityData>> selectionSupplier = Optional::empty;

    @Nonnull
    private Runnable changeHandler = () -> {};

    @Inject
    public ChangeEntityIRIUiAction(@Nonnull ProjectId projectId,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull InputBox inputBox,
                                   @Nonnull Messages messages) {
        super(messages.changeEntityIri());
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.inputBox = checkNotNull(inputBox);
        this.messages = checkNotNull(messages);
    }

    /**
     * Sets the supplier that provides the entity whose IRI should be changed.
     */
    public void setSelectionSupplier(@Nonnull Supplier<Optional<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
    }

    /**
     * Sets a handler that is run after a successful IRI change, e.g. to refresh the tree.
     */
    public void setChangeHandler(@Nonnull Runnable changeHandler) {
        this.changeHandler = checkNotNull(changeHandler);
    }

    @Override
    public void execute() {
        selectionSupplier.get().ifPresent(this::changeIriForEntity);
    }

    private void changeIriForEntity(@Nonnull OWLEntityData entityData) {
        String currentIri = entityData.getEntity().getIRI().toString();
        inputBox.showDialog(messages.changeEntityIri(), false, currentIri, newIriValue -> {
            String trimmedIri = newIriValue.trim();
            if (trimmedIri.isEmpty() || trimmedIri.equals(currentIri)) {
                return;
            }
            dispatchServiceManager.execute(
                    new ChangeEntityIRIAction(projectId, entityData.getEntity(), IRI.create(trimmedIri)),
                    result -> changeHandler.run());
        });
    }
}
