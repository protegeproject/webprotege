package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.OWLProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public abstract class OWLPropertyData extends OWLEntityData {

    protected OWLPropertyData(OWLProperty entity, String browserText) {
        super(entity, browserText);
    }

    public abstract boolean isOWLAnnotationProperty();

    @Override
    public OWLProperty getEntity() {
        return (OWLProperty) super.getEntity();
    }
}
