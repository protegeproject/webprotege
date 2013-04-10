package edu.stanford.bmir.protege.web.shared.entity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2012
 */
public interface OWLPrimitiveDataVisitor<R, E extends Throwable> {

    R visit(OWLClassData data) throws E;

    R visit(OWLObjectPropertyData data) throws E;

    R visit(OWLDataPropertyData data) throws E;

    R visit(OWLAnnotationPropertyData data) throws E;

    R visit(OWLNamedIndividualData data) throws E;

    R visit(OWLDatatypeData data) throws E;

    R visit(OWLLiteralData data) throws E;

    R visit(IRIData data) throws E;

}
