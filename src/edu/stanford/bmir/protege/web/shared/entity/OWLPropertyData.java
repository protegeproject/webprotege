package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public abstract class OWLPropertyData extends OWLEntityData {

    protected OWLPropertyData(OWLEntity entity, String browserText) {
        super(entity, browserText);
    }
}
