package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public interface SubAnnotationPropertyAxiomsBySubPropertyIndex {

    @Nonnull
    Stream<OWLSubAnnotationPropertyOfAxiom> getSubPropertyOfAxioms(@Nonnull OWLAnnotationProperty property,
                                                                   @Nonnull
                                                             OWLOntologyID ontologyId);
}
