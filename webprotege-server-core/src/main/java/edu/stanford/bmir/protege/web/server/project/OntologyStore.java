package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@ProjectSingleton
abstract class OntologyStore {

    protected abstract List<OWLOntologyChange> applyChanges(@Nonnull List<OWLOntologyChange> changes);
}
