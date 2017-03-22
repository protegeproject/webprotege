package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class ObjectPropertyValue extends PropertyValue implements ClassFramePropertyValue {


    protected ObjectPropertyValue() {
    }

    public ObjectPropertyValue(OWLObjectPropertyData property, OWLNamedIndividualData value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    protected ObjectPropertyValue(OWLObjectPropertyData property, OWLClassData value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    @Override
    final public OWLObjectPropertyData getProperty() {
        return (OWLObjectPropertyData) super.getProperty();
    }
}
