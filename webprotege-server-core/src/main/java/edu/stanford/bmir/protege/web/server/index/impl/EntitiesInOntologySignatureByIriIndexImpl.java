package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
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
 * 2019-09-05
 */
@ProjectSingleton
public class EntitiesInOntologySignatureByIriIndexImpl implements EntitiesInOntologySignatureByIriIndex, DependentIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex;

    @Nonnull
    private final OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Inject
    public EntitiesInOntologySignatureByIriIndexImpl(@Nonnull AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex,
                                                     @Nonnull OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex) {
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
        this.ontologyAnnotationsSignatureIndex = ontologyAnnotationsSignatureIndex;
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri, @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(iri);
        checkNotNull(ontologyId);
        var axiomsSignature = axiomsByEntityReferenceIndex.getEntitiesInSignatureWithIri(iri, ontologyId);
        var ontologyAnnotationsSignature = ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyId)
                .filter(entity -> entity.getIRI().equals(iri));
        return Streams.concat(axiomsSignature, ontologyAnnotationsSignature);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByEntityReferenceIndex);
    }
}
