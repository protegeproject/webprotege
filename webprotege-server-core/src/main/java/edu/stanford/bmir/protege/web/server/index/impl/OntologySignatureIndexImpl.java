package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.server.index.OntologySignatureIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.EntityType;
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
 * 2019-08-15
 */
@ProjectSingleton
public class OntologySignatureIndexImpl implements OntologySignatureIndex, DependentIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex;

    @Inject
    public OntologySignatureIndexImpl(@Nonnull AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex) {
        this.axiomsByEntityReferenceIndex = checkNotNull(axiomsByEntityReferenceIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByEntityReferenceIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull OWLOntologyID ontologyID) {
        checkNotNull(ontologyID);
        var streamsBuilder = Stream.<Stream<? extends OWLEntity>>builder();
        EntityType.values().forEach(entityType -> {
            var sig = axiomsByEntityReferenceIndex.getOntologyAxiomsSignature(entityType,
                                                                    ontologyID);
            streamsBuilder.add(sig);
        });
        return streamsBuilder.build().flatMap(s -> s);
    }
}
