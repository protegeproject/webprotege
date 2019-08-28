package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslator;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

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

    private final OWLOntology rootOntology;

    private final OntologyChangeRecordTranslator changeRecordTranslator;

    @Inject
    public OntologyStoreImpl(OWLOntology rootOntology,
                             OntologyChangeRecordTranslator changeRecordTranslator) {
        this.rootOntology = checkNotNull(rootOntology);
        this.changeRecordTranslator = changeRecordTranslator;
    }


    @Override
    List<OntologyChange> applyChanges(@Nonnull List<OntologyChange> changes) {
        checkNotNull(changes);
        var ontologyManager = rootOntology.getOWLOntologyManager();
        var owlOntologyChanges = changes.stream()
               .map(OntologyChange::toOwlOntologyChangeRecord)
               .map(rec -> rec.createOntologyChange(ontologyManager))
               .collect(Collectors.toList());
        var manager = ((ProjectOWLOntologyManager) ontologyManager).getDelegate();
        var appliedOwlOntologyChanges = manager.applyChangesAndGetDetails(owlOntologyChanges)
                                               .getEnactedChanges();
        return appliedOwlOntologyChanges.stream()
                                 .map(OWLOntologyChange::getChangeRecord)
                                 .map(changeRecordTranslator::getOntologyChange)
                                 .collect(Collectors.toList());

    }
}
