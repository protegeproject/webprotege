package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
class OntologyStoreImpl extends OntologyStore {

    private final OWLOntology rootOntology;

    @Inject
    public OntologyStoreImpl(OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }


    @Override
    protected List<OWLOntologyChange> applyChanges(@Nonnull List<OWLOntologyChange> changes) {
        checkNotNull(changes);
        var manager = ((ProjectOWLOntologyManager) rootOntology.getOWLOntologyManager()).getDelegate();
        return Collections.unmodifiableList(manager.applyChangesAndGetDetails(changes)
                                                   .getEnactedChanges());

    }
}
