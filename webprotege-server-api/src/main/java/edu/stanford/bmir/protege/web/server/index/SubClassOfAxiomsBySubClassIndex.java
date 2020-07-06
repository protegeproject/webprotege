package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public interface SubClassOfAxiomsBySubClassIndex extends Index {

    Stream<OWLSubClassOfAxiom> getSubClassOfAxiomsForSubClass(@Nonnull OWLClass subClass,
                                                              @Nonnull OWLOntologyID ontologyID);
}
