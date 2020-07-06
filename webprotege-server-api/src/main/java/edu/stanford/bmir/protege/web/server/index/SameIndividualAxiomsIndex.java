package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@ProjectSingleton
public interface SameIndividualAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLSameIndividualAxiom> getSameIndividualAxioms(@Nonnull
                                                           OWLIndividual individual,
                                                           @Nonnull
                                                           OWLOntologyID ontologyId);
}
