package edu.stanford.bmir.protege.web.shared.entity;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 */
public class OWLEntityDataVisitorEx<R> {

    public R getDefaultReturnValue(OWLEntityData data) {
        return null;
    }

    public R visit(OWLClassData data) {
        return getDefaultReturnValue(data);
    }

    public R visit(OWLObjectPropertyData data) {
        return getDefaultReturnValue(data);
    }

    public R visit(OWLDataPropertyData data) {
        return getDefaultReturnValue(data);
    }

    public R visit(OWLAnnotationPropertyData data) {
        return getDefaultReturnValue(data);
    }

    public R visit(OWLNamedIndividualData data) {
        return getDefaultReturnValue(data);
    }

    public R visit(OWLDatatypeData data) {
        return getDefaultReturnValue(data);
    }
}
