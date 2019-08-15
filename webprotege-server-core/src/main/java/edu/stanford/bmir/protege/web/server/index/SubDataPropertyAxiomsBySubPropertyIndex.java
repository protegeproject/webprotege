package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public interface SubDataPropertyAxiomsBySubPropertyIndex {

    @Nonnull
    Stream<OWLSubDataPropertyOfAxiom> getSubPropertyOfAxioms(@Nonnull
                                                             OWLDataProperty dataProperty,
                                                             @Nonnull
                                                             OWLOntologyID ontologyID);
}
