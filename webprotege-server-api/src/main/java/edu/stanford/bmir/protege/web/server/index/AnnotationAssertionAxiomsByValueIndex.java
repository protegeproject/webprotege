package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-13
 */
@ProjectSingleton
public interface AnnotationAssertionAxiomsByValueIndex extends Index {

    /**
     * Gets the annotation assertion axioms by values, where the value is either an {@link IRI}
     * or an {@link OWLAnonymousIndividual}
     * @param value The value.  The index only indexes against values that are {@link IRI} or
     *              {@link OWLAnonymousIndividual} values.  {@link OWLLiteral} values will return
     *              an empty stream.
     * @param ontologyId The ontology context.
     * @return
     */
    @Nonnull
    Stream<OWLAnnotationAssertionAxiom> getAxiomsByValue(@Nonnull OWLAnnotationValue value,
                                                         @Nonnull OWLOntologyID ontologyId);
}
