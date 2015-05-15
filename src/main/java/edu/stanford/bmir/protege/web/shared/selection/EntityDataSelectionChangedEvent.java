package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class EntityDataSelectionChangedEvent extends Event<EntityDataSelectionChangedHandler> {

    private static final Type<EntityDataSelectionChangedHandler> TYPE = new Type<>();

    private final Optional<? extends OWLEntityData> previousSelection;

    private final Optional<? extends OWLEntityData> lastSelection;

    public <T extends OWLEntityData> EntityDataSelectionChangedEvent(Optional<T> previousSelection,
                                           Optional<T> selection) {
        this.previousSelection = checkNotNull(previousSelection);
        this.lastSelection = checkNotNull(selection);
    }

    public Optional<? extends OWLEntityData> getPreviousSelection() {
        return previousSelection;
    }

    public Optional<? extends OWLEntityData> getLastSelection() {
        return lastSelection;
    }

    @Override
    public Type<EntityDataSelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EntityDataSelectionChangedHandler handler) {
        handler.handleSelectionChanged(this);
    }

    public static Type<EntityDataSelectionChangedHandler> getType() {
        return TYPE;
    }
}
