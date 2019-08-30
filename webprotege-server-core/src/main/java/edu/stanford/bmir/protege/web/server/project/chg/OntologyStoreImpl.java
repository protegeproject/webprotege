package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OwlOntologyChangeTranslator;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
class OntologyStoreImpl extends OntologyStore {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OwlOntologyChangeTranslator owlOntologyChangeTranslator;

    @Nonnull
    private final OntologyChangeTranslator ontologyChangeTranslator;

    @Inject
    public OntologyStoreImpl(@Nonnull OWLOntology rootOntology,
                             @Nonnull OwlOntologyChangeTranslator owlOntologyChangeTranslator,
                             @Nonnull OntologyChangeTranslator ontologyChangeTranslator) {
        this.rootOntology = checkNotNull(rootOntology);
        this.owlOntologyChangeTranslator = checkNotNull(owlOntologyChangeTranslator);
        this.ontologyChangeTranslator = checkNotNull(ontologyChangeTranslator);
    }


    @Override
    List<OntologyChange> applyChanges(@Nonnull List<OntologyChange> changes) {
        checkNotNull(changes);
        var ontologyManager = rootOntology.getOWLOntologyManager();
        var owlOntologyChanges = changes.stream()
               .map(ontologyChangeTranslator::toOwlOntologyChange)
               .collect(Collectors.toList());
        var manager = ((ProjectOWLOntologyManager) ontologyManager).getDelegate();
        var appliedOwlOntologyChanges = manager.applyChangesAndGetDetails(owlOntologyChanges)
                                               .getEnactedChanges();
        return appliedOwlOntologyChanges.stream()
                                 .map(owlOntologyChangeTranslator::toOntologyChange)
                                 .collect(Collectors.toList());

    }
}
