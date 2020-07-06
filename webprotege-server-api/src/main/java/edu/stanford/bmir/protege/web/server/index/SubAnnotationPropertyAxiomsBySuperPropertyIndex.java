package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@ProjectSingleton
public interface SubAnnotationPropertyAxiomsBySuperPropertyIndex extends Index {

    @Nonnull
    Stream<OWLSubAnnotationPropertyOfAxiom> getAxiomsForSuperProperty(@Nonnull OWLAnnotationProperty property,
                                                                      @Nonnull OWLOntologyID ontologyId);
}
