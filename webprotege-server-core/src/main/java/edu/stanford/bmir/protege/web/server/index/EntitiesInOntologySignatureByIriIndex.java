package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
public interface EntitiesInOntologySignatureByIriIndex {

    @Nonnull
    Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri,
                                             @Nonnull OWLOntologyID ontologyId);
}
