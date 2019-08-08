package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
public class AxiomsByEntityReferenceIndexImpl implements AxiomsByEntityReferenceIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public AxiomsByEntityReferenceIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    @Override
    public Stream<OWLAxiom> getReferencingAxioms(@Nonnull OWLEntity entity,
                                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(entity);
        checkNotNull(ontologyId);
        return projectOntologiesIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ontology -> ontology.getReferencingAxioms(entity).stream());
    }
}
