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

    public EntityDataSelectionChangedEvent(Optional<? extends OWLEntityData> previousSelection,
                                           Optional<? extends OWLEntityData> selection) {
        this.previousSelection = checkNotNull(previousSelection);
        this.lastSelection = checkNotNull(selection);
    }

    public Optional<OWLEntityData> getPreviousSelection() {
        if(previousSelection.isPresent()) {
            return Optional.<OWLEntityData>of(previousSelection.get());

        }
        else {
            return Optional.<OWLEntityData>absent();
        }
    }

    public Optional<OWLEntityData> getLastSelection() {
        if(lastSelection.isPresent()) {
            return Optional.<OWLEntityData>of(lastSelection.get());

        }
        else {
            return Optional.<OWLEntityData>absent();
        }
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
