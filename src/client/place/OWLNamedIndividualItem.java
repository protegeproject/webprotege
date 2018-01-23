package edu.stanford.bmir.protege.web.client.place;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/05/2014
 */
public class OWLNamedIndividualItem extends Item<OWLNamedIndividual> {

    private static final Type<OWLNamedIndividual> TYPE = new Type<OWLNamedIndividual>("NamedIndividual");

    public OWLNamedIndividualItem(OWLNamedIndividual item) {
        super(item);
    }

    public static Type<OWLNamedIndividual> getType() {
        return TYPE;
    }

    @Override
    public Type<OWLNamedIndividual> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String getItemRendering() {
        return getItem().toString();
    }
}
