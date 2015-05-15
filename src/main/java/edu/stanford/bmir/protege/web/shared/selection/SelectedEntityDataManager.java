package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class SelectedEntityDataManager<D extends OWLEntityData> {

    private final EventBus eventBus;

    private Optional<D> lastSelection = Optional.absent();


    public SelectedEntityDataManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Optional<D> getLastSelection() {
        return lastSelection;
    }

    public void setSelection(D entityData) {
        checkNotNull(entityData);
        Optional<D> sel = Optional.of(entityData);
        if(!sel.equals(lastSelection)) {
            Optional<D> lastSelectionCopy = lastSelection;
            lastSelection = sel;
            fireEvent(lastSelectionCopy);
        }
    }

    private void fireEvent(Optional<D> previousLastSelection) {
        eventBus.fireEvent(new EntityDataSelectionChangedEvent(previousLastSelection, lastSelection));
    }
}
