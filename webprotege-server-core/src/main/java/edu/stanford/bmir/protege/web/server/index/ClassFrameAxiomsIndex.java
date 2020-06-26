package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.*;

import java.util.Set;
import java.util.stream.Stream;

public interface ClassFrameAxiomsIndex {

    Set<OWLAxiom> getFrameAxioms(OWLClass subject, AnnotationsTreatment annotationsTreatment);

    Stream<OWLSubClassOfAxiom> getFrameSubClassOfAxioms(OWLClass subject);

    Stream<OWLEquivalentClassesAxiom> getFrameEquivalentClassesAxioms(OWLClass subject);

    Stream<OWLAnnotationAssertionAxiom> getFrameAnnotationAssertionsAxiom(OWLClass subject);

    public enum AnnotationsTreatment {
        INCLUDE_ANNOTATIONS,
        EXCLUDE_ANNOTATIONS
    }
}
