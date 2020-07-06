package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ProjectSingleton
public interface ClassAssertionAxiomsByClassIndex extends Index {

    @Nonnull
    Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLClass cls,
                                                           @Nonnull OWLOntologyID ontologyId);
}
