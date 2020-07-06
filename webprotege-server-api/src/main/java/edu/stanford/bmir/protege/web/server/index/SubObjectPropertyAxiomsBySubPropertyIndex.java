package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public interface SubObjectPropertyAxiomsBySubPropertyIndex extends Index {

    @Nonnull
    Stream<OWLSubObjectPropertyOfAxiom> getSubPropertyOfAxioms(@Nonnull
                                                               OWLObjectProperty property,
                                                               @Nonnull
                                                               OWLOntologyID ontologyId);
}
