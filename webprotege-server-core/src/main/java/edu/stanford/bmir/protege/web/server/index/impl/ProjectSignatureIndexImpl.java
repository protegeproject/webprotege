package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologySignatureIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public class ProjectSignatureIndexImpl implements ProjectSignatureIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologySignatureIndex ontologySignatureIndex;

    @Inject
    public ProjectSignatureIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                     @Nonnull OntologySignatureIndex ontologySignatureIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologySignatureIndex = checkNotNull(ontologySignatureIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getSignature() {
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontologySignatureIndex::getEntitiesInSignature);
    }
}
