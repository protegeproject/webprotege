package edu.stanford.bmir.protege.web.server.index;


import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public interface DataPropertyRangeAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxioms(@Nonnull OWLDataProperty dataProperty,
                                                                 @Nonnull OWLOntologyID ontologyID);
}
