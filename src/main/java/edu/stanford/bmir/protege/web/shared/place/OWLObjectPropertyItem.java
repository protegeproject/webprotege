package edu.stanford.bmir.protege.web.shared.place;

import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/05/2014
 */
public class OWLObjectPropertyItem extends Item<OWLObjectProperty> {

    private static final Type<OWLObjectProperty> TYPE = new Type<OWLObjectProperty>("ObjectProperty");

    public OWLObjectPropertyItem(OWLObjectProperty item) {
        super(item);
    }

    public static Type<OWLObjectProperty> getType() {
        return TYPE;
    }

    @Override
    public Type<OWLObjectProperty> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String getItemRendering() {
        return getItem().toString();
    }
}
