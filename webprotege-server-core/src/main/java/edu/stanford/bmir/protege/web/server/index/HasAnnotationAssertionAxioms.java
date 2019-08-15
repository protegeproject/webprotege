package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public interface HasAnnotationAssertionAxioms {

    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(OWLAnnotationSubject subject);
}
