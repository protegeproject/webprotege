package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.index.*;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public class AxiomsByReferenceIndexImpl implements AxiomsByReferenceIndex, DependentIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Nonnull
    private final AnnotationAxiomsByIriReferenceIndex axiomsByIriReferenceIndex;

    @Inject
    public AxiomsByReferenceIndexImpl(@Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                                      @Nonnull AnnotationAxiomsByIriReferenceIndex axiomsByIriReferenceIndex) {
        this.axiomsByEntityReferenceIndex = checkNotNull(axiomsByEntityReferenceIndex);
        this.axiomsByIriReferenceIndex = checkNotNull(axiomsByIriReferenceIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByIriReferenceIndex, axiomsByEntityReferenceIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> getReferencingAxioms(@Nonnull Collection<OWLEntity> entities,
                                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(entities);
        return entities.stream().flatMap(entity -> getReferencingAxioms(entity, ontologyId));
    }


    @Nonnull
    private Stream<OWLAxiom> getReferencingAxioms(OWLEntity entity,
                                                  OWLOntologyID ontologyId) {
        // Combine both entity in signature and IRI mentions in annotation axioms
        var entityReferencingAxioms = axiomsByEntityReferenceIndex.getReferencingAxioms(entity, ontologyId);
        var iriReferencingAxioms = axiomsByIriReferenceIndex.getReferencingAxioms(entity.getIRI(), ontologyId);
        return Streams.concat(entityReferencingAxioms, iriReferencingAxioms);
    }
}
