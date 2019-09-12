package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureByIriIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
@ProjectSingleton
public class EntitiesInOntologySignatureByIriIndexImpl implements EntitiesInOntologySignatureByIriIndex, DependentIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex;

    @Inject
    public EntitiesInOntologySignatureByIriIndexImpl(@Nonnull AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex) {
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri, @Nonnull OWLOntologyID ontologyId) {
        return axiomsByEntityReferenceIndex.getEntitiesInSignature(iri, ontologyId);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByEntityReferenceIndex);
    }
}
