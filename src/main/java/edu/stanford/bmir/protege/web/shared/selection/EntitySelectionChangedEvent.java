package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class EntitySelectionChangedEvent extends Event<EntitySelectionChangedHandler> {

    private static final Type<EntitySelectionChangedHandler> TYPE = new Type<>();

    private final Optional<? extends OWLEntity> previousSelection;

    private final Optional<? extends OWLEntity> lastSelection;

    public EntitySelectionChangedEvent(Optional<? extends OWLEntity> previousSelection,
                                       Optional<? extends OWLEntity> selection) {
        this.previousSelection = checkNotNull(previousSelection);
        this.lastSelection = checkNotNull(selection);
    }

    public Optional<OWLEntity> getPreviousSelection() {
        if(previousSelection.isPresent()) {
            return Optional.<OWLEntity>of(previousSelection.get());

        }
        else {
            return Optional.<OWLEntity>absent();
        }
    }

    public Optional<OWLEntity> getLastSelection() {
        if(lastSelection.isPresent()) {
            return Optional.<OWLEntity>of(lastSelection.get());

        }
        else {
            return Optional.<OWLEntity>absent();
        }
    }

    @Override
    public Type<EntitySelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EntitySelectionChangedHandler handler) {
        handler.handleSelectionChanged(this);
    }

    public static Type<EntitySelectionChangedHandler> getType() {
        return TYPE;
    }
}
