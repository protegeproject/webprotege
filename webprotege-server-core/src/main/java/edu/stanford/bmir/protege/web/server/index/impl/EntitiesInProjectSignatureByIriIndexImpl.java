package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
public class EntitiesInProjectSignatureByIriIndexImpl implements EntitiesInProjectSignatureByIriIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final EntitiesInOntologySignatureByIriIndex entitiesInOntologySignatureByIriIndex;

    @Inject
    public EntitiesInProjectSignatureByIriIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                    @Nonnull EntitiesInOntologySignatureByIriIndex entitiesInOntologySignatureByIriIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.entitiesInOntologySignatureByIriIndex = checkNotNull(entitiesInOntologySignatureByIriIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI entityIri) {
        checkNotNull(entityIri);
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> entitiesInOntologySignatureByIriIndex.getEntitiesInSignature(entityIri, ontId))
                                     .distinct();
    }
}
