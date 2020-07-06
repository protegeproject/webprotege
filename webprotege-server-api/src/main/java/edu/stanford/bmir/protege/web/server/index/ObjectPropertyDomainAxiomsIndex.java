package edu.stanford.bmir.protege.web.server.index;


import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public interface ObjectPropertyDomainAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(@Nonnull
                                                                       OWLObjectProperty property,
                                                                       @Nonnull
                                                                       OWLOntologyID ontologyId);
}
