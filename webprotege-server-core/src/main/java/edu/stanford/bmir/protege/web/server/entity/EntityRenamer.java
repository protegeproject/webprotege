package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.change.OntologyChangeFactory;
import edu.stanford.bmir.protege.web.server.index.AxiomsByReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
public class EntityRenamer {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Nonnull
    private final OntologyChangeFactory changeFactory;

    @Inject
    public EntityRenamer(@Nonnull OWLDataFactory dataFactory,
                         @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                         @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex,
                         @Nonnull OntologyChangeFactory changeFactory) {
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.axiomsByReferenceIndex = axiomsByReferenceIndex;
        this.changeFactory = changeFactory;
    }

    public List<OWLOntologyChange> generateChanges(@Nonnull Map<OWLEntity, IRI> iriReplacementMap) {
        OWLObjectDuplicator duplicator = new OWLObjectDuplicator(iriReplacementMap, dataFactory);
        var changes = new ArrayList<OWLOntologyChange>();
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> {
            axiomsByReferenceIndex.getReferencingAxioms(iriReplacementMap.keySet(), ontId)
                                  .forEach(ax -> {
                                      var removeAxiom = changeFactory.createRemoveAxiom(ontId, ax);
                                      changes.add(removeAxiom);
                                      OWLAxiom replacementAx = duplicator.duplicateObject(ax);
                                      var addAxiom = changeFactory.createAddAxiom(ontId, replacementAx);
                                      changes.add(addAxiom);
                                  });
        });
        return changes;
    }
}
