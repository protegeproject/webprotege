package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;

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
     * or an {@link OWLAnonymousIndividual} or an {@link OWLLiteral}.
     * @param value The value
     * @param ontologyId The ontology context.
     */
    @Nonnull
    Stream<OWLAnnotationAssertionAxiom> getAxiomsByValue(@Nonnull OWLAnnotationValue value,
                                                         @Nonnull OntologyDocumentId ontologyId);
}
