package edu.stanford.bmir.protege.web.server.index;


import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public interface EntitiesInOntologySignatureIndex extends Index {

    boolean containsEntityInSignature(@Nonnull OWLEntity entity,
                                      @Nonnull OWLOntologyID ontologyId);
}
