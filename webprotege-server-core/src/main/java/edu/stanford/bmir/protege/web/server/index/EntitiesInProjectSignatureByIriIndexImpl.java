package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
public class EntitiesInProjectSignatureByIriIndexImpl implements EntitiesInProjectSignatureByIriIndex {

    @Nonnull
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private OntologyIndex ontologyIndex;

    @Inject
    public EntitiesInProjectSignatureByIriIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                    @Nonnull OntologyIndex ontologyIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntityInSignature(@Nonnull IRI entityIri) {
        checkNotNull(entityIri);
        return projectOntologiesIndex.getOntologyIds()
                                     .map(ontId -> ontologyIndex.getOntology(ontId))
                                     .flatMap(Optional::stream)
                                     .flatMap(ont -> getEntitiesInSignature(ont, entityIri));
    }

    private Stream<OWLEntity> getEntitiesInSignature(OWLOntology ont, IRI entityIri) {
        return ont.getEntitiesInSignature(entityIri).stream();
    }
}
