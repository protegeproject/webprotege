package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologySignatureIndex;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public class OntologySignatureIndexImpl implements OntologySignatureIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public OntologySignatureIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull OWLOntologyID ontologyID) {
        checkNotNull(ontologyID);
        return ontologyIndex.getOntology(ontologyID)
                .stream()
                .flatMap(ont -> ont.getSignature().stream());
    }
}
