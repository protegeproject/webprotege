package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public class TripleMapperSelector {
    
    private OWLAPIProject project;

    private AnnotationsTreatment annotationsTreatment;

    private NonAnnotationTreatment nonAnnotationTreatment;
    
    public TripleMapperSelector(OWLAPIProject project, AnnotationsTreatment annotationsTreatment, NonAnnotationTreatment nonAnnotationTreatment) {
        this.project = project;
        this.annotationsTreatment = annotationsTreatment;
        this.nonAnnotationTreatment = nonAnnotationTreatment;
    }

    public TripleMapper<?> getMapper(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<TripleMapper<?>>() {
            public TripleMapper<OWLClass> visit(OWLClass owlClass) {
                return new OWLClassTripleMapper(project, owlClass, annotationsTreatment, nonAnnotationTreatment);
            }

            public TripleMapper<OWLObjectProperty> visit(OWLObjectProperty owlObjectProperty) {
                return new OWLObjectPropertyTripleMapper(project, owlObjectProperty, annotationsTreatment, nonAnnotationTreatment);
            }

            public TripleMapper<OWLDataProperty> visit(OWLDataProperty owlDataProperty) {
                return new OWLDataPropertyTripleMapper(project, owlDataProperty, annotationsTreatment, nonAnnotationTreatment);
            }

            public TripleMapper<OWLNamedIndividual> visit(OWLNamedIndividual owlNamedIndividual) {
                return new OWLNamedIndividualTripleMapper(project, owlNamedIndividual, annotationsTreatment, NonAnnotationTreatment.INCLUDE_NON_ANNOTATIONS);
            }

            public TripleMapper<OWLDatatype> visit(OWLDatatype owlDatatype) {
                return null;
            }

            public TripleMapper<OWLAnnotationProperty> visit(OWLAnnotationProperty owlAnnotationProperty) {
                return null;
            }
        });
    }
    
    
}
