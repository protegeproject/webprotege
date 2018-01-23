package edu.stanford.bmir.protege.web.client.place;

import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/05/2014
 */
public class OWLDataPropertyItem extends Item<OWLDataProperty> {

    private static final Type<OWLDataProperty> TYPE = new Type<OWLDataProperty>("DataProperty");

    public static Type<OWLDataProperty> getType() {
        return TYPE;
    }

    public OWLDataPropertyItem(OWLDataProperty item) {
        super(item);
    }

    @Override
    public Type<OWLDataProperty> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String getItemRendering() {
        return getItem().toString();
    }
}
