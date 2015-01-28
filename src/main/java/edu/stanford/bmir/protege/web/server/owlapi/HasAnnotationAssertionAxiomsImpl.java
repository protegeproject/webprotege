package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public class HasAnnotationAssertionAxiomsImpl implements HasAnnotationAssertionAxioms {

    private final OWLOntology rootOntology;

    public HasAnnotationAssertionAxiomsImpl(OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Override
    public Set<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(OWLAnnotationSubject subject) {
        Set<OWLAnnotationAssertionAxiom> result = new HashSet<>();
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            result.addAll(ontology.getAnnotationAssertionAxioms(subject));
        }
        return result;
    }
}
