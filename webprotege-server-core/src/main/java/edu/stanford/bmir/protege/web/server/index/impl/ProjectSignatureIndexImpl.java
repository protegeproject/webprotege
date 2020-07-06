package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

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
public class ProjectSignatureIndexImpl implements ProjectSignatureIndex, DependentIndex {

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
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, ontologySignatureIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getSignature() {
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontologySignatureIndex::getEntitiesInSignature);
    }
}
