package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class SelectedEntityDataManager<D extends OWLEntityData> {


    private Optional<D> lastSelection = Optional.absent();


    public SelectedEntityDataManager() {
    }

    public Optional<D> getLastSelection() {
        return lastSelection;
    }

    public void setSelection(D entityData) {
        checkNotNull(entityData);
        lastSelection = Optional.of(entityData);
    }


}
