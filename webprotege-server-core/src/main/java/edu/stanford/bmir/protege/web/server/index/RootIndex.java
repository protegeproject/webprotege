package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-10
 */
public interface RootIndex {

    /**
     * Minimizes and filters the specified list of changes to changes that actually
     * result in mutation of project ontologies.
     * @param changes The list of desired changes.
     * @return A list of changes that will have an effect on project ontologies.
     */
    @Nonnull
    List<OntologyChange> getEffectiveChanges(@Nonnull List<OntologyChange> changes);
}
