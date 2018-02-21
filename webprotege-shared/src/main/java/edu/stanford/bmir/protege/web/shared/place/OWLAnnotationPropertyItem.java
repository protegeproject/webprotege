package edu.stanford.bmir.protege.web.shared.place;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/05/2014
 */
public class OWLAnnotationPropertyItem extends Item<OWLAnnotationProperty> {

    private static final Type<OWLAnnotationProperty> TYPE = new Type<OWLAnnotationProperty>("AnnotationProperty");

    public OWLAnnotationPropertyItem(OWLAnnotationProperty item) {
        super(item);
    }

    public static Type<OWLAnnotationProperty> getType() {
        return TYPE;
    }

    @Override
    public Type<OWLAnnotationProperty> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String getItemRendering() {
        return getItem().toString();
    }
}
