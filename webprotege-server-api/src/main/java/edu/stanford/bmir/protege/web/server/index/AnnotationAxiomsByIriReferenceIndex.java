package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public interface AnnotationAxiomsByIriReferenceIndex extends Index {

    /**
     * Returns a stream of axioms from the specified ontology, where:
     *   - The axiom is an AnnotationAssertion axiom that has a subject or object equal to the IRI
     *   - The axiom is a SubAnnotationPropertyOf axiom that has a sub-property or super-property
     *     equal to the specified IRI
     *   - The axiom is an AnnotationPropertyDomain axiom where the domain is equal to the specified IRI
     *   - The axiom is an AnnotationPropertyRange axiom where the range is equal to the specified IRI
     * @param iri The IRI
     * @param ontologyID The ontology identifier
     */
    Stream<OWLAnnotationAxiom> getReferencingAxioms(@Nonnull IRI iri,
                                                    @Nonnull OWLOntologyID ontologyID);
}
