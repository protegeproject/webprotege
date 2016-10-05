package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class SelectedEntityManager<D extends OWLEntity> {


    private Optional<D> lastSelection = Optional.absent();


    @Inject
    public SelectedEntityManager() {
    }

    public Optional<D> getLastSelection() {
        return lastSelection;
    }

    public void setSelection(D entityData) {
        checkNotNull(entityData);
        lastSelection = Optional.of(entityData);
    }


}
